package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.Coupon;

import java.util.List;

public interface CouponService extends IService<Coupon> {

    Result issueCoupon(Coupon coupon);

    Result deleteCoupon(Long couponId);

    Result selectAllCoupon(Integer page, Integer limit);

    Result updateCoupon(Coupon coupon);

    Result selectCouponList(Integer page, Integer limit);

    Result giveCoupon(Long couponId, List<String> userIdList, Long shopId);

}
