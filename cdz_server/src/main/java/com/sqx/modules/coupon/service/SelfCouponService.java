package com.sqx.modules.coupon.service;

import com.sqx.modules.coupon.utils.Result;
import com.sqx.modules.coupon.entity.SelfCoupon;

public interface SelfCouponService {
    //列表
    Result findAll(Integer page, Integer size,Integer type,String couponName,String status);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    //添加
    Result saveBody(SelfCoupon entity);

    //修改
    Result updateBody(SelfCoupon entity);

}
