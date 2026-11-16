package com.sqx.modules.coupon.service;


import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.utils.Result;

public interface SelfCouponIssueService {
    //列表
    Result findAll(Integer page, Integer size, String goodsId,String tearoomId, String type,String couponName,String tearoomName);

    //用户端
    Result useList(Integer page,Integer size,String goodsId,Integer type,Integer couponType,Long tearoomId);

    //查询
    Result findOne(Long id);

    //删除
    Result delete(Long id);

    Result selectTearoomListByCoupon(Long couponIssueId);

    //添加
    Result saveBody(SelfCouponIssue entity);

    //修改
    Result updateBody(SelfCouponIssue entity);

}
