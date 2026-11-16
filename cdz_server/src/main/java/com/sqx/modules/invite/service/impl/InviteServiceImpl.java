package com.sqx.modules.invite.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
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
import com.sqx.modules.coupon.respository.SelfCouponUserRepository;
import com.sqx.modules.coupon.utils.DateUtil;
import com.sqx.modules.invite.dao.InviteDao;
import com.sqx.modules.invite.entity.Invite;
import com.sqx.modules.invite.service.InviteMoneyService;
import com.sqx.modules.invite.service.InviteService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.SenInfoCheckUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 邀请记录
 */
@Service
public class InviteServiceImpl extends ServiceImpl<InviteDao, Invite> implements InviteService {


    @Autowired
    private InviteDao inviteDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private InviteMoneyService inviteMoneyService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private SelfCouponUserRepository selfCouponUserRepository;
    @Autowired
    private SelfCouponUserJpaRepository selfCouponUserJpaRepository;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;
    @Autowired
    private SelfCouponIssueJpaRepository couponIssueJpaRepository;

    @Override
    public PageUtils selectInviteList(int page,int limit,Integer state,Long userId){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        if(state==null || state==-1){
            state=null;
        }
        return new PageUtils(inviteDao.selectInviteList(pages,state,userId));
    }


    @Override
    public PageUtils selectInviteUser(int page,int limit,Long userId,Integer state){
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.selectInviteUser(pages,userId,state));
    }

    @Override
    public Integer selectInviteByUserIdCountNotTime(Long userId) {
        return inviteDao.selectInviteByUserIdCountNotTime(userId);
    }

    @Override
    public Integer selectInviteByUserIdCount(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdCount(userId,startTime,endTime);
    }

    @Override
    public Double selectInviteByUserIdSum(Long userId, Date startTime, Date endTime) {
        return inviteDao.selectInviteByUserIdSum(userId,startTime,endTime);
    }

    @Override
    public Double sumInviteMoney(String time, Integer flag) {
        return inviteDao.sumInviteMoney(time,flag);
    }

    @Override
    public PageUtils inviteAnalysis(int page,int limit, String time, Integer flag) {
        Page<Map<String,Object>> pages=new Page<>(page,limit);
        return new PageUtils(inviteDao.inviteAnalysis(pages,time,flag));
    }

    @Override
    public Integer selectInviteCount(Integer state,Long userId){
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteCount(state,userId);
    }

    @Override
    public Double selectInviteSum(Integer state, Long userId) {
        if(state==null || state==-1){
            state=null;
        }
        return inviteDao.selectInviteSum(state,userId);
    }


    @Transactional
    @Override
    public int saveBody(Long userId, UserEntity userEntity){
        return 1;
    }

    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    @Override
    public void updateInvite(UserEntity userEntity,String format,Long userId){
        Invite invite1 = inviteDao.selectInviteByUser(userEntity.getUserId(), userId);
        if(invite1==null){
            Invite invite=new Invite();
            invite.setState(0);
            invite.setMoney(0.00);
            invite.setUserId(userEntity.getUserId());
            invite.setInviteeUserId(userId);
            invite.setCreateTime(format);
            inviteDao.insert(invite);
            invite1 = inviteDao.selectInviteByUser(userEntity.getUserId(), userId);
        }
        if(invite1.getState()==0){
            CommonInfo one = commonInfoService.findOne(189);
            if(one!=null && StringUtils.isNotEmpty(one.getValue()) && Double.parseDouble(one.getValue())>0){
                Double money=Double.parseDouble(one.getValue());
                invite1.setState(1);
                invite1.setMoney(money);
                inviteDao.updateById(invite1);
                inviteMoneyService.updateInviteMoneySum(money,userEntity.getUserId());
                userMoneyService.updateMoney(1,userEntity.getUserId(),money);
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setUserId(userEntity.getUserId());
                UserEntity userEntity1 = userService.selectUserById(userId);
                userMoneyDetails.setTitle("[邀请好友]好友名称："+userEntity1.getUserName());
                userMoneyDetails.setContent("增加金额："+money);
                userMoneyDetails.setType(1);
                userMoneyDetails.setMoney(new BigDecimal(money));
                userMoneyDetails.setCreateTime(format);
                userMoneyDetailsService.save(userMoneyDetails);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setContent("恭喜您，邀请的好友注册成功了，赠送您："+money+"金额！");
                messageInfo.setTitle("邀请赏金");
                messageInfo.setState(String.valueOf(5));
                messageInfo.setUserName(userEntity.getUserName());
                messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
                SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                messageInfo.setCreateAt(sdf.format(new Date()));
                messageInfo.setIsSee("0");
                messageService.saveBody(messageInfo);
                CommonInfo templateId = commonInfoService.findOne(1045);
                List<String> data = new ArrayList<>();
                String title=messageInfo.getTitle();
                if(title.length()>20){
                    title=title.substring(0,15)+"...";
                }
                data.add(title);
                String content=messageInfo.getContent();
                if(content.length()>20){
                    content=content.substring(0,15)+"...";
                }
                data.add(content);
                data.add(DateUtils.format(new Date()));
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), templateId.getValue(), data, 8);
                if(StringUtils.isNotEmpty(userEntity.getClientid())){
                    userService.pushToSingle("邀请赏金","恭喜您，邀请的好友注册成功了，赠送您："+money+"金额！",userEntity.getClientid());
                }



            }
        }
    }


}
