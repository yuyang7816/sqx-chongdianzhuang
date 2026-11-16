package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.dao.CouponCardDao;
import com.sqx.modules.coupon.dao.CouponCardTypeDao;
import com.sqx.modules.coupon.dao.CouponDao;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.entity.CouponCardType;
import com.sqx.modules.coupon.service.CouponCardTypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CouponCardTypeServiceImpl extends ServiceImpl<CouponCardTypeDao, CouponCardType> implements CouponCardTypeService {

    @Autowired
    private CouponCardDao couponCardDao;
    @Autowired
    private CouponDao couponDao;

    @Override
    public Result selectCouponCardTypeList(Integer page, Integer limit, String couponTypeName, Integer status){
        IPage<CouponCardType> couponCardTypePage = baseMapper.selectPage(new Page<>(page, limit), new QueryWrapper<CouponCardType>()
                .eq(StringUtils.isNotBlank(couponTypeName), "coupon_type_name", couponTypeName)
                .eq(status != null && status != 0, "status", status).orderByAsc("create_time"));
        List<CouponCardType> records = couponCardTypePage.getRecords();
        for(CouponCardType couponCardType:records){
            String[] split = couponCardType.getCouponIds().split(",");
            List<Coupon> couponList=new ArrayList<>();
            for(String id:split){
                Coupon coupon = couponDao.selectById(Long.parseLong(id));
                couponList.add(coupon);
            }
            couponCardType.setCouponList(couponList);
        }
        return Result.success().put("data",new PageUtils(couponCardTypePage));
    }



}
