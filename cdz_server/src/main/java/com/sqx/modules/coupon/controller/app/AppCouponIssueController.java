package com.sqx.modules.coupon.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.coupon.service.CouponIssueService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "发布优惠券", tags = {"发布优惠券"})
@RequestMapping(value = "/app/couponIssue")
public class AppCouponIssueController extends AbstractController {

    @Autowired
    private CouponIssueService couponIssueService;

    @Login
    @GetMapping("/selectCouponIssueList")
    @ApiOperation("查询发布优惠券")
    public Result selectCouponIssueList(Integer page,Integer limit,Integer classify,@RequestAttribute Long userId){
        return couponIssueService.selectCouponIssueListByUserId(page, limit, classify, userId);
    }
    
    @Login
    @PostMapping("/insertCouponUser")
    @ApiOperation("领取优惠券")
    public Result insertCouponUser(@RequestAttribute Long userId,Long couponIssueId){
        return couponIssueService.insertCouponUser(userId, couponIssueId);
    }






}