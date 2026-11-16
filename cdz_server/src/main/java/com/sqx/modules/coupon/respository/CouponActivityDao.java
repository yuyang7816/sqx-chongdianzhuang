package com.sqx.modules.coupon.respository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.coupon.entity.CouponActivity;
import com.sqx.modules.helpCenter.entity.HelpClassify;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CouponActivityDao extends BaseMapper<CouponActivity> {

    List<CouponActivity> selectCouponActivity();


}
