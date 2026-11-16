package com.sqx.modules.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.coupon.entity.Coupon;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponDao extends BaseMapper<Coupon> {

    IPage<Coupon> selectCouponPage(Page<Coupon> pages);

    List<Coupon> selectCouponList();

}
