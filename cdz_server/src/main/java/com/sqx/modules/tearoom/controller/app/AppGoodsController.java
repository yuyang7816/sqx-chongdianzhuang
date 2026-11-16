package com.sqx.modules.tearoom.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Room;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.utils.HttpClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RestController
@Api(value = "商品信息", tags = {"商品信息"})
@RequestMapping(value = "/app/goods")
public class AppGoodsController extends AbstractController {

    @Autowired
    private GoodsService goodsService;

    @GetMapping("/selectGoodsById")
    @ApiOperation("查询商品详情")
    public Result selectGoodsById(Long goodsId){
        return Result.success().put("data",goodsService.selectGoodsById(goodsId));
    }

    @GetMapping("/selectGoodsList")
    @ApiOperation("查询商品列表")
    public Result selectGoodsList(Integer page,Integer limit,Long tearoomId,String goodsName,Integer status,Integer goodsCategory){
        return goodsService.selectGoodsList(page, limit, tearoomId, goodsName, status,null,goodsCategory);
    }

    @Login
    @PostMapping("/updateGoods")
    @ApiOperation("修改商品信息")
    public Result updateGoods(@RequestBody Goods goods){
        goodsService.updateById(goods);
        return Result.success();
    }





}
