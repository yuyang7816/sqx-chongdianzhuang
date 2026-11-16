package com.sqx.modules.orders.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.entity.OrdersVideo;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.orders.service.OrdersVideoService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.TearoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@RestController
@Api(value = "订单视频", tags = {"订单视频"})
@RequestMapping(value = "/app/ordersVideo")
public class AppOrdersVideoController {

    @Autowired
    private OrdersVideoService ordersVideoService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private OrdersDao ordersDao;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrdersService ordersService;

    @PostMapping("/insertOrdersVideo")
    @ApiOperation("接受订单视频")
    public Result insertOrdersVideo(@RequestBody OrdersVideo ordersVideo, HttpServletRequest request){
        log.info("发起了请求请求记录"+ JSONObject.toJSONString(ordersVideo));
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return Result.error("非法操作");
        }
        String value = commonInfoService.findOne(885).getValue();
        if(!value.equals(token)){
            return Result.error("无效token");
        }
        ordersVideo.setCreateTime(DateUtils.format(new Date()));
        Tearoom tearoom = tearoomService.getOne(new QueryWrapper<Tearoom>().eq("camera_id", ordersVideo.getTearoomCameraId()));
        if(tearoom!=null){
            ordersVideo.setTearoomId(tearoom.getTearoomId());
        }
        Goods goods = goodsService.getOne(new QueryWrapper<Goods>().eq("camera_id", ordersVideo.getGoodsCameraId()));
        if(goods!=null){
            ordersVideo.setGoodsId(goods.getGoodsId());
        }
        ordersVideo.setVideoUrl(commonInfoService.findOne(915).getValue()+ordersVideo.getVideoUrl());
        //根据订单的时间 去查找订单
        Orders orders = ordersDao.selectOrdersByVideoTime(ordersVideo.getTearoomId(), ordersVideo.getGoodsId(), ordersVideo.getVideoTime());
        if(orders!=null){
            ordersVideo.setOrdersId(orders.getOrdersId());
        }
        ordersVideo.setClassify(1);
        ordersVideoService.save(ordersVideo);
        if(StringUtils.isNotBlank(ordersVideo.getMontageVideoUrl())){
            for (String videoUrl:ordersVideo.getMontageVideoUrl().split(",")){
                OrdersVideo ordersVideo1=new OrdersVideo();
                ordersVideo1.setClassify(2);
                ordersVideo1.setVideoUrl(videoUrl);
                ordersVideo1.setOrdersId(ordersVideo.getOrdersId());
                ordersVideo1.setTearoomId(ordersVideo.getTearoomId());
                ordersVideo1.setGoodsId(ordersVideo.getGoodsId());
                ordersVideo1.setCreateTime(ordersVideo.getCreateTime());
                ordersVideo1.setVideoTime(ordersVideo.getVideoTime());
                ordersVideoService.save(ordersVideo1);
            }
        }


        return Result.success();
    }


    @GetMapping("/selectOrdersVideoList")
    @ApiOperation("查询视频")
    public Result selectOrdersVideoList(Integer page,Integer limit,Long ordersId,Long tearoomId,Long goodsId,String goodsName,String userName,String phone,
                                        String ordersNo,String tearoomName,Long userId,Integer classify,Long shopId){
        return Result.success().put("data",ordersVideoService.selectOrdersVideoList(page, limit, ordersId, tearoomId, goodsId, goodsName,
                userName, phone, ordersNo, tearoomName,userId,classify,shopId));
    }

    @GetMapping("/selectOrdersList")
    @ApiOperation("查询订单列表")
    public Result selectOrdersLists(Integer page,Integer limit,Long userId,String userName,String phone,
                                    String ordersNo,String goodsName,Integer status,Long tearoomId,String startTime,String endTime,
                                    Integer isShop,String dateTime,String cameraId,Integer goodsCategory,Long openUserId,Integer ordersType,
                                    HttpServletRequest request){
        log.info("请求查询订单");
        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            return Result.error("非法操作");
        }
        String value = commonInfoService.findOne(885).getValue();
        if(!value.equals(token)){
            return Result.error("无效token");
        }
        return ordersService.selectOrdersList(page, limit, userId, userName, phone, ordersNo, goodsName,
                status,tearoomId,startTime,endTime,1,null,null,isShop,dateTime,cameraId,goodsCategory,
                null,null,openUserId,ordersType);
    }

}
