package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.CouponCard;
import com.sqx.modules.utils.excel.ExcelData;

public interface CouponCardService extends IService<CouponCard> {

    void insertCouponCard(Integer num, Long couponCardTypeId);

    CouponCard selectCouponCard(String couponCardNo);

    Result selectCouponCardList(Integer page, Integer limit, Integer status, String couponCardNo, Long couponCardTypeId, Long userId);

    ExcelData excelCouponCardList(Integer status, String couponCardNo, Long couponCardTypeId, Long userId);

    Result activateCouponCard(Long userId,String couponCardNo);

}
