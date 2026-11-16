package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.CouponIssue;

public interface CouponIssueService extends IService<CouponIssue> {

    Result selectCouponIssueList(Integer page,Integer limit,String couponIssueName,Integer classify,Integer status);

    Result selectCouponIssueListByUserId(Integer page,Integer limit,Integer classify,Long userId);

    Result insertCouponUser(Long userId, Long couponIssueId);

}
