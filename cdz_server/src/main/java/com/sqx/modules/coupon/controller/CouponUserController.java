package com.sqx.modules.coupon.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.CouponUser;
import com.sqx.modules.coupon.service.CouponUserService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "用户优惠券", tags = {"用户优惠券"})
@RequestMapping(value = "/couponUser")
public class CouponUserController extends AbstractController {

    @Autowired
    private CouponUserService couponUserService;

    @GetMapping("/selectCouponUserList")
    @ApiOperation("查询用户优惠券")
    public Result selectCouponUserList(Integer page,Integer limit,Long userId,Integer status,String phone,String couponName){
        return couponUserService.selectCouponByUserId(page, limit, userId, status, phone,couponName);
    }

    @PostMapping("/loseEfficacyCoupon")
    @ApiOperation("设置优惠券为失效")
    public Result loseEfficacyCoupon(Long id){
        CouponUser couponUser = couponUserService.getById(id);
        couponUser.setStatus(2);
        couponUserService.updateById(couponUser);
        return Result.success();
    }

    @PostMapping("/deleteCoupon")
    @ApiOperation("删除优惠券")
    public Result deleteCoupon(Long id){
        couponUserService.removeById(id);
        return Result.success();
    }


}