package com.sqx.modules.integral.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.entity.SelfCoupon;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.sqx.modules.coupon.service.SelfCouponIssueService;
import com.sqx.modules.coupon.utils.DateUtil;
import com.sqx.modules.integral.dao.UserIntegralDetailsDao;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserIntegralDetailsServiceImpl extends ServiceImpl<UserIntegralDetailsDao, UserIntegralDetails> implements UserIntegralDetailsService {

    @Autowired
    private UserIntegralDetailsDao userIntegralDetailsDao;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private SelfCouponIssueJpaRepository selfCouponIssueJpaRepository;
    @Autowired
    private SelfCouponUserJpaRepository selfCouponUserJpaRepository;
    @Autowired
    private SelfCouponJpaRepository selfCouponJpaRepository;
    @Autowired
    private UserService userService;

    @Override
    public IPage selectUserIntegralDetailsByUserId(int page, int limit, Long userId) {

        IPage<UserIntegralDetails> page1 = userIntegralDetailsDao.selectPage(new Page<>(page, limit), new QueryWrapper<UserIntegralDetails>().eq(userId != null, "user_id", userId).orderByDesc("create_time"));

        return page1;
    }


    @Override
    public Result signIn(Long userId){
        //先判断今天是否签过到
        UserIntegralDetails userIntegralDetails1 = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, new Date());
        if(userIntegralDetails1!=null){
            return Result.error("今天已经签到过了，请明天再来吧！");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userIntegralService.selectById(userId);
        //每周初始积分
        CommonInfo one = commonInfoService.findOne(812);
        //累计签到叠加积分
        CommonInfo two = commonInfoService.findOne(813);
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -1);
        //获取当前日期时第几天  第一天则重新开始计时
        int num=0;
        int day=1;
        UserIntegralDetails userIntegralDetails2 = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        if(userIntegralDetails2==null){
            num=Integer.parseInt(one.getValue());
        }else{
            if(userIntegralDetails2.getDay()==7){
                num=Integer.parseInt(one.getValue());
            }else{
                num=userIntegralDetails2.getNum()+Integer.parseInt(two.getValue());
                day=userIntegralDetails2.getDay()+1;
            }

        }
        userIntegralService.updateIntegral(1,userId,num);
        UserIntegralDetails userIntegralDetails=new UserIntegralDetails();
        userIntegralDetails.setClassify(1);
        userIntegralDetails.setContent("签到获得:"+num+"积分");
        userIntegralDetails.setCreateTime(sdf.format(new Date()));
        userIntegralDetails.setNum(num);
        userIntegralDetails.setType(1);
        userIntegralDetails.setUserId(userId);
        userIntegralDetails.setDay(day);
        userIntegralDetailsDao.insert(userIntegralDetails);
        return Result.success("签到成功，获得"+num+"积分");
    }

    @Override
    public Result selectIntegralDay(Long userId){
        Calendar cal=Calendar.getInstance();
        UserIntegralDetails nowIntegral = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        cal.add(Calendar.DAY_OF_MONTH, -1);
        UserIntegralDetails yesterdayIntegral = userIntegralDetailsDao.selectUserIntegralDetailsByUserId(userId, cal.getTime());
        Map<String,Object> result=new HashMap<>();
        result.put("nowIntegral",nowIntegral);
        result.put("yesterdayIntegral",yesterdayIntegral);
        return Result.success().put("data",result);
    }

    @Override
    public Result creditsExchange(Long userId,Long couponIssueId){
        UserIntegral userIntegral = userIntegralService.selectById(userId);
        SelfCouponIssue selfCouponIssue = selfCouponIssueJpaRepository.findById(couponIssueId).orElse(null);
        if(selfCouponIssue.getRemainNumber()<=0){
            return Result.error("优惠券剩余数量不足！");
        }
        if(userIntegral.getIntegralNum()<selfCouponIssue.getIntegral()){
            return Result.error("积分数量不足！");
        }
        userIntegralService.updateIntegral(2,userId,selfCouponIssue.getIntegral());
        UserIntegralDetails userIntegralDetails=new UserIntegralDetails();
        userIntegralDetails.setClassify(0);
        userIntegralDetails.setContent("积分兑换优惠券，消耗积分："+selfCouponIssue.getIntegral()+",兑换优惠券："+selfCouponIssue.getCouponName());
        userIntegralDetails.setCreateTime(DateUtils.format(new Date()));
        userIntegralDetails.setNum(selfCouponIssue.getIntegral());
        userIntegralDetails.setType(2);
        userIntegralDetails.setUserId(userId);
        userIntegralDetailsDao.insert(userIntegralDetails);
        SelfCoupon selfCoupon = selfCouponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
        SelfCouponUser selfCouponUser=new SelfCouponUser();
        selfCouponUser.setStatus(1);
        String time = DateUtil.createTime();
        selfCouponUser.setCreateTime(time);
        Integer days = selfCouponIssue.getValidDay();
        selfCouponUser.setFailureTime(DateUtil.addDays(time, days));
        selfCouponUser.setCouponId(selfCouponIssue.getCouponId());
        selfCouponUser.setCouponName(selfCouponIssue.getCouponName());
        selfCouponUser.setLessMoney(selfCoupon.getLessMoney());
        selfCouponUser.setMinMoney(selfCoupon.getMinMoney());
        UserEntity userEntity = userService.selectUserById(userId);
        selfCouponUser.setNickName(userEntity.getUserName());
        selfCouponUser.setPhone(userEntity.getPhone());
        if(selfCouponIssue.getCouponType()==2){
            selfCouponUser.setType(3);
        }else if (selfCouponIssue.getCouponType() == 3) {
            selfCouponUser.setType(4);
        }else{
            selfCouponUser.setType(2);
        }
        selfCouponUser.setUserId(userId);
        selfCouponUser.setHour(selfCouponIssue.getHour());
        selfCouponUser.setMinHour(selfCouponIssue.getMinHour());
        selfCouponUser.setGoodsIds(selfCouponIssue.getGoodsIds());
        selfCouponUser.setTearoomId(selfCouponIssue.getTearoomId());
        selfCouponUser.setTeamRule(selfCouponIssue.getTeamRule());
        selfCouponUser.setBuyRule(selfCouponIssue.getBuyRule());
        selfCouponUser.setPlatform(5);
        selfCouponUser.setCdkCode(getGeneralOrder());
        selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
        selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
        selfCouponUser.setTimes(selfCouponIssue.getTimes());
        selfCouponUserJpaRepository.save(selfCouponUser);

        selfCouponIssue.setRemainNumber(selfCouponIssue.getRemainNumber()-1);
        selfCouponIssueJpaRepository.save(selfCouponIssue);
        return Result.success();
    }

    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    @Override
    public Result selectUserIntegralDetailsCount(Integer page,Integer limit,Integer flag,String time){
        return Result.success().put("data",new PageUtils(baseMapper.selectUserIntegralDetailsCount(new Page<>(page,limit),flag,time)));
    }

    @Override
    public IPage<UserIntegralDetails> selectIntegralDetailsList(Integer page,Integer limit,String content,String userName,String phone,Long userId,
                                                         String startTime,String endTime){
        return baseMapper.selectIntegralDetailsList(new Page<>(page,limit), content, userName, phone, userId, startTime, endTime);
    }



}