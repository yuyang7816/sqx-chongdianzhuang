package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.dao.CouponDao;
import com.sqx.modules.coupon.dao.CouponUserDao;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.entity.CouponUser;
import com.sqx.modules.coupon.service.CouponUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CouponUserServiceImpl extends ServiceImpl<CouponUserDao, CouponUser> implements CouponUserService {

    @Autowired
    private CouponUserDao CouponUserDao;
    @Autowired
    private CouponDao CouponDao;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;

    @Override
    public Result selectCouponList(Long userId, Integer page, Integer limit, BigDecimal minMoney,Long shopId) {
        Page<CouponUser> pages=new Page<>(page,limit);
        String format = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        PageUtils pageUtils = new PageUtils(CouponUserDao.selectCouponList(pages, userId, minMoney, format,shopId));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result couponList(Long userId, Integer page, Integer limit, Integer status) {
        Page<CouponUser> pages=new Page<>(page,limit);
        PageUtils pageUtils = new PageUtils(CouponUserDao.CouponList(pages, userId, status));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public void updateExpiration() {
        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        CouponUserDao.updateExpiration(date);
    }

    @Transactional
    @Override
    public Result buyCoupon(Long userId, Long couponId) {

        Coupon coupon = CouponDao.selectById(couponId);
        //将优惠券添加到用户优惠券仓库
        CouponUser couponUser = new CouponUser();
        couponUser.setUserId(userId);
        couponUser.setCouponName(coupon.getCouponName());
        couponUser.setCouponPicture(coupon.getCouponPicture());
        String format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        couponUser.setCreateTime(format);
        //计算优惠券过期时间
        Integer endDate = coupon.getEndDate();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, endDate);
        calendar.add(Calendar.DATE, 1);
        Date date = calendar.getTime();
        String format1 = new SimpleDateFormat("yyyy-MM-dd").format(date);
        couponUser.setExpirationTime(format1);
        couponUser.setMinMoney(coupon.getMinMoney());
        couponUser.setMoney(coupon.getMoney());
        couponUser.setStatus(0);
        CouponUserDao.insert(couponUser);

        return Result.success();
    }

    @Override
    public Result selectCouponByUserId(Integer page, Integer limit, Long userId, Integer status, String phone,String couponName) {
        Page<CouponUser> pages = new Page<>(page, limit);
        PageUtils pageUtils = new PageUtils(CouponUserDao.selectCouponByUserId(pages, userId, status, phone,couponName));
        return Result.success().put("data", pageUtils);
    }

    @Override
    public Result insertNewUserCoupon(UserEntity userEntity){
        if(userEntity.getIsCoupon()!=null && userEntity.getIsCoupon()==1){
            return Result.error("您已经领取过新人优惠券了！");
        }
        Long userId=userEntity.getUserId();
        String value = commonInfoService.findOne(281).getValue();
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<Coupon> couponList=new ArrayList<>();
        for(String couponId:value.split(",")){
            Coupon coupon = CouponDao.selectById(Long.parseLong(couponId));
            Integer endDate = coupon.getEndDate();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, endDate);
            calendar.add(Calendar.DATE, 1);
            Date date = calendar.getTime();
            String expirationTime = simpleDateFormat.format(date);
            CouponUser couponUser=new CouponUser();
            couponUser.setUserId(userId);
            couponUser.setCouponName(coupon.getCouponName());
            couponUser.setCouponPicture(coupon.getCouponPicture());
            couponUser.setCreateTime(createTime);
            couponUser.setExpirationTime(expirationTime);
            couponUser.setMinMoney(coupon.getMinMoney());
            couponUser.setMoney(coupon.getMoney());
            couponUser.setStatus(0);
            baseMapper.insert(couponUser);
            couponList.add(coupon);
        }
        userEntity.setIsCoupon(1);
        userService.updateById(userEntity);
        return Result.success().put("data",couponList);
    }


}
