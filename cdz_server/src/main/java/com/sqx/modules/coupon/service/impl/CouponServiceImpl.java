package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.dao.CouponDao;
import com.sqx.modules.coupon.dao.CouponUserDao;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CouponServiceImpl extends ServiceImpl<CouponDao, Coupon> implements CouponService {

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CouponUserDao couponUserDao;

    @Override
    public Result selectAllCoupon(Integer page, Integer limit) {
        if(page == null || limit==null){
            List<Coupon> coupons = baseMapper.selectCouponList();
            return Result.success().put("data",coupons);
        }else{
            Page<Coupon> pages = new Page<>(page, limit);
            PageUtils pageUtils = new PageUtils(baseMapper.selectCouponPage(pages));
            return Result.success().put("data", pageUtils);
        }

    }

    @Override
    public Result issueCoupon(Coupon coupon) {
        couponDao.insert(coupon);
        return Result.success();
    }

    @Override
    public Result updateCoupon(Coupon coupon) {
        couponDao.updateById(coupon);
        return Result.success();
    }

    @Override
    public Result selectCouponList(Integer page, Integer limit) {
        Page<Coupon> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(baseMapper.selectPage(pages,null));
        return Result.success().put("data", pageUtils);
    }


    @Transactional
    @Override
    public Result giveCoupon(Long couponId, List<String> userIdList, Long shopId) {
        Coupon coupon = couponDao.selectById(couponId);
        //计算优惠券过期时间
        Integer endDate = coupon.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, endDate);
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String expirationTime = new SimpleDateFormat("yyyy-MM-dd").format(date);
        couponUserDao.giveCoupon(userIdList, coupon.getCouponName(), coupon.getCouponPicture(), createTime, expirationTime, coupon.getMinMoney(),
                coupon.getMoney(), 0,coupon.getEndDate().toString());
        return Result.success();
    }

    @Override
    public Result deleteCoupon(Long couponId) {
        couponDao.deleteById(couponId);
        return Result.success();
    }


}
