package com.sqx.modules.coupon.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.CouponActivity;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.coupon.service.CouponActivityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Api(value = "优惠券活动", tags = {"优惠券活动"})
@RequestMapping(value = "/couponActivity")
public class CouponActivityController {

    @Autowired
    private CouponActivityService couponActivityService;
    @Autowired
    private SelfCouponIssueJpaRepository selfCouponIssueJpaRepository;

    @PostMapping("/save")
    @ApiOperation("保存")
    public Result save(@RequestBody CouponActivity couponActivity){
        couponActivity.setCreateTime(DateUtils.format(new Date()));
        couponActivityService.save(couponActivity);
        return Result.success();
    }

    @PostMapping("/update")
    @ApiOperation("修改")
    public Result update(@RequestBody CouponActivity couponActivity){
        couponActivityService.updateById(couponActivity);
        return Result.success();
    }


    @PostMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long couponActivityId){
        couponActivityService.removeById(couponActivityId);
        return Result.success();
    }

    @GetMapping("/selectCouponActivityList")
    @ApiOperation("查询列表")
    public Result selectCouponActivityList(Integer page,Integer limit,String activityName){
        IPage<CouponActivity> couponActivityIPage = couponActivityService.page(new Page<>(page, limit), new QueryWrapper<CouponActivity>()
                .eq(StringUtils.isNotBlank(activityName), "activity_name", activityName));
        for (CouponActivity couponActivity:couponActivityIPage.getRecords()){
            List<SelfCouponIssue> selfCouponIssueList=new ArrayList<>();
            for (String couponId:couponActivity.getCouponIds().split(",")){
                SelfCouponIssue selfCouponIssue = selfCouponIssueJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                selfCouponIssueList.add(selfCouponIssue);
            }
            couponActivity.setSelfCouponIssueList(selfCouponIssueList);
        }
        return Result.success().put("data",couponActivityIPage);
    }



}
