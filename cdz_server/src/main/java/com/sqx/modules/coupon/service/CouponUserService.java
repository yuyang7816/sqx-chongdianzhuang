package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.coupon.entity.CouponUser;

import java.math.BigDecimal;

public interface CouponUserService extends IService<CouponUser> {

    Result selectCouponList(Long userId, Integer page, Integer limit, BigDecimal minMoney,Long shopId);

    Result couponList(Long userId, Integer page, Integer limit, Integer status);

    void updateExpiration();

    Result buyCoupon(Long userId, Long couponId);

    Result selectCouponByUserId(Integer page, Integer limit, Long userId, Integer status, String phone,String couponName);

    Result insertNewUserCoupon(UserEntity userEntity);

}
