package com.sqx.modules.tearoom.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.sys.entity.SysUserEntity;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Room;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@Api(value = "商品信息", tags = {"商品信息"})
@RequestMapping(value = "/goods")
public class GoodsController extends AbstractController {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RoomService roomService;

    @PostMapping("/insertGoods")
    @ApiOperation("添加商品信息")
    public Result insertGoods(@RequestBody Goods goods){
        goods.setCreateTime(DateUtils.format(new Date()));
        goods.setStatus(1);
        goods.setGoodsStatus(1);
        goodsService.save(goods);
        return Result.success();
    }

    @PostMapping("/updateGoods")
    @ApiOperation("查询商品信息")
    public Result updateGoods(@RequestBody Goods goods){
        goodsService.updateById(goods);
        return Result.success();
    }

    @PostMapping("/deleteGoodsById")
    @ApiOperation("删除商品信息")
    public Result deleteGoodsById(Long goodsId){
        goodsService.removeById(goodsId);
        roomService.remove(new QueryWrapper<Room>().eq("goods_id",goodsId));
        return Result.success();
    }

    @GetMapping("/selectGoodsById")
    @ApiOperation("查询商品详情")
    public Result selectGoodsById(Long goodsId){
        return Result.success().put("data",goodsService.selectGoodsById(goodsId));
    }

    @GetMapping("/selectGoodsList")
    @ApiOperation("查询商品列表")
    public Result selectGoodsList(Integer page,Integer limit,Long tearoomId,String goodsName,Integer status,Integer goodsCategory){
        return goodsService.selectGoodsList(page, limit, tearoomId, goodsName, status,1,goodsCategory);
    }

    @PostMapping("/bindInnerCode")
    @ApiOperation("绑定设备组")
    public Result bindInnerCode(@RequestBody Goods goods){
        JSONObject jsonObject = roomService.saveGroupBind(goods.getInnerCode(), "1");
        String code = jsonObject.getString("code");
        if(!"200".equals(code)){
            return Result.error(jsonObject.getString("msg"));
        }
        goodsService.updateById(goods);
        return Result.success();
    }

    @PostMapping("/closeInnerCode")
    @ApiOperation("解绑设备组")
    public Result closeInnerCode(@RequestBody Goods goods){
        JSONObject jsonObject = roomService.saveGroupBind(goods.getInnerCode(), "0");
        String code = jsonObject.getString("code");
        if(!"200".equals(code)){
            return Result.error(jsonObject.getString("msg"));
        }
        goodsService.update(null, Wrappers.<Goods>lambdaUpdate()
                .set(Goods::getInnerCode,null)
                .eq(Goods::getGoodsId,goods.getGoodsId()));
        return Result.success();
    }

    @PostMapping("/openOrCloseRoom")
    @ApiOperation("一键开启或关闭设备")
    public Result openOrCloseRoom(Long tearoomId,Integer type){
        SysUserEntity user = getUser();
        log.info("调用一键开启或关闭设备管理员："+user.getUsername());
        List<Goods> goodsList = goodsService.list(new QueryWrapper<Goods>().eq("tearoom_id", tearoomId).eq("status", 1));
        for (Goods goods:goodsList){
            List<Room> rooms = roomService.list(new QueryWrapper<Room>().eq("goods_id", goods.getGoodsId()));
            for(Room room:rooms){
                try {
                    roomService.issueInstruct(room.getRoomId(),type);
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("一键开启或关闭设备报错："+e.getMessage(),e);
                }
            }
        }
        return Result.success();
    }

}
