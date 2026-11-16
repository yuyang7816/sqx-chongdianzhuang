package com.sqx.modules.app.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.*;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.*;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.MD5Util;
import jodd.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
@Slf4j
public class UserMoneyServiceImpl extends ServiceImpl<UserMoneyDao, UserMoney> implements UserMoneyService {
    @Autowired
    private UserMoneyDetailsDao userMoneyDetailsDao;
    @Autowired
    private UserVipDao userVipDao;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private MsgService msgService;

    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);


    @Override
    public void updateMoney(int i, Long userId, double money) {
        baseMapper.updateMayMoney(i, userId, money);
    }

    @Override
    public void updateInviteMoney(int i, Long userId, double money) {
        baseMapper.updateInviteMoney(i, userId, money);
    }



    @Override
    public void updateShopMoney(int i, Long userId, double money) {
        baseMapper.updateShopMoney(i, userId, money);
    }

    @Override
    public void updateSumShopMoney(int i, Long userId, double money) {
        baseMapper.updateSumShopMoney(i, userId, money);
    }

    @Override
    public UserMoney selectUserMoneyByUserId(Long userId) {
        return baseMapper.selectOne(new QueryWrapper<UserMoney>().eq("user_id", userId));
    }

    @Override
    public Double selectMyProfit(Long userId) {

        return userMoneyDetailsDao.selectMyProfit(userId);
    }

    @Override
    public PageUtils balanceDetailed(Long userId, Page<UserMoneyDetails> pages) {
        return new PageUtils(userMoneyDetailsDao.selectUserMoneyDetailsUserId(pages,userId));
    }

    @Override
    public PageUtils selectInviteMoneyDetailsUserId(Long userId, Page<UserMoneyDetails> pages) {
        return new PageUtils(userMoneyDetailsDao.selectInviteMoneyDetailsUserId(pages,userId));
    }


    @Override
    public PageUtils selectShopUserMoneyDetailsList(Long userId, Page<UserMoneyDetails> pages) {
        return new PageUtils(userMoneyDetailsDao.selectPage(pages,new QueryWrapper<UserMoneyDetails>().eq("shop_user_id",userId).orderByDesc("create_time ")));
    }

    @Override
    public Double selectSumMoney(){
        return baseMapper.selectSumMoney();
    }


}
