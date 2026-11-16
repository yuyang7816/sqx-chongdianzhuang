package com.sqx.modules.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.coupon.entity.CouponUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CouponUserDao extends BaseMapper<CouponUser> {

    IPage<CouponUser> selectCouponList(Page<CouponUser> pages, @Param("userId") Long userId,
                                         @Param("minMoney") BigDecimal minMoney, @Param("format") String format,@Param("shopId") Long shopId);

    IPage<CouponUser> CouponList(Page<CouponUser> pages,@Param("userId") Long userId,@Param("status") Integer status);

    void updateExpiration(@Param("date") String date);

    IPage<CouponUser> selectCouponByUserId(Page<CouponUser> pages, @Param("userId") Long userId,@Param("status") Integer status, @Param("phone") String phone,@Param("couponName") String couponName);

    int selectCountCoupon(Long userId);

    int giveCoupon(List<String> userIdList, @Param("couponName")String couponName, @Param("couponPicture")String couponPicture,
                   @Param("createTime") String createTime, @Param("expirationTime")String expirationTime, @Param("minMoney")BigDecimal minMoney,
                   @Param("money")BigDecimal money, @Param("status")Integer status, @Param("endTime")String endTime);

}
