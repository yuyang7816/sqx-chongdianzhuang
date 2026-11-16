package com.sqx.modules.coupon.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.coupon.service.CouponCardService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "卡密优惠券", tags = {"卡密优惠券"})
@RequestMapping(value = "/app/couponCard")
public class AppCouponCardController extends AbstractController {

    @Autowired
    private CouponCardService couponCardService;


    @Login
    @GetMapping("/selectCouponCardList")
    @ApiOperation("查询卡密列表")
    public Result selectCouponCardList(Integer page,Integer limit,Integer status,String couponCardNo,Long couponCardTypeId,@RequestAttribute Long userId){
        return couponCardService.selectCouponCardList(page, limit, status, couponCardNo, couponCardTypeId, userId);
    }

    @Login
    @PostMapping("/activateCouponCard")
    @ApiOperation("激活卡密")
    public Result activateCouponCard(@RequestAttribute Long userId,String couponCardNo){
        return couponCardService.activateCouponCard(userId, couponCardNo);
    }





}