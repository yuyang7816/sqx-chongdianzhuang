package com.sqx.modules.tearoom.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.GoodsTime;
import com.sqx.modules.tearoom.entity.Room;
import com.sqx.modules.tearoom.service.GoodsTimeService;
import com.sqx.modules.tearoom.service.RoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@Api(value = "商品特殊时段", tags = {"商品特殊时段"})
@RequestMapping(value = "/goodsTime")
public class GoodsTimeController extends AbstractController {

    @Autowired
    private GoodsTimeService goodsTimeService;

    @PostMapping("/insertGoodsTime")
    @ApiOperation("添加商品特殊时段")
    public Result insertGoodsTime(@RequestBody GoodsTime goodsTime){
        if(goodsTime.getMinutePrice()!=null){
            goodsTime.setHourPrice(goodsTime.getMinutePrice().multiply(BigDecimal.valueOf(60)));
        }
        if(goodsTime.getMemberMinutePrice()!=null){
            goodsTime.setMemberHourPrice(goodsTime.getMemberMinutePrice().multiply(BigDecimal.valueOf(60)));
        }
        goodsTime.setCreateTime(DateUtils.format(new Date()));
        goodsTimeService.save(goodsTime);
        return Result.success();
    }

    @PostMapping("/updateGoodsTime")
    @ApiOperation("查询商品特殊时段")
    public Result updateGoodsTime(@RequestBody GoodsTime goodsTime){
        if(goodsTime.getMinutePrice()!=null){
            goodsTime.setHourPrice(goodsTime.getMinutePrice().multiply(BigDecimal.valueOf(60)));
            goodsTime.setMemberHourPrice(goodsTime.getMemberMinutePrice().multiply(BigDecimal.valueOf(60)));
        }
        goodsTimeService.updateById(goodsTime);
        return Result.success();
    }

    @PostMapping("/deleteGoodsTimeById")
    @ApiOperation("删除商品特殊时段")
    public Result deleteGoodsTimeById(Long goodsTimeId){
        goodsTimeService.removeById(goodsTimeId);
        return Result.success();
    }

    @GetMapping("/selectGoodsTimeList")
    @ApiOperation("查询商品特殊时段列表")
    public Result selectGoodsTimeList(Integer page,Integer limit,Long tearoomId){
        return Result.success().put("data",goodsTimeService.page(new Page<>(page,limit),new QueryWrapper<GoodsTime>().eq("tearoom_id",tearoomId)));
    }

    

}
