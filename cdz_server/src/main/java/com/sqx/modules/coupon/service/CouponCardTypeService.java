package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.CouponCardType;

public interface CouponCardTypeService extends IService<CouponCardType> {

    Result selectCouponCardTypeList(Integer page, Integer limit, String couponTypeName, Integer status);

}
