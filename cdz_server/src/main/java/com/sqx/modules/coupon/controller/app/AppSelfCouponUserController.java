package com.sqx.modules.coupon.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.sqx.modules.coupon.service.SelfCouponUserService;
import com.sqx.modules.coupon.utils.Result;
import com.sqx.modules.coupon.utils.ResultUtil;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@Api(value="自营商城优惠券领取",tags={"自营商城优惠券领取"})
@RequestMapping(value = "/app/selfCouponUser")
public class AppSelfCouponUserController {

    @Autowired
    private SelfCouponUserService service;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private SelfCouponUserJpaRepository jpaRepository;


    @PostMapping("/updateErrorMsg")
    @ApiOperation("去掉错误内容")
    public Result updateErrorMsg(Long couponUserId) {
        SelfCouponUser selfCouponUser = jpaRepository.findById(couponUserId).orElse(null);
        selfCouponUser.setErrorMsg(null);
        return service.updateBody(selfCouponUser);
    }

    @PostMapping("/sendUserCoupon")
    @ApiOperation("赠送用户优惠券")
    public Result sendUserCoupon(Long userIds,Long couponIds,Integer num){
        return service.sendUserCoupon(userIds, couponIds, 1);
    }

    @Login
    @PostMapping("/userCoupon")
    @ApiOperation("领取新人优惠券")
    public Result userCoupon(@RequestAttribute Long userId){
        return service.userCoupon(userId);
    }

    @Login
    @PostMapping("/conversionCoupon")
    @ApiOperation("兑换优惠券")
    public Result conversionCoupon(@RequestAttribute Long userId,String key,Integer classify,Integer codeType,Long tearoomId){
        return service.conversionCoupon(userId, key, classify,codeType,tearoomId);
    }
    
    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size, String couponName,Integer type,Long tearoomId,
                          Long userId,String ordersId,Integer payWay,String phone,Integer status,Integer platformType) {
        return service.findAll(page, size, couponName,null,type,tearoomId,userId,ordersId,payWay,phone,status,platformType);
    }

    @Login
    @GetMapping("/userList")
    @ApiOperation("用户端用户优惠券列表")
    public Result userList(Integer page, Integer size,@RequestAttribute  Long userId, String tearoomId,String goodsId,Integer status,Integer type,BigDecimal money) {
        return service.userList(page, size, userId, tearoomId,goodsId,status,type,money);
    }

    @Login
    @GetMapping("/userLists")
    @ApiOperation("用户端用户优惠券列表")
    public Result userLists(Integer page, Integer size,Long userId, String tearoomId,String goodsId,Integer status,Integer type,BigDecimal money) {
        return service.userList(page, size, userId, tearoomId,goodsId,status,type,money);
    }


    @Login
    @GetMapping("/shopList")
    @ApiOperation("商家核销列表端")
    public Result shopList(Integer page, Integer size,String tearoomId) {
        return service.shopList(page, size,tearoomId);
    }

    @Login
    @GetMapping("/useList")
    @ApiOperation("优惠券使用列表")
    public Result useList(@RequestAttribute Long userId,String tearoomId, String goodsId) {
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


    
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

    @Login
    @PostMapping("/payCoupon")
    @ApiOperation("零钱支付购买优惠券")
    public Result payCoupon(@RequestAttribute Long userId,Long couponIssueId){
        return service.payCoupon(userId, couponIssueId);
    }

    @Login
    @PostMapping("/verificationCoupon")
    @ApiOperation("核销劵")
    public Result verificationCoupon(@RequestAttribute Long userId,Long tearoomId,String cdkCode){
        return service.verificationCoupon(userId,tearoomId,cdkCode);
    }

    @Login
    @PostMapping("/sendNewUserCoupon")
    @ApiOperation("赠送用户优惠券")
    public Result sendNewUserCoupon(@RequestAttribute Long userId,Long couponId){
        return service.sendNewUserCoupon(userId, couponId);
    }

    @Login
    @GetMapping("/selectSendCouponUserByUserId")
    @ApiOperation("查询用户领取优惠券信息")
    public com.sqx.common.utils.Result selectSendCouponUserByUserId(@RequestAttribute Long userId){
        Long couponIssueId = Long.parseLong(commonInfoService.findOne(1053).getValue());
        Integer bossCount = service.selectCouponUserCount(userId, couponIssueId);
        couponIssueId = Long.parseLong(commonInfoService.findOne(849).getValue());
        Integer yaoCount = service.selectCouponUserCount(userId, couponIssueId);
        Map<String,Integer> result=new HashMap<>();
        result.put("bossCount",bossCount);
        result.put("yaoCount",yaoCount);
        return com.sqx.common.utils.Result.success().put("data",result);
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

    @GetMapping("/testInsertCoupon")
    public Result testInsertCoupon(){
        return service.testInsertCoupon();
    }

}
