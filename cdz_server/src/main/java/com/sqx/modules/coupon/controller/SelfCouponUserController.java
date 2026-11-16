package com.sqx.modules.coupon.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.coupon.utils.Result;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.service.SelfCouponUserService;
import com.sqx.modules.coupon.utils.ResultUtil;
import com.sqx.modules.orders.entity.Orders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@Api(value="自营商城优惠券领取",tags={"自营商城优惠券领取"})
@RequestMapping(value = "/selfCouponUser")
public class SelfCouponUserController {
    @Autowired
    private SelfCouponUserService service;

    @PostMapping("/sendUserCoupon")
    @ApiOperation("赠送用户优惠券")
    public Result sendUserCoupon(String userIds,String couponIds,Integer type){
        return service.sendUserCoupon(userIds, couponIds,type);
    }

    @PostMapping("/adminConversionCoupon")
    @ApiOperation("兑换优惠券")
    public Result adminConversionCoupon( Long userId,String key,Long tearoomId){
        return service.conversionCoupon(userId, key, null,1,tearoomId);
    }
    
    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size, String couponName,String nickName,Integer type,Long tearoomId,Long userId,
                          String ordersId,Integer payWay,String phone,Integer status,Integer platformType) {
        return service.findAll(page, size, couponName,nickName,type,tearoomId,userId,ordersId,payWay,phone,status,platformType);
    }
    
    @GetMapping("/userList")
    @ApiOperation("用户端用户优惠券列表")
    public Result userList(Integer page, Integer size,Long userId, String tearoomId,String goodsId,Integer status,Integer type,BigDecimal money) {
        return service.userList(page, size, userId,tearoomId, goodsId,status,type,money);
    }
    
    @GetMapping("/useList")
    @ApiOperation("优惠券使用列表")
    public Result useList(Long userId,String tearoomId, String goodsId) {
        return service.useList(userId, tearoomId, goodsId);
    }
    
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }
    
    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCouponUser entity) {
        return service.saveBody(entity);
    }
    
    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCouponUser entity) {
        return service.updateBody(entity);
    }

    @PostMapping("/updateStatus")
    @ApiOperation("修改")
    public Result updateStatus(@RequestBody SelfCouponUser entity) {
        return service.updateStatus(entity);
    }
    
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @PostMapping("/refundMoney")
    @ApiOperation("团购劵退款")
    public Result refundMoney(Long couponUserId){
        return service.refundMoney(couponUserId);
    }

    @PostMapping("/verificationCoupon")
    @ApiOperation("核销劵")
    public Result verificationCoupon(Long userId,Long tearoomId,String cdkCode){
        return service.verificationCoupon(userId,tearoomId,cdkCode);
    }

    @GetMapping("/selectCouponData")
    @ApiOperation("优惠券统计")
    public Result selectCouponData(Integer type,String time){
        if(type==2){
            time=time.substring(0,7);
        }else if(type==3){
            time=time.substring(0,4);
        }
        //1新用户领取 2邀请新用户 3邀请用户下单 4支付购买 5积分兑换 6商家赠送
        Integer xyhCount = service.selectCouponCountByTimeAndPlatform(time, 1);
        Integer yqxhCount = service.selectCouponCountByTimeAndPlatform(time, 2);
        Integer yqxdCount = service.selectCouponCountByTimeAndPlatform(time, 3);
        Integer zfgmCount = service.selectCouponCountByTimeAndPlatform(time, 4);
        Integer jfdhCount = service.selectCouponCountByTimeAndPlatform(time, 5);
        Integer sjzsCount = service.selectCouponCountByTimeAndPlatform(time, 6);
        Map<String,Integer> result=new HashMap<>();
        result.put("xyhCount",xyhCount);
        result.put("yqxhCount",yqxhCount);
        result.put("yqxdCount",yqxdCount);
        result.put("zfgmCount",zfgmCount);
        result.put("jfdhCount",jfdhCount);
        result.put("sjzsCount",sjzsCount);
        return ResultUtil.success(result);
    }

    @PostMapping("/sendNewUserCoupon")
    @ApiOperation("赠送用户优惠券")
    public Result sendNewUserCoupon(Long userId,Long couponId){
        return service.sendNewUserCoupon(userId, couponId);
    }


    @GetMapping("/selectTearoomCount")
    @ApiOperation("美团抖音 优惠券统计")
    public com.sqx.common.utils.Result selectTearoomCount(Integer flag, String time,String tearoomId){
        String dateTime=null;
        if(1==flag){
            dateTime=time;
        }else if(2==flag){
            dateTime=time.substring(0,7);
        }else{
            dateTime=time.substring(0,4);;
        }
        Integer mtCount = service.selectCouponCountByTimeAndPlatformType(dateTime, 1,tearoomId);
        Integer dyCount = service.selectCouponCountByTimeAndPlatformType(dateTime, 2,tearoomId);
        Integer ksCount = service.selectCouponCountByTimeAndPlatformType(dateTime, 3,tearoomId);
        Integer zsCount = service.selectCouponCountByTimeAndPlatforms(dateTime, tearoomId);
        Integer payCount = service.selectCouponCountByTimeAndPlatformsPay(dateTime, tearoomId);

        BigDecimal mtMoney = service.selectCouponMoneyByTimeAndPlatformType(dateTime, 1,tearoomId);
        BigDecimal dyMoney = service.selectCouponMoneyByTimeAndPlatformType(dateTime, 2,tearoomId);
        BigDecimal ksMoney = service.selectCouponMoneyByTimeAndPlatformType(dateTime, 3,tearoomId);
        BigDecimal zsMoney = service.selectCouponMoneyByTimeAndPlatforms(dateTime, tearoomId);
        BigDecimal payMoney = service.selectCouponMoneyByTimeAndPlatformsPay(dateTime, tearoomId);

        Map<String,Object> result=new HashMap<>();
        result.put("mtCount", mtCount);
        result.put("dyCount", dyCount);
        result.put("ksCount", ksCount);
        result.put("mtMoney", mtMoney);
        result.put("dyMoney", dyMoney);
        result.put("ksMoney", ksMoney);
        result.put("zsCount", zsCount);
        result.put("zsMoney", zsMoney);
        result.put("payCount", payCount);
        result.put("payMoney", payMoney);
        return com.sqx.common.utils.Result.success().put("data",result);
    }

}
