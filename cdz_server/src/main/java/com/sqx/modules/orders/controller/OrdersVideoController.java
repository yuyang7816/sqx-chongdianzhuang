package com.sqx.modules.orders.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.orders.entity.OrdersVideo;
import com.sqx.modules.orders.service.OrdersVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@Api(value = "订单视频", tags = {"订单视频"})
@RequestMapping(value = "/ordersVideo")
public class OrdersVideoController {

    @Autowired
    private OrdersVideoService ordersVideoService;

    @GetMapping("/selectOrdersVideoList")
    @ApiOperation("查询视频")
    public Result selectOrdersVideoList(Integer page,Integer limit,Long ordersId,Long tearoomId,Long goodsId,String goodsName,String userName,String phone,
                                        String ordersNo,String tearoomName,Long userId,Integer classify,Long shopId){
        return Result.success().put("data",ordersVideoService.selectOrdersVideoList(page, limit, ordersId, tearoomId, goodsId, goodsName,
                userName, phone, ordersNo, tearoomName,userId,classify,shopId));
    }

    @PostMapping("/updateOrdersVideo")
    @ApiOperation("修改视频")
    public Result updateOrdersVideo(@RequestBody OrdersVideo ordersVideo){
        ordersVideoService.updateById(ordersVideo);
        return Result.success();
    }

}
