package com.sqx.modules.coupon.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.coupon.entity.CouponIssue;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CouponIssueDao extends BaseMapper<CouponIssue> {

    IPage<CouponIssue> selectCouponIssueListByUserId(Page<CouponIssue> page,Integer classify,Long userId);

}
