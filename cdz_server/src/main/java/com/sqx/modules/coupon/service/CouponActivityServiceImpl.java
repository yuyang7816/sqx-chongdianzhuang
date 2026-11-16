package com.sqx.modules.coupon.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.coupon.entity.CouponActivity;
import com.sqx.modules.coupon.respository.CouponActivityDao;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.helpCenter.dao.HelpClassifyDao;
import com.sqx.modules.helpCenter.entity.HelpClassify;
import com.sqx.modules.helpCenter.service.HelpClassifyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CouponActivityServiceImpl extends ServiceImpl<CouponActivityDao, CouponActivity> implements CouponActivityService {

    @Autowired
    private SelfCouponIssueJpaRepository selfCouponIssueJpaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SelfCouponUserService selfCouponUserService;

    @Scheduled(cron = "0 */1 * * * ?")
    public void sendCoupon(){
        //判断当前是否有需要执行的活动
        List<CouponActivity> couponActivities = baseMapper.selectCouponActivity();
        for (CouponActivity couponActivity:couponActivities){
            couponActivity.setIsOk(1);
            baseMapper.updateById(couponActivity);
        }
        LocalTime currentTime = LocalTime.now();
        if (currentTime.getHour() == 0 && currentTime.getMinute() == 0) {
            System.out.println("当前时间是零时零分");
            return;
        }
        // 获取当前日期
        LocalDate today = LocalDate.now();
        // 获取当前日期是星期几
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        // 判断是否是周一
        boolean isMonday = dayOfWeek == DayOfWeek.MONDAY;
        // 判断是否为本月的 1 号
        boolean isFirstDay = today.getDayOfMonth() == 1;
        // 获取当前日期的月份和日
        int month = today.getMonthValue();
        int day = today.getDayOfMonth();

        // 判断是否是季度的开始月份且是 1 号
        boolean isQuarterFirstDay = (month == 1 || month == 4 || month == 7 || month == 10) && day == 1;
        // 判断是否是每年的 1 月 1 号
        boolean isNewYearDay = today.getMonthValue() == 1 && today.getDayOfMonth() == 1;

        List<UserEntity> userEntityList = userService.list(new QueryWrapper<UserEntity>().eq("status", 1));
        for (CouponActivity couponActivity:couponActivities){
            boolean flag=false;
            //触发频率 1每天 2每周 3每月 4每季度 5每年
            if(couponActivity.getPrivilegeType()==1){
                flag=true;
            }else if(couponActivity.getPrivilegeType()==2){
                if(isMonday){
                    flag=true;
                }
            }else if(couponActivity.getPrivilegeType()==3){
                if(isFirstDay){
                    flag=true;
                }
            }else if(couponActivity.getPrivilegeType()==4){
                if(isQuarterFirstDay){
                    flag=true;
                }
            }else if(couponActivity.getPrivilegeType()==5){
                if(isNewYearDay){
                    flag=true;
                }
            }
            if(flag){
                try {
                    for (UserEntity user:userEntityList){
                        for (String couponId:couponActivity.getCouponIds().split(",")){
                            try {
                                selfCouponUserService.sendCoupon(couponId,user.getUserId());
                            }catch (Exception e){
                                e.printStackTrace();
                                log.error("活动赠送优惠券，赠送用户出错："+e.getMessage(),e);
                            }
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("活动赠送优惠券，活动出错："+e.getMessage(),e);
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateVipIsOk(){
        baseMapper.update(null, Wrappers.<CouponActivity>lambdaUpdate()
                .set(CouponActivity::getIsOk,null)
                .eq(CouponActivity::getIsOk,1));
    }


}







