package com.sqx.modules.coupon.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.coupon.service.CouponService;
import com.sqx.modules.coupon.service.CouponUserService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@Api(value = "用户端-优惠券", tags = {"用户端-优惠券"})
@RequestMapping(value = "/app/coupon")
public class AppCouponUserController extends AbstractController {

    @Autowired
    private CouponUserService couponUserService;
    @Autowired
    private CouponService couponService;

    @Login
    @ApiOperation("使用时查看所有优惠券")
    @GetMapping(value = "/selectUserCouponList")
    public Result selectCouponList(@RequestAttribute Long userId, Integer page, Integer limit, BigDecimal minMoney,Long shopId){
        return couponUserService.selectCouponList(userId, page, limit, minMoney,shopId);
    }


    @Login
    @GetMapping("/selectCouponUserList")
    @ApiOperation("查询用户优惠券")
    public Result selectCouponUserList(Integer page,Integer limit,@RequestAttribute Long userId,Integer status,String phone){
        return couponUserService.selectCouponByUserId(page, limit, userId, status, phone,null);
    }

    //将所有超过失效时间的优惠券改为失效状态
    @Scheduled(cron = "0 */1 * * * ?", zone = "Asia/Shanghai")
    public void couponEnd(){
        couponUserService.updateExpiration();
    }

    @Login
    @GetMapping("/insertNewUserCoupon")
    @ApiOperation("领取新人优惠券")
    public Result insertNewUserCoupon(@LoginUser UserEntity userEntity){
        return couponUserService.insertNewUserCoupon(userEntity);
    }

}