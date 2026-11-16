package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.coupon.dao.CouponIssueDao;
import com.sqx.modules.coupon.dao.CouponUserDao;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.entity.CouponIssue;
import com.sqx.modules.coupon.entity.CouponUser;
import com.sqx.modules.coupon.service.CouponIssueService;
import com.sqx.modules.coupon.service.CouponService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class CouponIssueServiceImpl extends ServiceImpl<CouponIssueDao, CouponIssue> implements CouponIssueService {

    @Autowired
    private CouponUserDao couponUserDao;
    @Autowired
    private CouponService couponService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;


    @Override
    public Result selectCouponIssueList(Integer page,Integer limit,String couponIssueName,Integer classify,Integer status){
        IPage<CouponIssue> couponIssuePage = baseMapper.selectPage(new Page<>(page, limit),
                new QueryWrapper<CouponIssue>()
                        .eq(StringUtils.isNotBlank(couponIssueName), "coupon_issue_name", couponIssueName)
                        .eq(classify != 0, "classify", classify)
                        .eq(status != 0, "status", status));
        List<CouponIssue> records = couponIssuePage.getRecords();
        for(CouponIssue couponIssue:records){
            String[] couponIds = couponIssue.getCouponIds().split(",");
            List<Coupon> couponList=new ArrayList<>();
            for(String couponId:couponIds) {
                Coupon coupon = couponService.getById(Long.parseLong(couponId));
                couponList.add(coupon);
            }
            couponIssue.setCouponList(couponList);
        }
        return Result.success().put("data",new PageUtils(couponIssuePage));
    }

    @Override
    public Result selectCouponIssueListByUserId(Integer page,Integer limit,Integer classify,Long userId){
        return Result.success().put("data",new PageUtils(baseMapper.selectCouponIssueListByUserId(new Page<>(page,limit),classify,userId)));
    }


    @Override
    public Result insertCouponUser(Long userId, Long couponIssueId){
        CouponIssue couponIssue = baseMapper.selectById(couponIssueId);
        if(couponIssue.getIsLimit()==1){
            //是否限购
            CouponUser couponUser = couponUserDao.selectOne(new QueryWrapper<CouponUser>().eq("coupon_issue_id", couponIssueId));
            if(couponUser!=null){
                return Result.error("当前优惠券为限购，您已经拥有了！");
            }
            if(couponIssue.getCouponNum()<=0){
                return Result.error("优惠券已经被抢完了！");
            }
        }
        String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        if(couponIssue.getClassify()==2){
            UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
            if(couponIssue.getPrice().doubleValue()>userMoney.getMoney().doubleValue()){
                return Result.error("金额不足,请先充值 ！");
            }
            userMoneyService.updateMoney(2,userId,couponIssue.getPrice().doubleValue());
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(couponIssue.getPrice());
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setTitle("零钱购买优惠券，卷包："+couponIssue.getCouponIssueName());
            userMoneyDetails.setContent("购买优惠券费用："+couponIssue.getPrice());
            userMoneyDetails.setType(2);
            userMoneyDetails.setCreateTime(createTime);
            userMoneyDetailsService.save(userMoneyDetails);
        }
        String[] couponIds = couponIssue.getCouponIds().split(",");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for(String couponId:couponIds){
            Coupon coupon = couponService.getById(Long.parseLong(couponId));
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
            couponUserDao.insert(couponUser);
        }
        couponIssue.setCouponNum(couponIssue.getCouponNum()-1);
        baseMapper.updateById(couponIssue);
        return Result.success();
    }


}
