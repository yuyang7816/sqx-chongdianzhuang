package com.sqx.modules.coupon.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.service.CouponService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@Api(value = "管理端-优惠券", tags = {"管理端-优惠券"})
@RequestMapping(value = "/admin/coupon")
public class CouponController extends AbstractController {

    @Autowired
    private CouponService couponService;


    @ApiOperation("查看发布的优惠券")
    @GetMapping(value = "/seleteAllCoupon")
    public Result selectAllCoupon(Integer page, Integer limit){
        return couponService.selectAllCoupon(page, limit);
    }

    @ApiOperation("发布优惠券")
    @PostMapping(value = "/issueCoupon")
    public Result issueCoupon(@RequestBody Coupon coupon){
        return couponService.issueCoupon(coupon);
    }

    @ApiOperation("删除发布的优惠券")
    @PostMapping(value = "/deleteCoupon")
    public Result deleteCoupon(Long couponId){
        return couponService.deleteCoupon(couponId);
    }

    @ApiOperation("修改优惠券信息")
    @PostMapping(value = "/updateCoupon")
    public Result updateCoupon(@RequestBody Coupon coupon){
        return couponService.updateCoupon(coupon);
    }

    @ApiOperation("管理端赠送用户优惠券")
    @GetMapping(value = "/giveCoupon")
    public Result giveCoupon(Long couponId, String userIds, Long shopId){
        String[] userId = userIds.split(",");
        List<String> userIdList = Arrays.asList(userId);
        return couponService.giveCoupon(couponId, userIdList, shopId);
    }

}