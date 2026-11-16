package com.sqx.modules.coupon.service;

import com.sqx.modules.coupon.utils.Result;
import com.sqx.modules.coupon.entity.SelfCouponUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

public interface SelfCouponUserService {

    //列表
    Result findAll(Integer page, Integer size, String couponName,String nickName,Integer type,Long tearoomId,Long userId,
                   String ordersId,Integer payWay,String phone,Integer status,Integer platformType);

    Result conversionCoupon(Long userId,String key,Integer classify,Integer codeType,Long tearoomId);

    //用户端优惠券列表
    Result userList(Integer page, Integer size, Long userId, String tearoomId,String goodsId,Integer status,Integer type,BigDecimal money);

    Result shopList(Integer page, Integer size,String tearoomId);

    Result useList(Long userId,String tearoomId, String goodsId);

    Result userCoupon(Long userId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    Result payCoupon(Long userId, Long couponIssueId);

    //添加
    Result saveBody(SelfCouponUser entity);

    //修改
    Result updateBody(SelfCouponUser entity);

    Result updateStatus(SelfCouponUser entity);

    Result sendUserCoupon(String userIds,String couponIds,Integer type);

    Result sendUserCoupon(Long userId,Long couponId,Integer num);

    Result sendNewUserCoupon(Long userId,Long couponId);

    Result refundMoney(Long couponUserId);

    Result verificationCoupon(Long userId,Long tearoomId,String cdkCode);

    Integer selectCouponCountByTimeAndPlatform(String time,Integer platform);

    Result sendCoupon(String sCouponId,Long userId);

    Integer selectCouponCountByTimeAndPlatformType(String time, Integer platformType,String tearoomId);

    BigDecimal selectCouponMoneyByTimeAndPlatformType(String time, Integer platformType,String tearoomId);

    Integer selectCouponUserCount(Long userId,Long couponIssueId);

    Integer selectCouponCountByTimeAndPlatforms(String time,String tearoomId);

    BigDecimal selectCouponMoneyByTimeAndPlatforms(String time,String tearoomId);

    Integer selectCouponCountByTimeAndPlatformsPay(String time,String tearoomId);

    BigDecimal selectCouponMoneyByTimeAndPlatformsPay(String time,String tearoomId);

    Result testInsertCoupon();

}
