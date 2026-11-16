package com.sqx.modules.orders.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.entity.SelfCoupon;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponUserRepository;
import com.sqx.modules.coupon.utils.DateUtil;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.dao.OrdersVideoDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.entity.OrdersVideo;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.orders.service.OrdersVideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrdersVideoServiceImpl extends ServiceImpl<OrdersVideoDao, OrdersVideo> implements OrdersVideoService {


    @Override
    public IPage<OrdersVideo> selectOrdersVideoList(Integer page,Integer limit,Long ordersId,Long tearoomId,Long goodsId,String goodsName,String userName,String phone,
                                                    String ordersNo,String tearoomName,Long userId,Integer classify,Long shopId){
        return baseMapper.selectOrdersVideoList(new Page<>(page,limit), ordersId, tearoomId, goodsId, goodsName, userName, phone,
                ordersNo, tearoomName,userId,classify,shopId);
    }



}
