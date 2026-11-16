package com.sqx.modules.coupon.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.CouponIssue;
import com.sqx.modules.coupon.service.CouponIssueService;
import com.sqx.modules.sys.controller.AbstractController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "发布优惠券", tags = {"发布优惠券"})
@RequestMapping(value = "/couponIssue")
public class CouponIssueController extends AbstractController {

    @Autowired
    private CouponIssueService couponIssueService;

    @PostMapping("/insertCouponIssue")
    @ApiOperation("添加发布优惠券")
    public Result insertCouponIssue(@RequestBody CouponIssue couponIssue){
        couponIssue.setCreateTime(DateUtils.format(new Date()));
        couponIssueService.save(couponIssue);
        return Result.success();
    }

    @PostMapping("/updateCouponIssue")
    @ApiOperation("修改发布优惠券")
    public Result updateCouponIssue(@RequestBody CouponIssue couponIssue){
        couponIssueService.updateById(couponIssue);
        return Result.success();
    }

    @PostMapping("/deleteCouponIssueById")
    @ApiOperation("删除发布优惠券")
    public Result deleteCouponIssueById(Long couponIssueId){
        couponIssueService.removeById(couponIssueId);
        return Result.success();
    }

    @GetMapping("/selectCouponIssueList")
    @ApiOperation("查询发布优惠券")
    public Result selectCouponIssueList(Integer page,Integer limit,String couponIssueName,Integer classify,Integer status){
        return couponIssueService.selectCouponIssueList(page, limit, couponIssueName, classify, status);
    }








}