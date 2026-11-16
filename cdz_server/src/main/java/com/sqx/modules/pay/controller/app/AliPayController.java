package com.sqx.modules.pay.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.domain.AlipayTradeRefundModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.dao.VipDetailsDao;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.service.UserMoneyDetailsService;
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
import com.sqx.modules.coupon.utils.DateUtil;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.config.AliPayConstants;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayClassify;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayClassifyService;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Room;
import com.sqx.modules.tearoom.service.*;
import com.sqx.modules.utils.AmountCalUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 支付宝支付处理--暂不做同步处理、回调方式使用异步
 */
@Slf4j
@RestController
@Api(value = "支付宝支付", tags = {"支付宝支付"})
@RequestMapping("/app/aliPay")
public class AliPayController {

    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SelfCouponUserJpaRepository couponUserJpaRepository;
    @Autowired
    private SelfCouponIssueJpaRepository selfCouponIssueJpaRepository;
    @Autowired
    private SelfCouponJpaRepository selfCouponJpaRepository;
    @Autowired
    private RoomService roomService;
    @Autowired
    private PayClassifyService payClassifyService;
    @Autowired
    private VipDetailsDao vipDetailsDao;
    @Autowired
    private UserVipService userVipService;

    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    @RequestMapping(value = "/notifyApp", method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public void notifyApp(HttpServletRequest request, HttpServletResponse response) {
        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }
        try {
            log.info("回调成功！！！");
            boolean flag = AlipaySignature.rsaCheckV1(params, commonInfoService.findOne(64).getValue(), AliPayConstants.CHARSET, "RSA2");
            log.info(flag + "回调验证信息");
            if (flag) {
                String tradeStatus = params.get("trade_status");
                if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                    reentrantReadWriteLock.writeLock().lock();
                    try{
                        //支付宝返回的订单编号
                        String outTradeNo = params.get("out_trade_no");
                        log.error(outTradeNo);
                        //支付宝支付单号
                        String tradeNo = params.get("trade_no");
                        log.error(tradeNo);
                        PayDetails payDetails = payDetailsDao.selectByOrderId(outTradeNo);
                        if (payDetails.getState() == 0) {
                            payDetailsDao.updateState(payDetails.getId(), 1, sdf.format(new Date()), tradeNo);
                            if(payDetails.getType()==1){
                                PayClassify payClassify = payClassifyService.getById(Long.parseLong(payDetails.getRemark()));
                                BigDecimal money = payClassify.getMoney();
                                //调用处理接口
                                userMoneyDao.updateMayMoney(1, payDetails.getUserId(), money.doubleValue());
                                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                                userMoneyDetails.setUserId(payDetails.getUserId());
                                userMoneyDetails.setTitle("支付宝充值");
                                userMoneyDetails.setContent("支付宝充值:"+payDetails.getMoney()+",到账"+money);
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
                                    userMoneyDetails.setContent("支付宝支付购买：" + goods.getGoodsName());
                                    userMoneyDetails.setTitle("支付订单");
                                    userMoneyDetails.setType(2);
                                    userMoneyDetails.setCreateTime(createTime);
                                    userMoneyDetailsService.save(userMoneyDetails);
                                    orders.setPayTime(createTime);
                                    orders.setPayWay(2);
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

                            }else if(payDetails.getType()==4){
                                Long couponIssueId=Long.parseLong(payDetails.getRemark());
                                Long userId=payDetails.getUserId();
                                SelfCouponIssue selfCouponIssue = selfCouponIssueJpaRepository.findById(couponIssueId).orElse(null);
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
                                selfCouponUser.setPayWay(payDetails.getClassify());
                                selfCouponUser.setOrdersId(payDetails.getOrderId());
                                selfCouponUser.setPayMoney(BigDecimal.valueOf(payDetails.getMoney()));
                                selfCouponUser.setPlatform(4);
                                selfCouponUser.setCdkCode(getGeneralOrder());
                                selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
                                selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
                                selfCouponUser.setTimes(selfCouponIssue.getTimes());
                                couponUserJpaRepository.save(selfCouponUser);
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
                    }catch (Exception e){
                        e.printStackTrace();
                        log.error("支付宝回调异常："+e.getMessage());
                    }finally {
                        reentrantReadWriteLock.writeLock().unlock();
                    }
                }
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
            log.info("回调验证失败！！！");
        }
    }


    @Login
    @ApiOperation("充值")
    @RequestMapping(value = "/payMoney",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payMoney(Long payClassifyId, Integer classify, @RequestAttribute Long userId){
        PayClassify payClassify = payClassifyService.getById(payClassifyId);
        BigDecimal money = payClassify.getPrice();
        String name = commonInfoService.findOne(12).getValue();
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
        if (classify == 4) {
            return payApp(name, generalOrder, money.doubleValue());
        } else {
            CommonInfo one = commonInfoService.findOne(19);
            String returnUrl = one.getValue();
            return payH5(name, generalOrder, money.doubleValue(), returnUrl);
        }
    }

    @Login
    @ApiOperation("支付订单")
    @RequestMapping(value = "/payOrders",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payOrders(String ordersNo,Integer classify, @RequestAttribute Long userId){
        String name = commonInfoService.findOne(12).getValue();
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
        if (classify == 4) {
            return payApp(name, ordersNo, orders.getPayMoney().doubleValue());
        } else {
            CommonInfo one = commonInfoService.findOne(19);
            String returnUrl = one.getValue();
            return payH5(name, ordersNo, orders.getPayMoney().doubleValue(), returnUrl);
        }
    }

    @Login
    @ApiOperation("购买优惠券")
    @RequestMapping(value = "/payCoupon",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payCoupon(Long couponIssueId, Integer classify, @RequestAttribute Long userId){
        String name = commonInfoService.findOne(12).getValue();
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
        if (classify == 4) {
            return payApp(name, ordersNo, Double.parseDouble(selfCoupon.getLessMoney()));
        } else {
            CommonInfo one = commonInfoService.findOne(19);
            String returnUrl = one.getValue();
            return payH5(name, ordersNo, Double.parseDouble(selfCoupon.getLessMoney()), returnUrl);
        }
    }

    @Login
    @ApiOperation("支付订单")
    @RequestMapping(value = "/payMember",method = RequestMethod.POST)
    @Transactional(rollbackFor = Exception.class)
    public Result payMember(Long vipDetailsId,Integer classify, @RequestAttribute Long userId){
        String name = commonInfoService.findOne(12).getValue();
        String ordersNo = getGeneralOrder();
        VipDetails vipDetails = vipDetailsDao.selectById(vipDetailsId);
        PayDetails payDetails=new PayDetails();
        payDetails.setState(0);
        payDetails.setCreateTime(sdf.format(new Date()));
        payDetails.setOrderId(ordersNo);
        payDetails.setUserId(userId);
        payDetails.setMoney(vipDetails.getMoney().doubleValue());
        payDetails.setClassify(classify);
        payDetails.setType(5);
        payDetailsDao.insert(payDetails);
        if (classify == 4) {
            return payApp(name, ordersNo, payDetails.getMoney());
        } else {
            CommonInfo one = commonInfoService.findOne(19);
            String returnUrl = one.getValue();
            return payH5(name, ordersNo, payDetails.getMoney(), returnUrl);
        }
    }

    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    public Result payApp(String name, String generalOrder, Double money) {
        CommonInfo one = commonInfoService.findOne(19);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        String result = "";
        CommonInfo payWay = commonInfoService.findOne(66);
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(67);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);

                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(money +"");
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                //这里和普通的接口调用不同，使用的是sdkExecute
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.error("获取订单失败！");
                }
                return Result.success().put("data", result);
            } else {
                //实例化客户端
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
                AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
                //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
                AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
                model.setBody(name);
                model.setSubject(name);
                model.setOutTradeNo(generalOrder);
                model.setTimeoutExpress("30m");
                model.setTotalAmount(String.valueOf(money));
                model.setProductCode("QUICK_MSECURITY_PAY");
                request.setBizModel(model);
                request.setNotifyUrl(url);
                AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
                if (response.isSuccess()) {
                    result = response.getBody();
                } else {
                    return Result.error("获取订单失败！");
                }
                return Result.success().put("data", result);
            }
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return Result.error(-100, "获取订单失败！");
    }

    public Result payH5(String name, String generalOrder, Double money, String returnUrl) {
        CommonInfo payWay = commonInfoService.findOne(66);
        CommonInfo one = commonInfoService.findOne(19);
        String url = one.getValue() + "/sqx_fast/app/aliPay/notifyApp";
        try {
            if ("1".equals(payWay.getValue())) {
                //构造client
                CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
                //设置网关地址
                certAlipayRequest.setServerUrl("https://openapi.alipay.com/gateway.do");
                //设置应用Id
                certAlipayRequest.setAppId(commonInfoService.findOne(63).getValue());
                //设置应用私钥
                certAlipayRequest.setPrivateKey(commonInfoService.findOne(65).getValue());
                //设置请求格式，固定值json
                certAlipayRequest.setFormat("json");
                //设置字符集
                certAlipayRequest.setCharset(AliPayConstants.CHARSET);
                //设置签名类型
                certAlipayRequest.setSignType(AliPayConstants.SIGNTYPE);
                CommonInfo urls = commonInfoService.findOne(67);
                certAlipayRequest.setCertPath(urls.getValue() + "/appCertPublicKey.crt"); //应用公钥证书路径（app_cert_path 文件绝对路径）
                certAlipayRequest.setAlipayPublicCertPath(urls.getValue() + "/alipayCertPublicKey_RSA2.crt"); //支付宝公钥证书文件路径（alipay_cert_path 文件绝对路径）
                certAlipayRequest.setRootCertPath(urls.getValue() + "/alipayRootCert.crt");  //支付宝CA根证书文件路径（alipay_root_cert_path 文件绝对路径）
                //构造client
                AlipayClient alipayClient = new DefaultAlipayClient(certAlipayRequest);
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money + ""); //金额
                alipayRequest.setBizContent(order.toString());
                alipayRequest.setNotifyUrl(url); //在公共参数中设置回跳和通知地址
                alipayRequest.setReturnUrl(returnUrl); //线上通知页面地址
                String result = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", result);
            } else {
                AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
                AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
                JSONObject order = new JSONObject();
                order.put("out_trade_no", generalOrder); //订单号
                order.put("subject", name); //商品标题
                order.put("product_code", "QUICK_WAP_WAY");
                order.put("body", name);//商品名称
                order.put("total_amount", money); //金额
                alipayRequest.setBizContent(order.toString());
                //在公共参数中设置回跳和通知地址
                alipayRequest.setNotifyUrl(url);
                //通知页面地址
                alipayRequest.setReturnUrl(returnUrl);
                String form = alipayClient.pageExecute(alipayRequest).getBody();
                return Result.success().put("data", form);
            }
        } catch (AlipayApiException e) {
            log.error("CreatPayOrderForH5", e);
        }
        return Result.error("获取订单信息错误！");
    }



    /**
     * 说明： 支付宝订单退款
     *
     * @return 公共返回参数 code,msg,   响应参数实例: https://docs.open.alipay.com/api_1/alipay.trade.refund
     */
    public String alipayRefund(String ordersNo,BigDecimal money) {
        PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(ordersNo);//订单编号
        model.setTradeNo(payDetails.getTradeNo());//支付宝订单交易号
        model.setRefundAmount(money.toString());//退款金额 不得大于订单金额
        model.setRefundReason("取消");//退款说明
        model.setOutRequestNo(ordersNo);//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        alipay_request.setBizModel(model);
        try {
            AlipayTradeRefundResponse alipay_response = alipayClient.execute(alipay_request);
            String alipayRefundStr = alipay_response.getBody();
            log.info(alipayRefundStr);
            return alipayRefundStr;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String activityAlipayRefund(BigDecimal payMoney, String ordersNo, String tradeNo) {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do", commonInfoService.findOne(63).getValue(), commonInfoService.findOne(65).getValue(), "json", AliPayConstants.CHARSET, commonInfoService.findOne(64).getValue(), "RSA2");
        AlipayTradeRefundRequest alipay_request = new AlipayTradeRefundRequest();
        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(ordersNo);//订单编号
        model.setTradeNo(tradeNo);//支付宝订单交易号
        model.setRefundAmount(payMoney.toString());//退款金额 不得大于订单金额
        model.setRefundReason("取消");//退款说明
        model.setOutRequestNo(getGeneralOrder());//标识一次退款请求，同一笔交易多次退款需要保证唯一，如需部分退款，则此参数必传。
        alipay_request.setBizModel(model);
        try {
            AlipayTradeRefundResponse alipay_response = alipayClient.execute(alipay_request);
            String alipayRefundStr = alipay_response.getBody();
            log.info(alipayRefundStr);
            if (StringUtils.isNotBlank(alipayRefundStr)) {
                JSONObject jsonObject = JSON.parseObject(alipayRefundStr);
                JSONObject alipay_trade_refund_response = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code1 = alipay_trade_refund_response.getString("code");
                if ("10000".equals(code1)) {
                    PayDetails payDetails = payDetailsDao.selectByOrderId(ordersNo);
                    payDetails.setState(3);
                    payDetailsDao.updateById(payDetails);
                }
            }
            return alipayRefundStr;
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }



}
