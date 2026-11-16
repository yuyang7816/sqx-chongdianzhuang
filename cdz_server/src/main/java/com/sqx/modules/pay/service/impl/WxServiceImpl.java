package com.sqx.modules.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.entity.SelfCoupon;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.sqx.modules.coupon.service.SelfCouponUserService;
import com.sqx.modules.coupon.utils.DateUtil;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.config.WXConfig;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayClassifyService;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.tearoom.entity.*;
import com.sqx.modules.tearoom.service.*;
import com.sqx.modules.utils.AmountCalUtils;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.MD5Util;
import com.sqx.modules.utils.WXConfigUtil;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author fang
 * @date 2020/2/26
 */
@Service
@Slf4j
public class WxServiceImpl implements WxService {
    private static final String SPBILL_CREATE_IP = "127.0.0.1";
    private static final String TRADE_TYPE_APP = "APP";
    private static final String TRADE_TYPE_NATIVE = "NATIVE";
    private static final String TRADE_TYPE_JSAPI = "JSAPI";
    private static final String Wap = "MWEB";

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserService userService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private SelfCouponUserJpaRepository couponUserJpaRepository;
    @Autowired
    private SelfCouponIssueJpaRepository selfCouponIssueJpaRepository;
    @Autowired
    private SelfCouponJpaRepository selfCouponJpaRepository;
    @Autowired
    private PayClassifyService payClassifyService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserVipService userVipService;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);



    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @Override
    public Result payMoney(Long payClassifyId, Integer classify,Long userId, HttpServletRequest request) throws Exception {
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        BigDecimal money = payClassify.getPrice();
        String generalOrder = getGeneralOrder();
        PayDetails payDetails = new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(generalOrder);
        payDetails.setUserId(userId);
        payDetails.setMoney(money.doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(1);
        payDetails.setRemark(String.valueOf(payClassifyId));
        payDetailsDao.insert(payDetails);
        return pay(money.doubleValue(), classify,userId, generalOrder,request);
    }

    @Override
    public Result payOrders(String ordersNo, Integer classify,Long userId, HttpServletRequest request) throws Exception {
        Orders orders = ordersService.selectOrdersByNo(ordersNo);
        if(orders.getStatus()==2 || orders.getStatus()==3){
            return Result.success().put("data",orders);
        }
        PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
        if(payDetails==null){
            payDetails=new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(ordersNo);
            payDetails.setUserId(userId);
            payDetails.setMoney(orders.getPayMoney().doubleValue());
            payDetails.setClassify(classify);
            payDetails.setType(2);
            payDetailsDao.insert(payDetails);
        }
        return pay(orders.getPayMoney().doubleValue(), classify,userId, ordersNo,request);
    }

    @Override
    public Result payCoupon(Long couponIssueId, Integer classify, @RequestAttribute Long userId, HttpServletRequest request) throws Exception{
        String ordersNo = getGeneralOrder();
        SelfCouponIssue selfCouponIssue = selfCouponIssueJpaRepository.findById(couponIssueId).orElse(null);
        SelfCoupon selfCoupon = selfCouponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
        PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
        if(payDetails==null){
            payDetails=new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(ordersNo);
            payDetails.setUserId(userId);
            payDetails.setMoney(Double.parseDouble(selfCoupon.getLessMoney()));
            payDetails.setClassify(classify);
            payDetails.setType(4);
            payDetails.setRemark(String.valueOf(couponIssueId));
            payDetailsDao.insert(payDetails);
        }
        return pay(Double.parseDouble(selfCoupon.getLessMoney()), classify,userId, ordersNo,request);
    }

    @Override
    public Result payMember(Long vipDetailsId, Integer classify, Long userId, HttpServletRequest request) throws Exception{
        String ordersNo = getGeneralOrder();
        VipDetails vipDetails = vipDetailsDao.selectById(vipDetailsId);
        PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
        if(payDetails==null){
            payDetails=new PayDetails();
            payDetails.setState(0);
            payDetails.setCreateTime(sdf.format(new Date()));
            payDetails.setOrderId(ordersNo);
            payDetails.setUserId(userId);
            payDetails.setMoney(vipDetails.getMoney().doubleValue());
            payDetails.setClassify(classify);
            payDetails.setType(5);
            payDetails.setRemark(String.valueOf(vipDetailsId));
            payDetailsDao.insert(payDetails);
        }
        return pay(payDetails.getMoney(), classify,userId, ordersNo,request);
    }


    /**
     * 微信支付订单生成
     *
     * @param moneys     支付金额 带小数点
     * @param type       类型 1app  2 二维码支付  3小程序 公众号支付
     * @param userId     用户id
     * @param outTradeNo 单号
     * @return
     * @throws Exception
     */
    private Result pay(Double moneys, Integer type, Long userId, String outTradeNo,HttpServletRequest request) throws Exception {
        //h5服务域名配置
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if (type == 3) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyJsApi";
        } else if (type == 2 || type==4) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyMp";
        } else {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notify";
        }
        String currentTimeMillis = (System.currentTimeMillis() / 1000) + "";
        //后台服务名称
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址：" + url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money = String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        //微信小程序APPID        微信公众号APPID
        if (type == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (type == 2 || type==4) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        //微信商户key
        config.setKey(commonInfoService.findOne(75).getValue());
        //微信商户号mchId
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", generateNonceStr);
        String body = one.getValue();
        data.put("body", body);
        //生成商户订单号，不可重复
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", money);
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", url);
        //交易类型

        if(type==1){
            data.put("trade_type", TRADE_TYPE_APP);
        }else if(type==2){
            data.put("trade_type",TRADE_TYPE_JSAPI);
        }else if(type==3){
            data.put("trade_type",TRADE_TYPE_JSAPI);
        }else{
            data.put("trade_type",Wap);
            data.put("spbill_create_ip", HttpClientUtil.getIpAddress(request));
        }
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        if (type == 3 || type == 2) {
            UserEntity userEntity = userService.queryByUserId(userId);
            if (type == 3) {
                if (StringUtils.isNotBlank(userEntity.getOpenId())) {
                    data.put("openid", userEntity.getOpenId());
                }
            } else {
                data.put("openid", userEntity.getWxOpenId());
            }
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for (String key : response.keySet()) {
            log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数

            Map<String, String> param = new HashMap<>();
            if (type == 1) {
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                        WXPayConstants.SignType.MD5));
                param.put("outtradeno", outTradeNo);
            }else if(type==2 || type==3){
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                    /*param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));*/
                String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + currentTimeMillis + "" + "&key=" + config.getKey();
                String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                param.put("sign", sign);
                param.put("outtradeno", outTradeNo);
                param.put("package", "prepay_id=" + response.get("prepay_id"));//给前端返回的值
                param.put("mweb_url", response.get("mweb_url"));
                param.put("trade_type", response.get("trade_type"));
                param.put("return_msg", response.get("return_msg"));
                param.put("result_code", response.get("result_code"));
                param.put("signType", "MD5");
            }else{
                param.put("mweb_url", response.get("mweb_url"));
            }
            return Result.success().put("data", param);
        }
        return Result.error("获取订单失败");
    }

    private Result pays(Double moneys, Integer type, Long userId, String outTradeNo,HttpServletRequest request) throws Exception {
        //h5服务域名配置
        CommonInfo oneu = commonInfoService.findOne(19);
        String url;
        if (type == 3) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyJsApis";
        } else if (type == 2 || type == 4) {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifyMps";
        } else {
            url = oneu.getValue() + "/sqx_fast/app/wxPay/notifys";
        }
        String currentTimeMillis=(System.currentTimeMillis() / 1000)+"";
        //后台服务名称
        CommonInfo one = commonInfoService.findOne(12);
        log.info("回调地址：" + url);
        Double mul = AmountCalUtils.mul(moneys, 100);
        String money = String.valueOf(mul.intValue());
        String generateNonceStr = WXPayUtil.generateNonceStr();
        WXConfig config = new WXConfig();
        //微信小程序APPID        微信公众号APPID
        if(type==1){
            config.setAppId(commonInfoService.findOne(260).getValue());//微信商户key
            config.setKey(commonInfoService.findOne(245).getValue());
            //微信商户号mchId
            config.setMchId(commonInfoService.findOne(246).getValue());
        }else if(type==2 || type == 4){
            config.setAppId(commonInfoService.findOne(5).getValue());
            //微信商户key
            config.setKey(commonInfoService.findOne(75).getValue());
            //微信商户号mchId
            config.setMchId(commonInfoService.findOne(76).getValue());
        }else{
            config.setAppId(commonInfoService.findOne(391).getValue());
            //微信商户key
            config.setKey(commonInfoService.findOne(75).getValue());
            //微信商户号mchId
            config.setMchId(commonInfoService.findOne(76).getValue());
        }

        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", generateNonceStr);
        String body = one.getValue();
        data.put("body", body);
        //生成商户订单号，不可重复
        data.put("out_trade_no", outTradeNo);
        data.put("total_fee", money);
        //自己的服务器IP地址
        data.put("spbill_create_ip", SPBILL_CREATE_IP);
        //异步通知地址（请注意必须是外网）
        data.put("notify_url", url);
        //交易类型
        if (type == 1) {
            data.put("trade_type", TRADE_TYPE_APP);
        } else if (type == 2) {
            data.put("trade_type", TRADE_TYPE_JSAPI);
        } else if (type == 3) {
            data.put("trade_type", TRADE_TYPE_JSAPI);
        } else {
            data.put("trade_type", Wap);
            data.put("spbill_create_ip", HttpClientUtil.getIpAddress(request));
        }
        //附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
        data.put("attach", "");
        data.put("sign", WXPayUtil.generateSignature(data, config.getKey(),
                WXPayConstants.SignType.MD5));
        if(type==3 || type==2){
            UserEntity userEntity = userService.queryByUserId(userId);
            if(type==3){
                if(StringUtils.isNotBlank(userEntity.getArtificerOpenId())){
                    data.put("openid", userEntity.getArtificerOpenId());
                }
            }else{
                data.put("openid", userEntity.getWxOpenId());
            }
        }
        //使用官方API请求预付订单
        Map<String, String> response = wxpay.unifiedOrder(data);
        for (String key : response.keySet()) {
            log.info("微信支付订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
        }
        if ("SUCCESS".equals(response.get("return_code"))) {//主要返回以下5个参数

            if (type == 1) {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("package", "Sign=WXPay");
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                        WXPayConstants.SignType.MD5));
                param.put("outtradeno", outTradeNo);
                return Result.success().put("data", param);
            } else {
                Map<String, String> param = new HashMap<>();
                param.put("appid", config.getAppID());
                param.put("partnerid", response.get("mch_id"));
                param.put("prepayid", response.get("prepay_id"));
                param.put("noncestr", generateNonceStr);
                param.put("timestamp", currentTimeMillis);
                    /*param.put("sign", WXPayUtil.generateSignature(param, config.getKey(),
                            WXPayConstants.SignType.MD5));*/
                String stringSignTemp = "appId=" + config.getAppID() + "&nonceStr=" + generateNonceStr + "&package=prepay_id=" + response.get("prepay_id") + "&signType=MD5&timeStamp=" + currentTimeMillis + "" + "&key=" + config.getKey();
                String sign = MD5Util.md5Encrypt32Upper(stringSignTemp).toUpperCase();
                param.put("sign", sign);
                param.put("outtradeno", outTradeNo);
                param.put("package", "prepay_id=" + response.get("prepay_id"));//给前端返回的值
                param.put("mweb_url", response.get("mweb_url"));
                param.put("trade_type", response.get("trade_type"));
                param.put("return_msg", response.get("return_msg"));
                param.put("result_code", response.get("result_code"));
                param.put("signType", "MD5");
                return Result.success().put("data", param);
            }
        }
        return Result.error("获取订单失败");
    }

    @Override
    public String payBack(String resXml, Integer type) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("进入回调了！！！");
        if (type == 1) {
            config.setAppId(commonInfoService.findOne(74).getValue());
        } else if (type == 2) {
            config.setAppId(commonInfoService.findOne(5).getValue());
        } else {
            config.setAppId(commonInfoService.findOne(45).getValue());
        }
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                log.error("验证成功！！！");
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    log.error("验证成功222！！！");
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        log.error("订单号！！！" + out_trade_no);
                        reentrantReadWriteLock.writeLock().lock();
                        try {
                            PayDetails payDetails = payDetailsDao.selectByOrderId(out_trade_no);
                            payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), "");
                            if (payDetails.getState() == 0) {
                                if (payDetails.getType() == 1) {
                                    PayClassify payClassify = payClassifyService.getById(Long.parseLong(payDetails.getRemark()));
                                    BigDecimal money = payClassify.getMoney().add(payClassify.getGiveMoney());
                                    //调用处理接口
                                    userMoneyDao.updateMayMoney(1, payDetails.getUserId(), money.doubleValue());
                                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                    userMoneyDetails.setUserId(payDetails.getUserId());
                                    userMoneyDetails.setTitle("微信充值");
                                    userMoneyDetails.setContent("微信充值:" + payDetails.getMoney() + ",到账：" + money);
                                    userMoneyDetails.setType(1);
                                    userMoneyDetails.setMoney(money);
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    userMoneyDetails.setCreateTime(sdf.format(new Date()));
                                    userMoneyDetailsService.save(userMoneyDetails);

                                }else if(payDetails.getType()==2){
                                    Orders orders = ordersService.selectOrdersByNo(payDetails.getOrderId());
                                    String createTime = DateUtils.format(new Date());
                                    if(orders.getOrdersType()==1) {
                                        Goods goods = goodsService.getById(orders.getGoodsId());
                                        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                        userMoneyDetails.setMoney(orders.getPayMoney());
                                        userMoneyDetails.setUserId(orders.getUserId());
                                        userMoneyDetails.setContent("微信支付购买：" + goods.getGoodsName());
                                        userMoneyDetails.setTitle("支付订单");
                                        userMoneyDetails.setType(2);
                                        userMoneyDetails.setCreateTime(createTime);
                                        userMoneyDetailsService.save(userMoneyDetails);
                                        orders.setPayTime(createTime);
                                        orders.setPayWay(1);
                                        ordersService.updateById(orders);
                                        goods.setGoodsStatus(2);
                                        goods.setOrdersId(orders.getOrdersId());
                                        goodsService.updateById(goods);
                                        List<Room> lightRoom = roomService.list(new QueryWrapper<Room>().eq("goods_id", orders.getGoodsId()));
                                        for (Room room : lightRoom) {
                                            try {
                                                roomService.issueInstruct(room.getRoomId(), 1);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }

                                } else if (payDetails.getType() == 4) {
                                    Long couponIssueId = Long.parseLong(payDetails.getRemark());
                                    Long userId = payDetails.getUserId();
                                    SelfCouponIssue selfCouponIssue = selfCouponIssueJpaRepository.findById(couponIssueId).orElse(null);
                                    SelfCoupon selfCoupon = selfCouponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);

                                    SelfCouponUser selfCouponUser = new SelfCouponUser();
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
                                    if (selfCouponIssue.getCouponType() == 2) {
                                        selfCouponUser.setType(3);
                                    } else if (selfCouponIssue.getCouponType() == 3) {
                                        selfCouponUser.setType(4);
                                    } else {
                                        selfCouponUser.setType(2);
                                    }
                                    selfCouponUser.setUserId(userId);
                                    selfCouponUser.setHour(selfCouponIssue.getHour());
                                    selfCouponUser.setMinHour(selfCouponIssue.getMinHour());
                                    selfCouponUser.setGoodsIds(selfCouponIssue.getGoodsIds());
                                    selfCouponUser.setTearoomId(selfCouponIssue.getTearoomId());
                                    selfCouponUser.setTeamRule(selfCouponIssue.getTeamRule());
                                    selfCouponUser.setBuyRule(selfCouponIssue.getBuyRule());
                                    selfCouponUser.setPayWay(payDetails.getClassify());
                                    selfCouponUser.setOrdersId(payDetails.getOrderId());
                                    selfCouponUser.setPayMoney(BigDecimal.valueOf(payDetails.getMoney()));
                                    selfCouponUser.setPlatform(4);
                                    selfCouponUser.setCdkCode(getGeneralOrder());
                                    selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
                                    selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
                                    selfCouponUser.setTimes(selfCouponIssue.getTimes());
                                    couponUserJpaRepository.save(selfCouponUser);

                                    Tearoom tearoom = tearoomService.getById(selfCouponIssue.getTearoomId());

                                    BigDecimal shopMoney = BigDecimal.valueOf(payDetails.getMoney()).multiply(tearoom.getRate()).setScale(2, RoundingMode.DOWN);
                                    userMoneyService.updateShopMoney(1, tearoom.getUserId(), shopMoney.doubleValue());
                                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                    userMoneyDetails.setMoney(shopMoney);
                                    userMoneyDetails.setShopUserId(tearoom.getUserId());
                                    userMoneyDetails.setContent("购买团购劵收益：" + shopMoney);
                                    userMoneyDetails.setTitle("购买团购劵：" + selfCouponIssue.getCouponName());
                                    userMoneyDetails.setType(1);
                                    userMoneyDetails.setClassify(5);
                                    userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                                    userMoneyDetailsService.save(userMoneyDetails);


                                }else if(payDetails.getType()==5){
                                    VipDetails vipDetails = vipDetailsDao.selectById(Long.parseLong(payDetails.getRemark()));
                                    UserVip userVip = userVipService.selectUserVipByUserId(payDetails.getUserId());
                                    Calendar calendar=Calendar.getInstance();
                                    if(userVip!=null && userVip.getIsVip()==1){
                                        calendar.setTime(DateUtils.stringToDate(userVip.getEndTime(),DateUtils.DATE_TIME_PATTERN));
                                    }
                                    if(userVip==null){
                                        userVip=new UserVip();
                                        userVip.setUserId(payDetails.getUserId());
                                        userVip.setCreateTime(DateUtils.format(new Date()));
                                    }
                                    userVip.setIsVip(1);
                                    if(vipDetails.getVipNameType()==1){
                                        calendar.add(Calendar.MONTH,1);
                                    }else if(vipDetails.getVipNameType()==2){
                                        calendar.add(Calendar.MONTH,3);
                                    }else{
                                        calendar.add(Calendar.MONTH,12);
                                    }
                                    userVip.setEndTime(DateUtils.format(calendar.getTime()));
                                    if(userVip.getVipId()==null){
                                        userVipService.save(userVip);
                                    }else{
                                        userVipService.updateById(userVip);
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            log.error("微信支付回调异常：" + e.getMessage(), e);
                        } finally {
                            reentrantReadWriteLock.writeLock().unlock();
                        }
                        log.error("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        log.error("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                log.error("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            log.error("手机支付回调通知失败" + e.getMessage(), e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }



    @Override
    public boolean refund(String ordersNo,BigDecimal payMoney,BigDecimal refundMoney) {
        PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
        WXConfigUtil config = null;
        String h5Url = commonInfoService.findOne(19).getValue().split("://")[1];
        String filePath = "/www/wwwroot/"+h5Url+"/service/apiclient_cert.p12";
        try {
            config = new WXConfigUtil(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int commInfoId = 0;
        //orders.getPayWay(); //支付方式（1app微信 2微信公众号 3微信小程序 4app支付宝 5H5支付宝 6零钱）
        switch (payDetails.getClassify()) {
            case 1:
                commInfoId = 5;
                break; //appId
            case 2:
                commInfoId = 74;
                break; //公众号id
            case 3:
                commInfoId = 45;
                break; //小程序id
        }
        config.setAppId(commonInfoService.findOne(commInfoId).getValue());
        config.setKey(commonInfoService.findOne(75).getValue());
        config.setMchId(commonInfoService.findOne(76).getValue());
        WXPay wxpay = new WXPay(config);
        Map<String, String> data = new HashMap<>();
        data.put("appid", config.getAppID());
        data.put("mch_id", config.getMchID());
        data.put("nonce_str", WXPayUtil.generateNonceStr());
        try {
            data.put("sign", WXPayUtil.generateSignature(data, config.getKey(), WXPayConstants.SignType.MD5));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        data.put("out_trade_no", ordersNo); //订单号,支付单号一致
        data.put("out_refund_no", getGeneralOrder()); //退款单号，同一笔用不同的退款单号
        data.put("total_fee", payMoney.multiply(BigDecimal.valueOf(100)).intValue()+""); //1块等于微信支付传入100);
        data.put("refund_fee", refundMoney.multiply(BigDecimal.valueOf(100)).intValue()+""); //1块等于微信支付传入100);
        //使用官方API退款
        try {
            Map<String, String> response = wxpay.refund(data);
            for (String key : response.keySet()) {
                log.info("微信退款订单微信返回参数：keys:" + key + "    value:" + response.get(key).toString());
            }
            if ("SUCCESS".equals(response.get("return_code")) && "SUCCESS".equals(response.get("result_code"))) {//主要返回以下5个参数
                System.err.println("退款成功");
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.info("返回");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String payBacks(String resXml, Integer type) {
        WXConfig config = null;
        try {
            config = new WXConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.error("进入回调了！！！");
        if(type==1){
            config.setAppId(commonInfoService.findOne(260).getValue());
            config.setKey(commonInfoService.findOne(245).getValue());
            config.setMchId(commonInfoService.findOne(246).getValue());
        }else if(type==2){
            config.setAppId(commonInfoService.findOne(5).getValue());
            config.setKey(commonInfoService.findOne(75).getValue());
            config.setMchId(commonInfoService.findOne(76).getValue());
        }else{
            config.setAppId(commonInfoService.findOne(391).getValue());
            config.setKey(commonInfoService.findOne(392).getValue());
            config.setMchId(commonInfoService.findOne(246).getValue());
        }

        WXPay wxpay = new WXPay(config);
        String xmlBack = "";
        Map<String, String> notifyMap = null;
        try {
            notifyMap = WXPayUtil.xmlToMap(resXml);         // 调用官方SDK转换成map类型数据
            if (wxpay.isPayResultNotifySignatureValid(notifyMap)) {//验证签名是否有效，有效则进一步处理
                log.error("验证成功！！！");
                String return_code = notifyMap.get("return_code");//状态
                String out_trade_no = notifyMap.get("out_trade_no");//商户订单号
                if (return_code.equals("SUCCESS")) {
                    log.error("验证成功222！！！");
                    if (out_trade_no != null) {
                        // 注意特殊情况：订单已经退款，但收到了支付结果成功的通知，不应把商户的订单状态从退款改成支付成功
                        // 注意特殊情况：微信服务端同样的通知可能会多次发送给商户系统，所以数据持久化之前需要检查是否已经处理过了，处理了直接返回成功标志
                        //业务数据持久化
                        log.error("订单号！！！" + out_trade_no);
                        PayDetails payDetails = payDetailsDao.selectByOrderId(out_trade_no);
                        if (payDetails.getState() == 0) {
                            payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), "");
                            if(payDetails.getType()==11){
                                //缴纳保证金
                                Double cashDeposit = payDetails.getMoney();
                                Long userId = payDetails.getUserId();
                                userMoneyDao.updateCashDeposit(1, userId, cashDeposit);
                                UserEntity userEntity = userService.selectUserById(userId);
                                userEntity.setIsCashDeposit(2);
                                userService.updateById(userEntity);
                                //交保证金的记录
                                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                                userMoneyDetails.setUserId(userId);
                                userMoneyDetails.setTitle("缴纳保证金");
                                userMoneyDetails.setContent("已缴纳保证金："+cashDeposit+"元");
                                userMoneyDetails.setType(1);
                                userMoneyDetails.setMoney(BigDecimal.valueOf(cashDeposit));
                                userMoneyDetails.setState(2);
                                userMoneyDetails.setClassify(1);
                                userMoneyDetails.setCreateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                                userMoneyDetailsService.save(userMoneyDetails);
                            }else if(payDetails.getType()==3){
                                userMoneyDao.updateMayMoney(1, payDetails.getUserId(), payDetails.getMoney());
                                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                userMoneyDetails.setUserId(payDetails.getUserId());
                                userMoneyDetails.setTitle("微信充值");
                                userMoneyDetails.setContent("微信充值:"+payDetails.getMoney());
                                userMoneyDetails.setType(1);
                                userMoneyDetails.setMoney(BigDecimal.valueOf(payDetails.getMoney()));
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                userMoneyDetails.setCreateTime(sdf.format(new Date()));
                                userMoneyDetailsService.save(userMoneyDetails);
                            }
                        }
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
                    } else {
                        System.err.println("微信手机支付回调成功订单号:" + out_trade_no + "");
                        xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                    }
                } else {
                }
                return xmlBack;
            } else {
                // 签名错误，如果数据里没有sign字段，也认为是签名错误
                System.err.println("手机支付回调通知签名错误");
                xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
                return xmlBack;
            }
        } catch (Exception e) {
            System.err.println("手机支付回调通知失败" + e);
            xmlBack = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
        }
        return xmlBack;
    }






}