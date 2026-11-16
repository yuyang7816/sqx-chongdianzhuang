package com.sqx.modules.tearoom.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.GoodsTime;
import com.sqx.modules.tearoom.service.GoodsTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@Api(value = "商品特殊时段", tags = {"商品特殊时段"})
@RequestMapping(value = "/app/goodsTime")
public class AppGoodsTimeController extends AbstractController {

    @Autowired
    private GoodsTimeService goodsTimeService;

    @GetMapping("/selectGoodsTimeList")
    @ApiOperation("查询商品特殊时段列表")
    public Result selectGoodsTimeList(Long tearoomId){
        return Result.success().put("data",goodsTimeService.list(new QueryWrapper<GoodsTime>().eq("tearoom_id",tearoomId)));
    }

    @GetMapping("/selectGoodsTimeByTime")
    @ApiOperation("查询当前时间是否为特殊时间")
    public Result selectGoodsTimeByTime(Long tearoomId){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        return Result.success().put("data",goodsTimeService.selectGoodsTimeByTime(LocalTime.now().format(formatter),tearoomId));
    }

}
