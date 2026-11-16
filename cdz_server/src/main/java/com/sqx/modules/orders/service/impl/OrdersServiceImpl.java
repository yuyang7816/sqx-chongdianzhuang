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
import com.sqx.modules.app.entity.UserVip;
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
import com.sqx.modules.coupon.respository.SelfCouponUserRepository;
import com.sqx.modules.coupon.service.SelfCouponUserService;
import com.sqx.modules.coupon.utils.DateUtil;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import com.sqx.modules.integral.service.UserIntegralDetailsService;
import com.sqx.modules.integral.service.UserIntegralService;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.controller.app.AliPayController;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.tearoom.dao.GoodsTimeDao;
import com.sqx.modules.tearoom.entity.*;
import com.sqx.modules.tearoom.service.*;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.TimeCalculator;
import com.sqx.modules.utils.excel.ExcelData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {

    @Autowired
    private SelfCouponUserJpaRepository couponUserJpaRepository;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private AliPayController aliPayController;
    @Autowired
    private WxService wxService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private UserIntegralDetailsService userIntegralDetailsService;
    @Autowired
    private GoodsTimeDao goodsTimeDao;
    @Autowired
    private RoomTimeService roomTimeService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private SelfCouponUserRepository selfCouponUserRepository;

    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Override
    public Result selectOrdersMoney(Long userId, Long goodsId, Integer hour) {
        // 1. 获取商品基础信息并校验
        Goods goods = goodsService.getById(goodsId);
        if (goods == null) {
            return Result.error("商品不存在！");
        }

        // 2. 确定起始时间和结束时间
        Date now = new Date();
        Calendar startCal = Calendar.getInstance();
        startCal.setTime(now); // 下单起始时间
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(now);
        endCal.add(Calendar.HOUR, hour); // 下单结束时间

        // 校验时间有效性
        if (endCal.before(startCal) || endCal.equals(startCal)) {
            return Result.error("请选择正确的时间！");
        }

        // 3. 一次性查询用户VIP信息（优化性能，避免重复查询）
        boolean isVip = false;
        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id", userId));
        if (userVip != null && userVip.getIsVip() == 1) {
            isVip = true;
        }

        // 4. 初始化核心变量
        BigDecimal totalPayMoney = BigDecimal.ZERO;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        List<Map<String, Object>> priceList = new ArrayList<>();
        Calendar currentCal = (Calendar) startCal.clone(); // 当前遍历的起始时间
        Calendar tempEndCal = Calendar.getInstance(); // 临时存储当前价格时段的结束时间

        // 5. 循环遍历：直到当前时间超过下单结束时间（按价格时段循环，非分钟）
        while (currentCal.before(endCal)) {
            // 5.1 获取当前时间的日期和时段（用于查询对应小时价）
            String currentDate = DateUtils.format(currentCal.getTime(), "yyyy-MM-dd");
            int currentHour = currentCal.get(Calendar.HOUR_OF_DAY);
            String currentHourStr = currentHour < 10 ? "0" + currentHour : String.valueOf(currentHour);
            // 拼接当前时间的时段（格式：HH:mm，适配原有SQL查询规则）
            int currentMinute = currentCal.get(Calendar.MINUTE);
            String currentMinuteStr = currentMinute < 10 ? "0" + currentMinute : String.valueOf(currentMinute);
            String currentTimeSlot = currentHourStr + ":" + currentMinuteStr;

            // 5.2 查询当前时间对应的小时价（保留原有优先级：日期规则→星期规则→商品默认，弃用分钟价）
            BigDecimal hourPrice; // 仅保留小时价，作为唯一计费基准
            GoodsTime goodsTimeByDate = goodsTimeDao.selectGoodsTimeByRuleType(currentTimeSlot, goods.getTearoomId());

            if (goodsTimeByDate != null) {
                // 优先按具体日期规则获取小时价
                hourPrice = isVip ? goodsTimeByDate.getMemberHourPrice() : goodsTimeByDate.getHourPrice();
            } else {
                hourPrice = goods.getHourPrice();
            }

            // 5.3 确定当前价格时段的结束时间（关键：处理跨时段场景）
            tempEndCal = (Calendar) currentCal.clone();
            // 先尝试设置为当前小时的下一个整点（价格时段边界，如07:23 → 08:00）
            tempEndCal.set(Calendar.MINUTE, 0);
            tempEndCal.add(Calendar.HOUR, 1);
            // 若当前价格时段的整点结束时间超过下单结束时间，则以下单结束时间为准
            if (tempEndCal.after(endCal)) {
                tempEndCal = (Calendar) endCal.clone();
            }

            // 5.4 计算当前价格时段内的实际计费分钟数
            long timeDiffMillis = tempEndCal.getTimeInMillis() - currentCal.getTimeInMillis();
            int actualMinutes = (int) (timeDiffMillis / (1000 * 60));
            if (actualMinutes <= 0) {
                break; // 无有效分钟数，退出循环
            }

            // 5.5 核心：按小时价折算实际分钟数的费用（解决除不尽误差）
            // 公式：时段总费用 = 小时价 × 实际分钟数 ÷ 60
            // 先乘后除，保留足够精度，最终保留2位小数（人民币分单位）
            BigDecimal slotTotalPrice = hourPrice
                    .multiply(new BigDecimal(actualMinutes)) // 先乘实际分钟数，避免精度丢失
                    .divide(new BigDecimal(60), 10, RoundingMode.HALF_UP) // 除以60，先保留10位精度
                    .setScale(2, RoundingMode.HALF_UP); // 最终保留2位小数，解决除不尽问题

            // 5.6 累加总费用
            totalPayMoney = totalPayMoney.add(slotTotalPrice).setScale(2, RoundingMode.HALF_UP);

            // 5.7 构建当前价格时段的明细（仅返回小时价，不返回分钟价，适配前端展示）
            Map<String, Object> priceDetail = new HashMap<>();
            priceDetail.put("hourPrice", hourPrice); // 前端展示的小时价
            priceDetail.put("actualMinutes", actualMinutes); // 当前时段的实际计费分钟数
            priceDetail.put("slotTotalPrice", slotTotalPrice); // 当前时段的总费用
            priceDetail.put("startTime", DateUtils.format(currentCal.getTime(), "HH:mm")); // 当前时段起始时间
            priceDetail.put("endTime", DateUtils.format(tempEndCal.getTime(), "HH:mm")); // 当前时段结束时间
            priceList.add(priceDetail);

            // 5.8 推进当前遍历时间到当前价格时段的结束时间，继续下一个时段
            currentCal = (Calendar) tempEndCal.clone();
        }

        // 6. 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("payMoney", totalPayMoney);
        result.put("priceList", priceList); // 各价格时段明细（仅含小时价，无分钟价）
        return Result.success(result);
    }


    @Override
    public Result insertOrders(Orders orders) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            Goods goods = goodsService.getById(orders.getGoodsId());
            if (goods.getStatus() != 1) {
                return Result.error("请更换其他充电桩！");
            }
            if (goods.getGoodsStatus() != 1) {
                return Result.error("当前充电桩使用中，请更换其他充电桩！");
            }

            Orders yuOrders = baseMapper.selectOne(new QueryWrapper<Orders>()
                    .eq("status", 3).eq("user_id", orders.getUserId()));
            if (yuOrders != null) {
                return Result.error("当前有正在进行的订单！");
            }

            orders.setGoodsCategory(goods.getGoodsCategory());
            orders.setGoodsName(goods.getGoodsName());
            orders.setGoodsCoverImage(goods.getGoodsCoverImage());
            orders.setCashPledge(goods.getCashPledge());

            if (orders.getOrdersType() == 1 || orders.getOrdersType() == 2) {
                // 初始化订单开始时间
                orders.setStartTime(DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                // 处理订单结束时间（无传入时自动计算）
                if (StringUtils.isEmpty(orders.getEndTime())) {
                    Date d1 = df.parse(orders.getStartTime());
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(d1);
                    calendar.add(Calendar.HOUR, orders.getHour().intValue());
                    orders.setEndTime(df.format(calendar.getTime()));
                }


                // 解析订单开始时间和结束时间，转换为Calendar
                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = df.parse(orders.getStartTime());
                    d2 = df.parse(orders.getEndTime() );
                    if (orders.getEndTime().contains("23:59")) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(d2);
                        calendar.add(Calendar.MINUTE, 1);
                        d2 = calendar.getTime();
                    }
                } catch (Exception e) {
                    log.error("时间出错：" + e.getMessage(), e);
                    return Result.error("请重新选择时间！");
                }
                Calendar startCal = Calendar.getInstance();
                startCal.setTime(d1);
                Calendar endCal = Calendar.getInstance();
                endCal.setTime(d2);

                // 校验时间有效性
                if (endCal.before(startCal) || endCal.equals(startCal)) {
                    return Result.error("请选择正确的时间！");
                }

                // 一次性查询用户VIP信息（优化性能，移出循环）
                boolean isVip = false;
                UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id", orders.getUserId()));
                if (userVip != null && userVip.getIsVip() == 1) {
                    isVip = true;
                }

                // 核心：按小时价精准计算总费用（替换原有30分钟时段拆分逻辑）
                BigDecimal totalPayMoney = BigDecimal.ZERO;
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                Calendar currentCal = (Calendar) startCal.clone();
                Calendar tempEndCal = Calendar.getInstance();

                // 按价格时段循环计费（非分钟/30分钟时段，SQL查询次数=价格时段数）
                while (currentCal.before(endCal)) {
                    // 获取当前时间的日期和时段（适配原有SQL查询规则）
                    String currentDate = DateUtils.format(currentCal.getTime(), "yyyy-MM-dd");
                    int currentHour = currentCal.get(Calendar.HOUR_OF_DAY);
                    String currentHourStr = currentHour < 10 ? "0" + currentHour : String.valueOf(currentHour);
                    int currentMinute = currentCal.get(Calendar.MINUTE);
                    String currentMinuteStr = currentMinute < 10 ? "0" + currentMinute : String.valueOf(currentMinute);
                    String currentTimeSlot = currentHourStr + ":" + currentMinuteStr;

                    // 查询当前时段的小时价（保留原有优先级：日期规则→星期规则→商品默认，弃用分钟价）
                    BigDecimal hourPrice;
                    GoodsTime goodsTimeByDate = goodsTimeDao.selectGoodsTimeByRuleType(currentTimeSlot, orders.getTearoomId());

                    if (goodsTimeByDate != null) {
                        // 优先按具体日期规则获取小时价
                        hourPrice = isVip ? goodsTimeByDate.getMemberHourPrice() : goodsTimeByDate.getHourPrice();
                    } else {
                        hourPrice = goods.getHourPrice();
                    }

                    // 确定当前价格时段的结束时间（处理跨时段场景，优先到整点）
                    tempEndCal = (Calendar) currentCal.clone();
                    tempEndCal.set(Calendar.MINUTE, 0);
                    tempEndCal.add(Calendar.HOUR, 1);
                    // 若整点结束时间超过订单结束时间，以下单结束时间为准
                    if (tempEndCal.after(endCal)) {
                        tempEndCal = (Calendar) endCal.clone();
                    }

                    // 计算当前价格时段内的实际计费分钟数
                    long timeDiffMillis = tempEndCal.getTimeInMillis() - currentCal.getTimeInMillis();
                    int actualMinutes = (int) (timeDiffMillis / (1000 * 60));
                    if (actualMinutes <= 0) {
                        break;
                    }

                    // 核心折算公式：时段费用=小时价×实际分钟数÷60（解决除不尽误差，先乘后除）
                    BigDecimal slotTotalPrice = hourPrice
                            .multiply(new BigDecimal(actualMinutes))
                            .divide(new BigDecimal(60), 10, RoundingMode.HALF_UP)
                            .setScale(2, RoundingMode.HALF_UP);

                    // 累加总费用
                    totalPayMoney = totalPayMoney.add(slotTotalPrice).setScale(2, RoundingMode.HALF_UP);

                    // 推进当前遍历时间到当前价格时段结束时间
                    currentCal = (Calendar) tempEndCal.clone();
                }
                BigDecimal payMoney = BigDecimal.ZERO;
                // 原有订单基础信息赋值（保留业务规则）
                orders.setGoodsName(goods.getGoodsName());
                orders.setGoodsCoverImage(goods.getGoodsCoverImage());
                orders.setCashPledge(goods.getCashPledge());
                orders.setSumMoney(goods.getCashPledge());
                orders.setOldPayMoney(totalPayMoney);
                orders.setMinuteNum(0);
                orders.setMinuteMoney(BigDecimal.ZERO);


                // 优惠券处理逻辑
                if (orders.getCouponId() != null) {
                    SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                    if (couponUser == null) {
                        return Result.error("优惠券不存在！");
                    } else if (couponUser.getStatus() != 1) {
                        return Result.error("优惠券已失效！");
                    }

                    // 计算订单总时段数（兼容原有优惠券时长校验逻辑）
                    long periodDiffMinutes = (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60);
                    int periodCount = (int) (periodDiffMinutes / 30);

                    if (StringUtils.isNotBlank(couponUser.getGoodsIds())) {
                        boolean flag = false;
                        for (String goodsId : couponUser.getGoodsIds().split(",")) {
                            if (goodsId.equals(String.valueOf(orders.getGoodsId()))) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            return Result.error("当前优惠券不支持：" + goods.getGoodsName());
                        }
                    }
                    if (couponUser.getIsLimitTime() != null && couponUser.getIsLimitTime() == 1) {
                        boolean flag = false;
                        LocalDate today = LocalDate.now();
                        DayOfWeek dayOfWeek = today.getDayOfWeek();
                        for (String week : couponUser.getWeeks().split(",")) {
                            if (Integer.parseInt(week) == dayOfWeek.getValue()) {
                                flag = true;
                                break;
                            }
                        }
                        if (!flag) {
                            return Result.error("当前优惠券须在指定星期内使用");
                        }
                        flag = false;
                        LocalTime now = LocalTime.now();
                        for (String time : couponUser.getTimes().split(",")) {
                            String[] time1 = time.split("-");
                            String[] statTime = time1[0].split(":");
                            String[] endTime = time1[1].split(":");
                            LocalTime start = LocalTime.of(Integer.parseInt(statTime[0]), Integer.parseInt(statTime[1]));
                            LocalTime end = LocalTime.of(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));

                            DateTimeFormatter formatters = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            LocalDateTime startTimeLocalDate = LocalDateTime.parse(orders.getStartTime(), formatters);
                            LocalTime startTimeLocal = startTimeLocalDate.toLocalTime();
                            LocalDateTime endTimeLocalDate = LocalDateTime.parse(orders.getEndTime(), formatters);
                            LocalTime endTimeLocal = endTimeLocalDate.toLocalTime();

                            flag = (startTimeLocal.isAfter(start) || startTimeLocal.equals(start))
                                    && (endTimeLocal.isBefore(end) || endTimeLocal.equals(end));
                            if (flag) {
                                break;
                            }
                        }
                        if (!flag) {
                            return Result.error("当前优惠券须在" + couponUser.getTimes() + "时间内使用");
                        }
                    }

                    couponUser.setStatus(2);
                    couponUser.setShopTearoomId(orders.getTearoomId());
                    couponUserJpaRepository.save(couponUser);
                    orders.setCouponName(couponUser.getCouponName());
                    orders.setCouponMoney(new BigDecimal(couponUser.getLessMoney()));

                    orders.setPlatformType(couponUser.getPlatformType());
                    orders.setStatus(1);
                    orders.setPayTime(DateUtils.format(new Date()));
                    payMoney = payMoney.add(totalPayMoney);
                    payMoney=payMoney.subtract(new BigDecimal(couponUser.getLessMoney()));
                    if (startCal.get(Calendar.MINUTE) != 0 && startCal.get(Calendar.MINUTE) != 30) {
                        orders.setHour(Double.parseDouble(String.valueOf(periodCount - 1)));
                    } else {
                        orders.setHour(Double.parseDouble(String.valueOf(periodCount)));
                    }
                } else {
                    // 无优惠券时，叠加商品费用（总费用=押金（按需）+ 小时价折算费用）
                    payMoney = payMoney.add(totalPayMoney);

                    // 订单时长赋值
                    long periodDiffMinutes = (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60);
                    int periodCount = (int) (periodDiffMinutes / 30);
                    if (startCal.get(Calendar.MINUTE) != 0 && startCal.get(Calendar.MINUTE) != 30) {
                        orders.setHour(Double.parseDouble(String.valueOf(periodCount - 1)));
                    } else {
                        orders.setHour(Double.parseDouble(String.valueOf(periodCount)));
                    }
                    orders.setStatus(1);
                }

                // 订单支付金额及其他信息赋值
                orders.setPayMoney(payMoney);
                orders.setOnePayMoney(payMoney);
                orders.setCreateTime(DateUtils.format(new Date()));
                orders.setOrdersNo(getGeneralOrder());

                // 用户信息及返利逻辑
                if (orders.getUserId() != null) {
                    UserEntity userEntity = userService.selectUserById(orders.getUserId());
                    orders.setPhone(userEntity.getPhone());

                    UserEntity oneUser = userService.queryByInvitationCode(userEntity.getInviterCode());
                    if (payMoney.doubleValue() > 0) {
                        if (oneUser != null) {
                            if (oneUser.getZhiRate().doubleValue() > 0) {
                                BigDecimal zhiMoney = orders.getPayMoney().multiply(oneUser.getZhiRate());
                                orders.setZhiMoney(zhiMoney);
                                orders.setZhiUserId(oneUser.getUserId());
                            }
                            UserEntity towUser = userService.queryByInvitationCode(oneUser.getInviterCode());
                            if (towUser != null) {
                                if (towUser.getFeiRate().doubleValue() > 0) {
                                    BigDecimal feiMoney = orders.getPayMoney().multiply(towUser.getFeiRate());
                                    orders.setFeiMoney(feiMoney);
                                    orders.setFeiUserId(towUser.getUserId());
                                }
                            }
                        }
                    }
                }

                // 插入订单
                baseMapper.insert(orders);

                return Result.success().put("data", orders);
            } /*else {
                // 非ordersType=1的逻辑，按原有代码保留（无需处理）
                UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(orders.getUserId());
                if (userMoney.getMoney().doubleValue() <= 0) {
                    return Result.error("请先充值");
                }

                orders.setSumMoney(goods.getCashPledge());
                orders.setMinuteNum(0);
                orders.setMinuteMoney(BigDecimal.ZERO);
                orders.setStatus(3);
                orders.setPayMoney(BigDecimal.ZERO);
                orders.setCreateTime(DateUtils.format(new Date()));
                orders.setOrdersNo(getGeneralOrder());
                UserEntity userEntity = userService.selectUserById(orders.getUserId());
                orders.setPhone(userEntity.getPhone());
                baseMapper.insert(orders);
                goods.setGoodsStatus(2);
                goods.setOrdersId(orders.getOrdersId());
                goodsService.updateById(goods);

                return Result.success().put("data", orders);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
            log.error("下单报错：" + e.getMessage(), e);
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }

    @Override
    public Result selectOrdersByUserId(Long userId){
        Orders orders = baseMapper.selectOne(new QueryWrapper<Orders>()
                .in("status", 3).eq("user_id",userId).in("goods_category",1,2));
        if(orders!=null && orders.getGoodsId()!=null){
            orders.setGoods(goodsService.selectGoodsById(orders.getGoodsId()));
        }
        return Result.success().put("data",orders);
    }


    public String getGeneralOrder() {
        Date date = new Date();
        String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 1000));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = sdf.format(date);
        return format + newString;
    }

    @Override
    public Result payOrders(String ordersNo){
        log.info("订单号："+ordersNo);
        Orders orders = selectOrdersByNo(ordersNo);
        if(orders.getStatus()==2 || orders.getStatus()==3){
            return Result.success().put("data",orders);
        }
        if(!orders.getStatus().equals(1)){
            return Result.error("订单状态异常，刷新页面后重试！");
        }

        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(orders.getUserId());
        if(userMoney.getMoney().doubleValue()<orders.getPayMoney().doubleValue()){
            return Result.error("余额不足！");
        }
        String createTime = DateUtils.format(new Date());
        Goods goods = goodsService.getById(orders.getGoodsId());
        if(orders.getOrdersType()==1 || orders.getOrdersType()==2){
            userMoneyService.updateMoney(2,orders.getUserId(),orders.getPayMoney().doubleValue());
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(orders.getPayMoney());
            userMoneyDetails.setUserId(orders.getUserId());
            userMoneyDetails.setContent("余额支付购买："+goods.getGoodsName());
            userMoneyDetails.setTitle("支付订单");
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(7);
            userMoneyDetails.setCreateTime(createTime);
            userMoneyDetailsService.save(userMoneyDetails);
            orders.setStatus(3);

            List<Room> lightRoom = roomService.list(new QueryWrapper<Room>().eq("goods_id",orders.getGoodsId()));
            for (Room room:lightRoom){
                try{
                    roomService.issueInstruct(room.getRoomId(), 1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
        orders.setPayTime(createTime);
        orders.setPayWay(3);
        baseMapper.updateById(orders);
        goods.setGoodsStatus(2);
        goods.setOrdersId(orders.getOrdersId());
        goodsService.updateById(goods);
        //sendPayOrdersMsg(orders);
        return Result.success();
    }



    @Override
    public void sendPayOrdersMsg(Orders orders){
        UserEntity userEntity = userService.selectUserById(orders.getUserId());
        String templateId = commonInfoService.findOne(818).getValue();
        List<String> msgList=new ArrayList<>();
        msgList.add(orders.getOrdersNo());
        msgList.add(String.valueOf(orders.getPayMoney()));
        msgList.add(orders.getGoodsName());
        msgList.add(orders.getCreateTime());
        if(StringUtils.isNotEmpty(userEntity.getOpenId())){
            SenInfoCheckUtil.sendMsg(userEntity.getOpenId(),templateId,msgList,1);
        }
    }


    @Override
    public Orders selectOrdersByNo(String ordersNo){
        log.info("订单号："+ordersNo);
        Orders orders = baseMapper.selectOne(new QueryWrapper<Orders>().eq("orders_no", ordersNo));
        orders.setTearoom(tearoomService.getById(orders.getTearoomId()));
        orders.setGoods(goodsService.getById(orders.getGoodsId()));
        return orders;
    }

    @Override
    public Orders selectOrdersDetailsVip(String ordersNo){
        Orders orders = baseMapper.selectOne(new QueryWrapper<Orders>().eq("orders_no", ordersNo));
        orders.setTearoom(tearoomService.getById(orders.getTearoomId()));
        orders.setGoods(goodsService.getById(orders.getGoodsId()));
        if(orders.getOpenDoor()==null || orders.getOpenDoor()!=1){
            if(orders.getGoods()!=null){
                orders.getGoods().setRoomWifi(null);
                orders.getGoods().setRoomPwd(null);
                orders.getGoods().setRoomNo(null);
            }
        }
        if(orders.getCouponId()!=null){
            orders.setCouponUser(couponUserJpaRepository.findById(orders.getCouponId()).orElse(null));
        }
        if(orders.getOrdersType()==1){
            Long startTime = null;
            // 获取两个 Date 对象的时间戳（毫秒）
            if(StringUtils.isNotBlank(orders.getPayTime())){
                startTime = DateUtils.stringToDate(orders.getPayTime(),DateUtils.DATE_TIME_PATTERN).getTime();
            }else{
                startTime = DateUtils.stringToDate(orders.getCreateTime(),DateUtils.DATE_TIME_PATTERN).getTime();
            }

            Long endTime = null;
            if(StringUtils.isNotBlank(orders.getEndTime())){
                endTime = DateUtils.stringToDate(orders.getEndTime(),DateUtils.DATE_TIME_PATTERN).getTime();
            }else{
                endTime = System.currentTimeMillis();
            }

            // 计算时间差（毫秒）
            long diffInMillis = Math.abs(endTime - startTime);

            // 将时间差转换为分钟数，不足一分钟按一分钟算
            int diffInMinutes = (int) Math.ceil((double) diffInMillis / (1000 * 60));

            BigDecimal payMinutePrice = orders.getMinutePrice().multiply(BigDecimal.valueOf(diffInMinutes));
            orders.setPayMinuteMoney(payMinutePrice);
        }else{
            orders.setPayMinuteMoney(BigDecimal.ZERO);
        }
        return orders;
    }

    @Override
    public Result selectOrdersList(Integer page,Integer limit,Long userId,String userName,String phone,
                                   String ordersNo,String goodsName,Integer status,Long tearoomId,
                                   String startTime,String endTime,Integer isDelete,Integer payWay,Integer money,
                                   Integer isShop,String dateTime,String cameraId,Integer goodsCategory,String tearoomName,
                                   Integer platformType,Long openUserId,Integer ordersType){
        IPage<Orders> ordersIPage = baseMapper.selectOrdersPage(new Page<>(page, limit), userId, userName, phone, ordersNo,
                goodsName, status, tearoomId, startTime, endTime, isDelete,payWay,money,isShop,dateTime,cameraId,goodsCategory,
                tearoomName,platformType,openUserId,ordersType);
        List<Orders> records = ordersIPage.getRecords();
        if(isDelete==null) {
            for (Orders orders : records) {
                orders.setGoods(goodsService.getById(orders.getGoodsId()));
                orders.setTearoom(tearoomService.getById(orders.getTearoomId()));
            }
        }
        return Result.success().put("data",new PageUtils(ordersIPage));
    }

    @Override
    public ExcelData excelOrders(Long userId,String userName,String phone,
                                 String ordersNo,String goodsName,Integer status,Long tearoomId,
                                 String startTime,String endTime,Integer isDelete,Integer payWay,Integer money,
                                 Integer isShop,String dateTime,Integer goodsCategory,String tearoomName,
                                 Integer platformType,Long openUserId,Integer ordersType) {
        List<Orders> ordersList = baseMapper.selectOrdersList( userId, userName, phone, ordersNo,
                goodsName, status, tearoomId, startTime, endTime, isDelete,payWay,money,isShop,dateTime,
                goodsCategory,tearoomName,platformType,openUserId,ordersType);
        if(isDelete==null){
            for (Orders orders:ordersList){
                orders.setGoods(goodsService.getById(orders.getGoodsId()));
                orders.setTearoom(tearoomService.getById(orders.getTearoomId()));
            }
        }
        ExcelData data = new ExcelData();
        data.setName("订单列表");
        List<String> titles = new ArrayList();
        titles.add("编号");titles.add("台球馆");titles.add("台球桌名称");
        titles.add("订单号"); titles.add("订单类型");
        titles.add("用户名称");titles.add("手机号");titles.add("每小时价格");titles.add("开台小时数");titles.add("优惠券名称");titles.add("订单金额");
        titles.add("超出时间(分钟)");titles.add("超出费用");titles.add("押金");titles.add("商家总收益");
        titles.add("支付方式");titles.add("创建时间");titles.add("付款时间");titles.add("状态");

        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for(Orders orders:ordersList){
            List<Object> row = new ArrayList();
            row.add(orders.getOrdersId());row.add(orders.getTearoomName());row.add(orders.getGoodsName());
            row.add(orders.getOrdersNo());
            if(orders.getOrdersType()==1){
                row.add("开台");
            }else{
                row.add("预约");
            }
            row.add(orders.getPayUserName());row.add(orders.getPayPhone());row.add(orders.getHourMoney());row.add(orders.getHour());
            row.add(StringUtils.isNotBlank(orders.getCouponName())?orders.getCouponName():"");
            row.add(orders.getPayMoney());
            row.add(orders.getMinuteNum());row.add(orders.getMinuteMoney());row.add(orders.getCashPledge());row.add(orders.getShopMoney());
            if(orders.getPayWay()!=null && orders.getPayWay()==1){
                row.add("微信");
            }else if(orders.getPayWay()!=null && orders.getPayWay()==2){
                row.add("支付宝");
            }else if(orders.getPayWay()!=null && orders.getPayWay()==3){
                row.add("零钱");
            }else{
                row.add("-");
            }
            row.add(orders.getCreateTime());row.add(orders.getPayTime());
            //态 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价
            if(orders.getStatus()==1){
                row.add("待支付");
            }else if(orders.getStatus()==2){
                row.add("待使用");
            }else if(orders.getStatus()==3){
                row.add("使用中");
            }else if(orders.getStatus()==4){
                row.add("已完成");
            }else if(orders.getStatus()==5){
                row.add("已取消");
            }else if(orders.getStatus()==6){
                row.add("待评价");
            }else{
                row.add("-");
            }
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }


    @Override
    public Result cancelOrders(String ordersNo){
        Orders orders = selectOrdersByNo(ordersNo);
        if(orders.getStatus().equals(5)){
            return Result.error("订单已取消！");
        }else if(orders.getStatus().equals(1)){
            orders.setStatus(5);
            baseMapper.updateById(orders);
            if(orders.getCouponId()!=null){
                SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                if(couponUser!=null){
                    couponUser.setStatus(1);
                    couponUserJpaRepository.save(couponUser);
                }
            }
            if(StringUtils.isNotBlank(orders.getCouponIds())){
                for (String couponId:orders.getCouponIds().split(",")){
                    SelfCouponUser couponUser = couponUserJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                    if(couponUser!=null){
                        couponUser.setStatus(1);
                        couponUserJpaRepository.save(couponUser);
                    }
                }
            }
            Goods goods = goodsService.selectGoodsById(orders.getGoodsId());
            goods.setGoodsStatus(1);
            goodsService.updateById(goods);
            return Result.success();
        }else if(orders.getStatus().equals(4)){
            return Result.error("订单已结束，不可退款！");
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());

        //订单类型 1包间 2柜子商品  3预约
        BigDecimal payMoney=BigDecimal.ZERO;
        if(orders.getOrdersType()==1){
            //查询包间当前
            //当前开台后不支持取消 只支持结束订单
            return Result.error("请点击结束订单！");

        }else if(orders.getOrdersType()==2){
            payMoney=orders.getPayMoney();
        }else{
            payMoney=orders.getPayMoney();
            //预约费 判断是否超过3分钟（配置） 未超过则可退款
            long now = System.currentTimeMillis();
            Date date = DateUtils.stringToDate(orders.getPayTime(), DateUtils.DATE_TIME_PATTERN);
//            long payTime = date.getTime();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE,Integer.parseInt(commonInfoService.findOne(835).getValue()));
            long payTime = calendar.getTime().getTime();
            if(payTime<now){
                //如果大于 取消不退
                orders.setStatus(4);
                orders.setIsSubscription(2);
                orders.setShopMoney(orders.getPayMoney());
                baseMapper.updateById(orders);
                Goods goods = goodsService.selectGoodsById(orders.getGoodsId());
                goods.setGoodsStatus(1);
                goodsService.updateById(goods);

                //不退则吧钱给商家
                Tearoom tearoom = tearoomService.getById(orders.getTearoomId());
                userMoneyService.updateShopMoney(1,tearoom.getUserId(),orders.getPayMoney().doubleValue());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(orders.getPayMoney());
                userMoneyDetails.setShopUserId(tearoom.getUserId());
                userMoneyDetails.setContent("到账收益："+orders.getPayMoney());
                userMoneyDetails.setTitle("预约订单超时取消："+orders.getOrdersNo());
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(5);
                userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);

                return Result.success();
            }
        }
        Goods goods = goodsService.selectGoodsById(orders.getGoodsId());
        goods.setGoodsStatus(1);
        goodsService.updateById(goods);
        orders.setIsSubscription(1);
        orders.setShopMoney(BigDecimal.ZERO);
        if(orders.getPayWay()==1){
            boolean refund = wxService.refund(orders.getOrdersNo(),  payMoney, payMoney);
            if(!refund){
                return Result.error("退款失败，请联系客服！");
            }
        }else if(orders.getPayWay()==2){
            String data = aliPayController.alipayRefund(orders.getOrdersNo(), payMoney);
            if(StringUtils.isNotBlank(data)){
                log.error(data);
                JSONObject jsonObject = JSON.parseObject(data);
                JSONObject alipay_trade_refund_response = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code1 = alipay_trade_refund_response.getString("code");
                if(!"10000".equals(code1)){
                    return Result.error("退款失败！"+alipay_trade_refund_response.getString("sub_msg"));
                }
            }else{
                return Result.error("退款失败！");
            }
        }else{
            userMoneyService.updateMoney(1,orders.getUserId(),payMoney.doubleValue());
        }
        UserMoneyDetails userMoneyDetails =new UserMoneyDetails();
        userMoneyDetails.setMoney(payMoney);
        userMoneyDetails.setUserId(orders.getUserId());
        userMoneyDetails.setContent("订单号："+orders.getOrdersNo()+",已原路退款！");
        userMoneyDetails.setTitle("订单退款");
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(time);
        userMoneyDetailsService.save(userMoneyDetails);
        orders.setStatus(5);
        orders.setOpenDoor(2);
        baseMapper.updateById(orders);
        if(orders.getCouponId()!=null){
            SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
            if(couponUser!=null){
                couponUser.setStatus(1);
                couponUserJpaRepository.save(couponUser);
            }
        }
        if(StringUtils.isNotBlank(orders.getCouponIds())){
            for (String couponId:orders.getCouponIds().split(",")){
                SelfCouponUser couponUser = couponUserJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                if(couponUser!=null){
                    couponUser.setStatus(1);
                    couponUserJpaRepository.save(couponUser);
                }
            }
        }
        //cancelOrdersMsg(orders,1);
        return Result.success();
    }


    @Override
    public Result refundOrders(String ordersNo){
        Orders orders = selectOrdersByNo(ordersNo);
        if(orders.getStatus().equals(5)){
            return Result.error("订单已取消！");
        }else if(orders.getStatus().equals(1)){
            orders.setStatus(5);
            baseMapper.updateById(orders);
            if(orders.getCouponId()!=null){
                SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                if(couponUser!=null){
                    couponUser.setStatus(1);
                    couponUserJpaRepository.save(couponUser);
                }
            }
            if(StringUtils.isNotBlank(orders.getCouponIds())){
                for (String couponId:orders.getCouponIds().split(",")){
                    SelfCouponUser couponUser = couponUserJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                    if(couponUser!=null){
                        couponUser.setStatus(1);
                        couponUserJpaRepository.save(couponUser);
                    }
                }
            }
            Goods goods = goodsService.selectGoodsById(orders.getGoodsId());
            goods.setGoodsStatus(1);
            goodsService.updateById(goods);
            return Result.success();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());

        //订单类型 1包间 2柜子商品
        BigDecimal payMoney=BigDecimal.ZERO;
        payMoney=orders.getOnePayMoney();

        if(orders.getPayWay()==1){
            boolean refund = wxService.refund(orders.getOrdersNo(),  payMoney, payMoney);
            if(!refund){
                return Result.error("退款失败，请联系客服！");
            }
        }else if(orders.getPayWay()==2){
            String data = aliPayController.alipayRefund(orders.getOrdersNo(), payMoney);
            if(StringUtils.isNotBlank(data)){
                log.error(data);
                JSONObject jsonObject = JSON.parseObject(data);
                JSONObject alipay_trade_refund_response = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code1 = alipay_trade_refund_response.getString("code");
                if(!"10000".equals(code1)){
                    return Result.error("退款失败！"+alipay_trade_refund_response.getString("sub_msg"));
                }
            }else{
                return Result.error("退款失败！");
            }
        }else{
            userMoneyService.updateMoney(1,orders.getUserId(),payMoney.doubleValue());
        }
//        roomTimeService.deleteRoomTimeByOrdersNo(ordersNo);
        UserMoneyDetails userMoneyDetails =new UserMoneyDetails();
        userMoneyDetails.setMoney(payMoney);
        userMoneyDetails.setUserId(orders.getUserId());
        userMoneyDetails.setContent("订单号："+orders.getOrdersNo()+",已原路退款！");
        userMoneyDetails.setTitle("订单退款");
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(time);
        userMoneyDetailsService.save(userMoneyDetails);
        Goods goods = goodsService.selectGoodsById(orders.getGoodsId());
        goods.setGoodsStatus(1);
        goodsService.updateById(goods);
        orders.setIsSubscription(1);
        orders.setShopMoney(BigDecimal.ZERO);
        orders.setStatus(5);
        baseMapper.updateById(orders);
        if(orders.getCouponId()!=null){
            SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
            if(couponUser!=null){
                couponUser.setStatus(1);
                couponUserJpaRepository.save(couponUser);
            }
        }
        if(StringUtils.isNotBlank(orders.getCouponIds())){
            for (String couponId:orders.getCouponIds().split(",")){
                SelfCouponUser couponUser = couponUserJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                if(couponUser!=null){
                    couponUser.setStatus(1);
                    couponUserJpaRepository.save(couponUser);
                }
            }
        }
//        cancelOrdersMsg(orders,2);
        return Result.success();
    }

    @Override
    public Result refundOrdersMoney(String ordersNo,BigDecimal refundMoney,String refundRemark){
        Orders orders = selectOrdersByNo(ordersNo);
        if(orders.getStatus().equals(5)){
            return Result.error("订单已取消！");
        }else if(orders.getStatus().equals(1)){
            orders.setStatus(5);
            baseMapper.updateById(orders);
            if(orders.getCouponId()!=null){
                SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                if(couponUser!=null){
                    couponUser.setStatus(1);
                    couponUserJpaRepository.save(couponUser);
                }
            }
            if(StringUtils.isNotBlank(orders.getCouponIds())){
                for (String couponId:orders.getCouponIds().split(",")){
                    SelfCouponUser couponUser = couponUserJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                    if(couponUser!=null){
                        couponUser.setStatus(1);
                        couponUserJpaRepository.save(couponUser);
                    }
                }
            }
            Goods goods = goodsService.selectGoodsById(orders.getGoodsId());
            goods.setGoodsStatus(1);
            goodsService.updateById(goods);
            return Result.success();
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = simpleDateFormat.format(new Date());
        if(orders.getUserId()==null){
            return Result.error("订单非用户开台");
        }
        BigDecimal sumRefundMoney=refundMoney;
        if(orders.getRefundMoney()!=null){
            sumRefundMoney=sumRefundMoney.add((orders.getRefundMoney()));
        }
        if(sumRefundMoney.doubleValue()>orders.getPayMoney().doubleValue()){
            return Result.error("已超出支付金额");
        }
        if(orders.getPayWay()==1){
            boolean refund = wxService.refund(orders.getOrdersNo(),  orders.getOnePayMoney(), refundMoney);
            if(!refund){
                return Result.error("退款失败，请联系客服！");
            }
        }else if(orders.getPayWay()==2){
            String data = aliPayController.alipayRefund(orders.getOrdersNo(), refundMoney);
            if(StringUtils.isNotBlank(data)){
                log.error(data);
                JSONObject jsonObject = JSON.parseObject(data);
                JSONObject alipay_trade_refund_response = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code1 = alipay_trade_refund_response.getString("code");
                if(!"10000".equals(code1)){
                    return Result.error("退款失败！"+alipay_trade_refund_response.getString("sub_msg"));
                }
            }else{
                return Result.error("退款失败！");
            }
        }else{
            userMoneyService.updateMoney(1,orders.getUserId(),refundMoney.doubleValue());
        }
//        roomTimeService.deleteRoomTimeByOrdersNo(ordersNo);
        UserMoneyDetails userMoneyDetails =new UserMoneyDetails();
        userMoneyDetails.setMoney(refundMoney);
        userMoneyDetails.setUserId(orders.getUserId());
        userMoneyDetails.setContent("订单号："+orders.getOrdersNo()+",已原路退款！");
        userMoneyDetails.setTitle("订单退款");
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(time);
        userMoneyDetailsService.save(userMoneyDetails);

        if(orders.getRefundMoney()==null){
            orders.setRefundMoney(refundMoney);
        }else{
            orders.setRefundMoney(orders.getRefundMoney().add(refundMoney));
        }
        if(StringUtils.isNotBlank(refundRemark)){
            if(StringUtils.isNotBlank(orders.getRefundRemark())){
                orders.setRefundRemark(orders.getRefundRemark()+";"+refundRemark);
            }else{
                orders.setRefundRemark(refundRemark);
            }
        }
        if(orders.getCouponId()!=null){
            if(orders.getRefundMoney().doubleValue()>=orders.getPayMoney().doubleValue()){
                SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                if(couponUser!=null){
                    couponUser.setStatus(1);
                    couponUserJpaRepository.save(couponUser);
                }
            }

        }

        if(StringUtils.isNotBlank(orders.getCouponIds())){
            for (String couponId:orders.getCouponIds().split(",")){
                SelfCouponUser couponUser = couponUserJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                if(couponUser!=null){
                    couponUser.setStatus(1);
                    couponUserJpaRepository.save(couponUser);
                }
            }
        }
        if(orders.getTearoomId()!=null){
            Tearoom tearoom = tearoomService.getById(orders.getTearoomId());
            BigDecimal shopMoney = refundMoney.multiply(tearoom.getRate()).setScale(2, RoundingMode.DOWN);
            userMoneyService.updateShopMoney(2,tearoom.getUserId(),shopMoney.doubleValue());
            userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(shopMoney);
            userMoneyDetails.setShopUserId(tearoom.getUserId());
            userMoneyDetails.setContent("扣除收益："+shopMoney);
            userMoneyDetails.setTitle("订单退款："+orders.getOrdersNo());
            userMoneyDetails.setType(2);
            userMoneyDetails.setClassify(5);
            userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
            userMoneyDetailsService.save(userMoneyDetails);
            if(orders.getShopMoney()!=null){
                orders.setShopMoney(orders.getShopMoney().subtract(shopMoney));
            }
        }
        baseMapper.updateById(orders);

        return Result.success();
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void closeOrders(){
        //查询正在使用中的订单 进行计费操作
        List<Orders> ordersList = baseMapper.selectList(new QueryWrapper<Orders>().eq("status",3));
        for(Orders orders:ordersList){
            try {
                Date ordersEndTime=null;
                ordersEndTime=DateUtils.stringToDate(orders.getEndTime(),DateUtils.DATE_TIME_PATTERN);

                //判断当前时间是否已经超出了
                long nowTime = System.currentTimeMillis();
                if(ordersEndTime.getTime()<nowTime){
                    closeOrders(orders.getOrdersId());
                }
            }catch (Exception e){
                log.error("订单关闭订单异常："+e.getMessage(),e);
            }

        }



        //超时订单未支付 改为取消
        String time = commonInfoService.findOne(826).getValue();
        ordersList = baseMapper.selectCloseOrdersList(time);
        for (Orders orders:ordersList){
            try{
                Goods goods = goodsService.selectGoodsById(orders.getGoodsId());
                goods.setGoodsStatus(1);
                goodsService.updateById(goods);
                orders.setStatus(5);
                if(orders.getOrdersType()!=null && orders.getOrdersType()==1){
                    if(orders.getCouponId()!=null){
                        SelfCouponUser couponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
                        if(couponUser!=null){
                            couponUser.setStatus(1);
                            couponUserJpaRepository.save(couponUser);
                        }
                    }
                    if(StringUtils.isNotBlank(orders.getCouponIds())){
                        for (String couponId:orders.getCouponIds().split(",")){
                            SelfCouponUser couponUser = couponUserJpaRepository.findById(Long.parseLong(couponId)).orElse(null);
                            if(couponUser!=null){
                                couponUser.setStatus(1);
                                couponUserJpaRepository.save(couponUser);
                            }
                        }
                    }
                    roomTimeService.deleteRoomTimeByOrdersNo(orders.getOrdersNo());
                }
                baseMapper.updateById(orders);
            }catch (Exception e){
                e.getMessage();
                log.info("释放订单出错："+e.getMessage(),e);
            }
        }



    }


    @Override
    public Result removeOrders(Long ordersId){
        Orders orders = baseMapper.selectById(ordersId);
        orders.setIsDelete(1);
        baseMapper.updateById(orders);
        return Result.success();
    }

    @Override
    public Result selectTeamUserList(Integer page, Integer limit, String invitationCode,Integer type,Long userId) {
        return Result.success().put("data", new PageUtils(baseMapper.selectTeamUserList(new Page<>(page,limit),invitationCode,type,userId)));
    }

    @Override
    public Double selectOrdersMoneyCountByUserId(Long userId,Integer type){
        return baseMapper.selectOrdersMoneyCountByUserId(userId,type);
    }

    @Override
    public Integer selectUserCountByInvitationCode(String invitationCode,Integer type){
        return baseMapper.selectUserCountByInvitationCode(invitationCode,type);
    }

    @Override
    public Result selectTearoomOrdersCount(Integer page,Integer limit,Integer flag,String time){
        return Result.success().put("data",new PageUtils(baseMapper.selectTearoomOrdersCount(new Page<>(page,limit),flag,time)));
    }

    @Override
    public Double selectOrdersMoneyByTearoom(Integer flag,String time,Long tearoomId,Integer payWay){
        return baseMapper.selectOrdersMoneyByTearoom(flag, time, tearoomId,payWay);
    }

    @Override
    public Integer selectOrdersCountByTearoom(Integer flag,String time,Long tearoomId,Integer payWay){
        return baseMapper.selectOrdersCountByTearoom(flag, time, tearoomId,payWay);
    }

    @Override
    public Double selectOrdersMoneyByGoods(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime){
        return baseMapper.selectOrdersMoneyByGoods(flag, time,tearoomId, goodsId,payWay,endTime);
    }

    @Override
    public Integer selectOrdersCountByGoods(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime){
        return baseMapper.selectOrdersCountByGoods(flag, time,tearoomId, goodsId,payWay,endTime);
    }

    @Override
    public Double selectOrdersMoneyByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime,Integer isShop){
        return baseMapper.selectOrdersMoneyByTearoomId(flag, time, tearoomId,payWay,endTime,isShop);
    }


    @Override
    public Result roomChange(Long ordersId,Long goodsId){
        Orders orders = baseMapper.selectById(ordersId);
        if(orders.getGoodsCategory()==4){
            return Result.error("柜子订单不支持更换！");
        }
        Goods goods = goodsService.selectGoodsById(goodsId);
        if(orders.getCouponId()!=null){
            SelfCouponUser selfCouponUser = couponUserJpaRepository.findById(orders.getCouponId()).orElse(null);
            if(selfCouponUser!=null && StringUtils.isNotBlank(selfCouponUser.getGoodsIds())){
                boolean flag=false;
                for (String oldGoodsId:selfCouponUser.getGoodsIds().split(",")){
                    if(oldGoodsId.equals(String.valueOf(goodsId))){
                        flag=true;
                        break;
                    }
                }
                if(!flag){
                    return Result.error("当前订单使用了优惠券，此优惠券不支持换台至"+goods.getGoodsName());
                }
            }
        }
        Long oldGoodsId=orders.getGoodsId();

        if(goods.getGoodsStatus()==2){
            return Result.error("当前台球桌正在使用中！");
        }
        Goods oldGoods = goodsService.selectGoodsById(oldGoodsId);
        oldGoods.setGoodsStatus(1);
        goods.setGoodsStatus(2);
        goods.setOrdersId(ordersId);
        goodsService.updateById(oldGoods);
        goodsService.updateById(goods);
        orders.setGoodsId(goodsId);
        orders.setGoodsName(goods.getGoodsName());
        orders.setGoodsCoverImage(goods.getGoodsCoverImage());
        baseMapper.updateById(orders);
        /*Tearoom tearoom = tearoomService.getById(orders.getTearoomId());
        roomService.lightControl(tearoom.getKeyCode(),oldGoods.getChannels(),0);
        roomService.lightControl(tearoom.getKeyCode(),goods.getChannels(),1);*/
        try{
            List<Room> lightRoom = roomService.list(new QueryWrapper<Room>().eq("goods_id",oldGoods.getGoodsId()));
            for (Room room:lightRoom){
                try{
                    Result result = roomService.issueInstruct(room.getRoomId(), 2);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            lightRoom = roomService.list(new QueryWrapper<Room>().eq("goods_id",goods.getGoodsId()));
            for (Room room:lightRoom){
                try{
                    Result result = roomService.issueInstruct(room.getRoomId(), 1);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            log.error("调用关闭设备报错："+e.getMessage(),e);
        }
        return Result.success();
    }

    @Override
    public Result closeOrders(Long ordersId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            String createTime = DateUtils.format(new Date());
            Orders orders = baseMapper.selectById(ordersId);
            if (orders.getStatus() != 3) {
                return Result.error("数据异常，刷新后重试！");
            }
            Integer oldStatus = orders.getStatus();
            Goods goods = goodsService.selectGoodsById(orders.getGoodsId());

            Integer status = 4;
            // 1. 初始化时间格式化工具
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // 2. 解析订单关键时间
            // 订单实际开始时间
            Date orderStartTime = df.parse(orders.getStartTime());
            // 订单原计划结束时间
            Date originalEndTime = df.parse(orders.getEndTime());
            // 用户当前点击结束订单的时间（实际结束时间）
            Date currentEndTime = new Date();

            // 3. 转换时间为Calendar，便于精准计算时长
            Calendar orderStartCal = Calendar.getInstance();
            orderStartCal.setTime(orderStartTime);
            Calendar originalEndCal = Calendar.getInstance();
            originalEndCal.setTime(originalEndTime);
            Calendar currentEndCal = Calendar.getInstance();
            currentEndCal.setTime(currentEndTime);

            // 4. 校验时间有效性（避免非法时间区间导致计算异常）
            if (currentEndCal.before(orderStartCal)) {
                return Result.error("当前时间早于订单开始时间，无法结束订单！");
            }

            // 若当前时间晚于或等于原计划结束时间，按原计划结束时间计算（无剩余时长，不退款）
            if (currentEndCal.after(originalEndCal) || currentEndCal.equals(originalEndCal)) {
                currentEndCal = (Calendar) originalEndCal.clone();
            }

            // 5. 查询用户VIP状态
            boolean isVip = false;
            UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id", orders.getUserId()));
            if (userVip != null && userVip.getIsVip() == 1) {
                isVip = true;
            }

            BigDecimal originalGoodsTotalMoney = BigDecimal.ZERO;
            Calendar originalStartCal = (Calendar) orderStartCal.clone();
            Calendar originalTempEndCal = Calendar.getInstance();
            while (originalStartCal.before(originalEndCal)) {
                String currentDate = DateUtils.format(originalStartCal.getTime(), "yyyy-MM-dd");
                int currentHour = originalStartCal.get(Calendar.HOUR_OF_DAY);
                String currentHourStr = currentHour < 10 ? "0" + currentHour : String.valueOf(currentHour);
                int currentMinute = originalStartCal.get(Calendar.MINUTE);
                String currentMinuteStr = currentMinute < 10 ? "0" + currentMinute : String.valueOf(currentMinute);
                String currentTimeSlot = currentHourStr + ":" + currentMinuteStr;

                BigDecimal hourPrice;
                GoodsTime goodsTimeByDate = goodsTimeDao.selectGoodsTimeByRuleType(currentTimeSlot, orders.getTearoomId());
                if (goodsTimeByDate != null) {
                    hourPrice = isVip ? goodsTimeByDate.getMemberHourPrice() : goodsTimeByDate.getHourPrice();
                } else {
                    hourPrice = goods.getHourPrice();
                }

                originalTempEndCal = (Calendar) originalStartCal.clone();
                originalTempEndCal.set(Calendar.MINUTE, 0);
                originalTempEndCal.add(Calendar.HOUR, 1);
                if (originalTempEndCal.after(originalEndCal)) {
                    originalTempEndCal = (Calendar) originalEndCal.clone();
                }

                long timeDiffMillis = originalTempEndCal.getTimeInMillis() - originalStartCal.getTimeInMillis();
                int actualMinutes = (int) (timeDiffMillis / (1000 * 60));
                if (actualMinutes <= 0) {
                    break;
                }
                BigDecimal slotMoney = hourPrice
                        .multiply(new BigDecimal(actualMinutes))
                        .divide(new BigDecimal(60), 10, RoundingMode.HALF_UP)
                        .setScale(2, RoundingMode.HALF_UP);
                originalGoodsTotalMoney = originalGoodsTotalMoney.add(slotMoney).setScale(2, RoundingMode.HALF_UP);

                originalStartCal = (Calendar) originalTempEndCal.clone();
            }

            // 7. 计算【用户已使用时长对应的商品费用】
            BigDecimal usedGoodsMoney = calculateTimeMoney(orderStartCal, currentEndCal, orders.getGoodsId(), isVip, dateFormatter, goods);

            // 8. 计算【理论应退商品费用】= 原商品总费用 - 已使用商品费用
            BigDecimal refundGoodsMoney = originalGoodsTotalMoney.subtract(usedGoodsMoney);
            // 兜底：避免理论应退金额为负数
            refundGoodsMoney = refundGoodsMoney.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : refundGoodsMoney;

            // 9. 核心：优惠券场景下规避反向变现
            BigDecimal actualPayGoodsMoney = BigDecimal.ZERO;
            // 计算用户实际支付的商品费用（原商品费用 - 优惠券抵扣金额）
            if (orders.getCouponMoney() != null) {
                actualPayGoodsMoney = originalGoodsTotalMoney.subtract(orders.getCouponMoney());
                // 若优惠券抵扣后实际支付商品费用＜0，按0计算（用户未支付商品费用）
                actualPayGoodsMoney = actualPayGoodsMoney.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : actualPayGoodsMoney;
            } else {
                // 无优惠券，实际支付商品费用 = 原商品总费用
                actualPayGoodsMoney = originalGoodsTotalMoney;
            }
            // 关键限制：商品退款金额 不能超过 用户实际支付的商品费用（优惠券抵扣部分不退款）
            if (refundGoodsMoney.compareTo(actualPayGoodsMoney) > 0) {
                refundGoodsMoney = actualPayGoodsMoney;
            }

            // 10. 计算【最终退款金额】= 应退商品费用
            BigDecimal totalRefundMoney = refundGoodsMoney;

            // 11. 兜底校验：最终退款金额 不能超过 用户实际支付的总金额（防止超付退款）
            if (totalRefundMoney.compareTo(orders.getPayMoney()) > 0) {
                totalRefundMoney = orders.getPayMoney();
            }

            // 12. 执行退款操作（仅当退款金额＞0时执行）
            if (totalRefundMoney.compareTo(BigDecimal.ZERO) > 0) {
                if (orders.getPayWay() == 1) {
                    // 微信支付退款
                    wxService.refund(orders.getOrdersNo(), orders.getOnePayMoney(), totalRefundMoney);
                } else if (orders.getPayWay() == 2) {
                    // 支付宝支付退款
                    aliPayController.alipayRefund(orders.getOrdersNo(), totalRefundMoney);
                } else if (orders.getPayWay() == 3) {
                    // 余额支付：原路退还到用户余额
                    userMoneyService.updateMoney(1, orders.getUserId(), totalRefundMoney.doubleValue());
                }

                // 记录余额明细
                UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                userMoneyDetails.setMoney(totalRefundMoney);
                userMoneyDetails.setUserId(orders.getUserId());
                userMoneyDetails.setContent("订单提前结束，退款金额：" + totalRefundMoney);
                userMoneyDetails.setTitle("订单退款：" + goods.getGoodsName() + "（订单号：" + orders.getOrdersNo() + "）");
                userMoneyDetails.setType(1);
                userMoneyDetails.setCreateTime(createTime);
                userMoneyDetailsService.save(userMoneyDetails);

                // 更新订单退款信息
                orders.setRefundMoney(totalRefundMoney); // 修复：原代码用了refundGoodsMoney，应该用totalRefundMoney
                String refundRemark = "提前结束订单，按剩余时长计算退款";
                if (StringUtils.isNotBlank(orders.getRefundRemark())) {
                    orders.setRefundRemark(orders.getRefundRemark() + ";" + refundRemark);
                } else {
                    orders.setRefundRemark(refundRemark);
                }

            }
            if (goods.getSales() == null) {
                goods.setSales(1);
            } else {
                goods.setSales(goods.getSales() + 1);
            }
            goods.setGoodsStatus(1);
            goodsService.updateById(goods);
            Tearoom tearoom = tearoomService.getById(orders.getTearoomId());
            if (tearoom != null) {
                if (tearoom.getSales() == null) {
                    tearoom.setSales(1);
                } else {
                    tearoom.setSales(tearoom.getSales() + 1);
                }
                tearoomService.updateById(tearoom);
            }
            // 13. 非全额退款场景：执行商家分账、销量更新等原有逻辑
            if (totalRefundMoney.compareTo(orders.getPayMoney()) < 0) {
                // 计算商家收入（按用户实际消费金额分账）
                BigDecimal sumMoney = orders.getPayMoney().subtract(totalRefundMoney);

                if (tearoom != null && tearoom.getUserId() != null) {
                    // 按实际消费金额（已使用商品费用）分账
                    BigDecimal shopMoney = sumMoney.multiply(tearoom.getRate()).setScale(2, RoundingMode.DOWN);
                    userMoneyService.updateShopMoney(1, tearoom.getUserId(), shopMoney.doubleValue());
                    UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
                    userMoneyDetails.setMoney(shopMoney);
                    userMoneyDetails.setShopUserId(tearoom.getUserId());
                    userMoneyDetails.setContent("到账收益：" + shopMoney);
                    userMoneyDetails.setTitle("订单收益：" + orders.getOrdersNo());
                    userMoneyDetails.setType(1);
                    userMoneyDetails.setClassify(5);
                    userMoneyDetails.setCreateTime(createTime);
                    userMoneyDetailsService.save(userMoneyDetails);
                    orders.setShopMoney(shopMoney);
                }
                userIntegralService.updateIntegral(1,orders.getUserId(),sumMoney.intValue());
                UserIntegralDetails userIntegralDetails=new UserIntegralDetails();
                userIntegralDetails.setClassify(1);
                userIntegralDetails.setContent("完成订单："+orders.getOrdersNo());
                userIntegralDetails.setCreateTime(DateUtils.format(new Date()));
                userIntegralDetails.setNum(sumMoney.intValue());
                userIntegralDetails.setType(1);
                userIntegralDetails.setUserId(orders.getUserId());
                userIntegralDetailsService.save(userIntegralDetails);
            }

            // 14. 更新订单状态和结束时间
            orders.setStatus(status);
            orders.setEndTime(createTime);
            orders.setPayMoney(orders.getPayMoney().subtract(totalRefundMoney));
            baseMapper.updateById(orders);

            // 15. 关闭设备
            try {
                List<Room> lightRoom = roomService.list(new QueryWrapper<Room>().eq("goods_id", orders.getGoodsId()));
                for (Room room : lightRoom) {
                    try {
                        roomService.issueInstruct(room.getRoomId(), 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("调用关闭设备报错1：" + e.getMessage(), e);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("调用关闭设备报错：" + e.getMessage(), e);
            }

            return Result.success().put("totalRefundMoney", totalRefundMoney);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("结束订单异常：" + e.getMessage(), e);
            return Result.error("系统繁忙，请稍后再试！");
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    /**
     * 辅助方法：按下单时的价格规则，精准计算指定时间区间内的商品费用
     * @param startCal 开始时间
     * @param endCal 结束时间
     * @param goodsId 商品ID
     * @param isVip 是否VIP
     * @param dateFormatter 日期格式化器
     * @param goods 商品信息（避免重复查询，优化性能）
     * @return 该时间区间对应的商品费用
     */
    private BigDecimal calculateTimeMoney(Calendar startCal, Calendar endCal, Long goodsId, boolean isVip,
                                          DateTimeFormatter dateFormatter, Goods goods) {
        BigDecimal totalMoney = BigDecimal.ZERO;
        Calendar currentCal = (Calendar) startCal.clone();
        Calendar tempEndCal = Calendar.getInstance();

        if (currentCal.compareTo(endCal) >= 0) {
            return BigDecimal.ZERO;
        }

        // 按价格时段循环计算
        while (currentCal.before(endCal)) {
            int currentHour = currentCal.get(Calendar.HOUR_OF_DAY);
            String currentHourStr = currentHour < 10 ? "0" + currentHour : String.valueOf(currentHour);
            int currentMinute = currentCal.get(Calendar.MINUTE);
            String currentMinuteStr = currentMinute < 10 ? "0" + currentMinute : String.valueOf(currentMinute);
            String currentTimeSlot = currentHourStr + ":" + currentMinuteStr;

            // 查询小时价
            BigDecimal hourPrice;
            GoodsTime goodsTimeByDate = goodsTimeDao.selectGoodsTimeByRuleType(currentTimeSlot, goods.getTearoomId());
            if (goodsTimeByDate != null) {
                hourPrice = isVip ? goodsTimeByDate.getMemberHourPrice() : goodsTimeByDate.getHourPrice();
            } else {
                hourPrice = goods.getHourPrice();
            }

            // 确定时段结束时间
            tempEndCal = (Calendar) currentCal.clone();
            tempEndCal.set(Calendar.MINUTE, 0);
            tempEndCal.add(Calendar.HOUR, 1);
            if (tempEndCal.after(endCal)) {
                tempEndCal = (Calendar) endCal.clone();
            }

            // 计算实际分钟数和时段费用
            long timeDiffMillis = tempEndCal.getTimeInMillis() - currentCal.getTimeInMillis();
            double actualMinutes = ((double) timeDiffMillis / (1000 * 60));
            actualMinutes = (long) Math.ceil(actualMinutes);
            if (actualMinutes <= 0) {
                break;
            }
            BigDecimal slotMoney = hourPrice
                    .multiply(new BigDecimal(actualMinutes))
                    .divide(new BigDecimal(60), 10, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);

            totalMoney = totalMoney.add(slotMoney).setScale(2, RoundingMode.HALF_UP);
            // 推进到当前时段结束时间，继续下一个价格时段计算
            currentCal = (Calendar) tempEndCal.clone();
        }

        return totalMoney;
    }





    @Override
    public Double selectOrdersSumShopMoneyByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime,Integer isShop){
        return baseMapper.selectOrdersSumShopMoneyByTearoomId(flag, time, tearoomId, payWay, endTime,isShop);
    }

    @Override
    public Double selectOrdersSumPayMoneyByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime,Integer isShop){
        return baseMapper.selectOrdersSumPayMoneyByTearoomId(flag, time, tearoomId, payWay, endTime,isShop);
    }

    @Override
    public Integer selectOrdersShopCountByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime){
        return baseMapper.selectOrdersShopCountByTearoomId(flag, time, tearoomId, payWay, endTime);
    }

    @Override
    public Result selectUserByOrdersMoneyList(Integer page,Integer limit,Long tearoomId){
        return Result.success().put("data",baseMapper.selectUserByOrdersMoneyList(new Page<>(page,limit),tearoomId));
    }

    @Override
    public Double selectTearoomPayMoneyCountByTime(Integer flag,String time,Integer classify){
        return baseMapper.selectTearoomPayMoneyCountByTime(flag, time,classify);
    }

    @Override
    public Double selectTearoomShopMoneyCountByTime(Integer flag,String time,Integer classify){
        return baseMapper.selectTearoomShopMoneyCountByTime(flag, time,classify);
    }

    @Override
    public Double selectTearoomCashPledgeCountByTime(Integer flag,String time,Integer classify){
        return baseMapper.selectTearoomCashPledgeCountByTime(flag, time,classify);
    }

    @Override
    public Double selectTearoomSumMoneyCountByTime(Integer flag,String time,Integer classify){
        return baseMapper.selectTearoomSumMoneyCountByTime(flag, time,classify);
    }

    @Override
    public IPage<Map<String,Object>> selectUserByOrdersHourList(Integer page,Integer limit, Integer type, String userName, String phone, String city,
                                                                Long tearoomId,String startTime,String endTime,String tearoomName){
        return baseMapper.selectUserByOrdersHourList(new Page<>(page,limit), type, userName, phone, city, tearoomId,startTime,endTime,tearoomName);
    }

    @Override
    public IPage<Map<String,Object>> selectOrdersUserMoneyList(Integer page,Integer limit,String userName,String phone,String startTime,String endTime){
        IPage<Map<String, Object>> mapIPage = baseMapper.selectOrdersUserMoneyList(new Page<>(page, limit), userName, phone, startTime, endTime);
        return mapIPage;
    }

    @Override
    public Map<String,Object> selectUserByOrdersHourByUserId(Long userId,Integer type,String city,Long tearoomId){
        return baseMapper.selectUserByOrdersHourByUserId(userId, type, city, tearoomId);
    }

    @Override
    public Integer selectOrdersHourByTearoomId(Long tearoomId,Integer type){
        return baseMapper.selectOrdersHourByTearoomId(tearoomId, type);
    }

    @Override
    public Integer selectOrdersGoodsHourByTearoomId(Long goodsId,String startTime,String endTime,Integer isShop){
        return baseMapper.selectOrdersGoodsHourByTearoomId(goodsId, startTime, endTime,isShop);
    }

    @Override
    public Double selectOrdersGoodsMoneyByTearoomId(Long goodsId,String startTime,String endTime,Integer isShop){
        return baseMapper.selectOrdersGoodsMoneyByTearoomId(goodsId, startTime, endTime,isShop);
    }


    @Override
    public Double selectOrdersPayMoneyByTime(Integer type,String time){
        return baseMapper.selectOrdersPayMoneyByTime(type,time);
    }

    @Override
    public Double selectOrdersShopMoneyByTime(Integer type,String time){
        return baseMapper.selectOrdersShopMoneyByTime(type,time);
    }

    @Override
    public Double selectOrdersFenMoneyByTime(Integer type,String time){
        return baseMapper.selectOrdersFenMoneyByTime(type,time);
    }

    @Override
    public Integer selectOrdersCountByNoIsShop(Integer type,String time){
        return baseMapper.selectOrdersCountByNoIsShop(type,time);
    }

    @Override
    public Integer selectOrdersMoneyCountByNoIsShop(Integer type,String time){
        return baseMapper.selectOrdersMoneyCountByNoIsShop(type,time);
    }

    @Override
    public Integer selectOrdersCouponCountByNoIsShop(Integer type,String time){
        return baseMapper.selectOrdersCouponCountByNoIsShop(type,time);
    }

    @Override
    public Double selectShopOpenOrdersMoney(Long tearoomId,String time,Integer type,Integer shopWay,Long openUserId){
        return baseMapper.selectShopOpenOrdersMoney(tearoomId, time, type, shopWay,openUserId);
    }

    @Override
    public Integer selectShopOpenOrdersCount(Long tearoomId,String time,Integer type,Integer shopWay,Long openUserId){
        return baseMapper.selectShopOpenOrdersCount(tearoomId, time, type, shopWay,openUserId);
    }

    @Override
    public Double selectOrdersMoneyByStartTimeAndEndTime(Long tearoomId,Integer flag,String startTime,String endTime,Integer platformType){
        return baseMapper.selectOrdersMoneyByStartTimeAndEndTime(tearoomId, flag, startTime, endTime, platformType);
    }

    @Override
    public Integer selectOrdersCountByStartTimeAndEndTime(Long tearoomId,Integer flag,String startTime,String endTime,Integer platformType){
        return baseMapper.selectOrdersCountByStartTimeAndEndTime(tearoomId, flag, startTime, endTime, platformType);
    }

    @Override
    public Result shopUpdateOrdersStatus(Long ordersId,Long ordersGoodsId){
        Orders orders = baseMapper.selectById(ordersId);
        if(orders.getStatus()==8){
            return Result.error("订单已经挂账了");
        }else if(orders.getStatus()==4 || orders.getStatus()==5){
            return Result.error("订单已完成");
        }
        orders.setStatus(8);
        orders.setOrdersGoodsId(ordersGoodsId);
        baseMapper.updateById(orders);
        Goods goods = goodsService.getById(orders.getGoodsId());
        if(goods.getSales()==null){
            goods.setSales(1);
        }else{
            goods.setSales(goods.getSales()+1);
        }
        goods.setGoodsStatus(1);
        goodsService.updateById(goods);
        Tearoom tearoom = tearoomService.getById(orders.getTearoomId());
        if(tearoom.getSales()==null){
            tearoom.setSales(1);
        }else{
            tearoom.setSales(tearoom.getSales()+1);
        }
        tearoomService.updateById(tearoom);
        try{
            List<Room> lightRoom = roomService.list(new QueryWrapper<Room>().eq("goods_id",orders.getGoodsId()));
            for (Room room:lightRoom){
                try{
                    roomService.issueInstruct(room.getRoomId(), 2);
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("调用关闭设备报错1："+e.getMessage(),e);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("调用关闭设备报错："+e.getMessage(),e);
        }
        return Result.success();
    }

    @Override
    public Double selectOrdersSumMinuteByGoodsId(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime){
        return baseMapper.selectOrdersSumMinuteByGoodsId(flag, time, tearoomId,goodsId,payWay,endTime);
    }

    @Override
    public Double selectOrdersSumMoneyByGoodsId(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime){
        return baseMapper.selectOrdersSumMoneyByGoodsId(flag, time, tearoomId,goodsId,payWay,endTime);
    }

    @Override
    public Result selectOrdersCountByDate(Long tearoomId, String startTime, String endTime, Integer type) {
        // 定义返回的总结果
        Map<String, Object> result = new HashMap<>();
        // 日期解析
        Date startDate = DateUtils.stringToDate(startTime, DateUtils.DATE_PATTERN);
        Date endDate = DateUtils.stringToDate(endTime, DateUtils.DATE_PATTERN);

        if (type == 1) {
            // ========== 类型1：销售总额（金额维度，全程BigDecimal保证精度） ==========
            BigDecimal sumMoney=BigDecimal.ZERO;

            // 2. 循环遍历每一天，统计单日金额+占比
            List<Map<String, Object>> list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (!calendar.getTime().after(endDate)) {
                Map<String, Object> map = new HashMap<>();
                String currentDate = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                map.put("date", currentDate);

                // 3. 查询单日总金额（BigDecimal处理）
                Double dayTotalMoneyDouble = selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 1, currentDate, currentDate, null);
                BigDecimal dayTotalMoney = getBigDecimal(dayTotalMoneyDouble).setScale(2, RoundingMode.HALF_UP);


                // 4. 查询单日各渠道金额（BigDecimal处理+保留两位小数）
                BigDecimal mtMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(currentDate, 1,tearoomId)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal dyMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(currentDate, 2,tearoomId)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal ksMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(currentDate, 3,tearoomId)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal wxMoney = getBigDecimal(selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 4)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal lqMoney = getBigDecimal(selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 5)).setScale(2, RoundingMode.HALF_UP);
                BigDecimal yyqMoney = getBigDecimal(selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 6)).setScale(2, RoundingMode.HALF_UP);
                dayTotalMoney=dayTotalMoney.add(mtMoney).add(dyMoney).add(ksMoney);

                // 5. 计算单日各渠道占比（BigDecimal版百分比计算）
                double mtPercent = calculatePercentage(mtMoney, dayTotalMoney);
                double dyPercent = calculatePercentage(dyMoney, dayTotalMoney);
                double ksPercent = calculatePercentage(ksMoney, dayTotalMoney);
                double wxPercent = calculatePercentage(wxMoney, dayTotalMoney);
                double yyqPercent = calculatePercentage(yyqMoney, dayTotalMoney);
                double lqPercent = calculatePercentage(lqMoney, dayTotalMoney);

                map.put("dayTotalMoney", dayTotalMoney.doubleValue());
                // 6. 存入map（转Double，保留两位小数）
                map.put("mtMoney", mtMoney.doubleValue());
                map.put("mtPercent", mtPercent);
                map.put("dyMoney", dyMoney.doubleValue());
                map.put("dyPercent", dyPercent);
                map.put("ksMoney", ksMoney.doubleValue());
                map.put("ksPercent", ksPercent);
                map.put("wxMoney", wxMoney.doubleValue());
                map.put("wxPercent", wxPercent);
                map.put("yyqMoney", yyqMoney.doubleValue());
                map.put("yyqPercent", yyqPercent);
                map.put("lqMoney", lqMoney.doubleValue());
                map.put("lqPercent", lqPercent);
                list.add(map);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                sumMoney=sumMoney.add(dayTotalMoney);
            }
            result.put("list", list);
            result.put("sumMoney", sumMoney);
        } else {
            // ========== 类型2：销售总数（订单数维度，Integer无需精度处理，仅占比保留两位） ==========
            // 1. 查询周期总订单数
            Integer sumCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 1, startTime, endTime, null);
            sumCount = sumCount == null ? 0 : sumCount;
            result.put("sumCount", sumCount);

            // 2. 循环遍历每一天，统计单日订单数+占比
            List<Map<String, Object>> list = new ArrayList<>();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);

            while (!calendar.getTime().after(endDate)) {
                Map<String, Object> map = new HashMap<>();
                String currentDate = DateUtils.format(calendar.getTime(), DateUtils.DATE_PATTERN);
                map.put("date", currentDate);

                // 3. 查询单日总订单数
                Integer dayTotalCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 1, currentDate, currentDate, null);
                dayTotalCount = dayTotalCount == null ? 0 : dayTotalCount;
                map.put("dayTotalCount", dayTotalCount);

                // 4. 查询单日各渠道订单数
                Integer mtCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 1);
                Integer dyCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 2);
                Integer ksCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 3);
                Integer wxCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 4);
                Integer lqCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 5);
                Integer yyqCount = selectOrdersCountByStartTimeAndEndTime(tearoomId, 2, currentDate, currentDate, 6);



                // 5. 计算单日各渠道订单数占比（保留两位小数）
                double mtPercent = calculatePercentage(mtCount, dayTotalCount);
                double dyPercent = calculatePercentage(dyCount, dayTotalCount);
                double ksPercent = calculatePercentage(ksCount, dayTotalCount);
                double wxPercent = calculatePercentage(wxCount, dayTotalCount);
                double lqPercent = calculatePercentage(lqCount, dayTotalCount);
                double yyqPercent = calculatePercentage(yyqCount, dayTotalCount);

                // 6. 存入map
                map.put("mtCount", mtCount);
                map.put("mtPercent", mtPercent);
                map.put("dyCount", dyCount);
                map.put("dyPercent", dyPercent);
                map.put("ksCount", ksCount);
                map.put("ksPercent", ksPercent);
                map.put("wxCount", wxCount);
                map.put("wxPercent", wxPercent);
                map.put("lqCount", lqCount);
                map.put("lqPercent", lqPercent);
                map.put("yyqCount", yyqCount);
                map.put("yyqPercent", yyqPercent);

                list.add(map);
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }
            result.put("list", list);
        }

        // 返回最终结果
        return Result.success().put("data", result);
    }




    @Override
    public Result selectOrdersDateByTearoomId(Long tearoomId) {
        // ========== 1. 查询原始金额并转换为BigDecimal（避免精度丢失） ==========
        // 总金额
        Double sumMoneyDouble = selectOrdersMoneyByStartTimeAndEndTime(tearoomId, null, null, null, null);
        BigDecimal sumMoney = getBigDecimal(sumMoneyDouble).setScale(2, RoundingMode.HALF_UP);

        // 今日金额
        Calendar calendar = Calendar.getInstance();
        String nowDate = DateUtils.format(calendar.getTime(),DateUtils.DATE_PATTERN);
        Double nowMoneyDouble = selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 2, nowDate, null, null);
        BigDecimal mtMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(nowDate, 1,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal dyMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(nowDate, 2,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal ksMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(nowDate, 3,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal nowMoney = getBigDecimal(nowMoneyDouble).setScale(2, RoundingMode.HALF_UP);
        nowMoney=nowMoney.add(mtMoney).add(dyMoney).add(ksMoney);
        // 昨日金额
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String yesterdayDate = DateUtils.format(calendar.getTime(),DateUtils.DATE_PATTERN);
        Double yesterdayMoneyDouble = selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 2, yesterdayDate, null, null);
        mtMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(yesterdayDate, 1,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        dyMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(yesterdayDate, 2,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        ksMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(yesterdayDate, 3,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal yesterdayMoney = getBigDecimal(yesterdayMoneyDouble).setScale(2, RoundingMode.HALF_UP);
        yesterdayMoney=yesterdayMoney.add(mtMoney).add(dyMoney).add(ksMoney);

        // 各渠道金额（转BigDecimal并保留两位小数）
        mtMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType2( 1,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        dyMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType2( 2,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        ksMoney = getBigDecimal(selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType2( 3,tearoomId)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal wxMoney = getBigDecimal(selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 3, null, null, 4)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal lqMoney = getBigDecimal(selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 3, null, null, 5)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal yyqMoney = getBigDecimal(selectOrdersMoneyByStartTimeAndEndTime(tearoomId, 3, null, null, 6)).setScale(2, RoundingMode.HALF_UP);
        sumMoney=sumMoney.add(mtMoney).add(dyMoney).add(ksMoney);

        // ========== 2. 计算其他金额（BigDecimal精准减法，避免精度丢失） ==========
        BigDecimal otherMoney = sumMoney
                .subtract(mtMoney)    // 总金额 - 美团
                .subtract(dyMoney)    // 减抖音
                .subtract(ksMoney)    // 减快手
                .subtract(wxMoney)    // 减微信
                .subtract(lqMoney)    // 减商家
                .subtract(yyqMoney);   // 减其他指定渠道
        // 保留两位小数（四舍五入），且避免负数（统计口径差异导致）
        otherMoney = otherMoney.setScale(2, RoundingMode.HALF_UP);
        if (otherMoney.compareTo(BigDecimal.ZERO) < 0) {
            otherMoney = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        // ========== 3. 计算各渠道占比（保留两位小数） ==========
        double mtPercent = calculatePercentage(mtMoney, sumMoney);
        double dyPercent = calculatePercentage(dyMoney, sumMoney);
        double ksPercent = calculatePercentage(ksMoney, sumMoney);
        double wxPercent = calculatePercentage(wxMoney, sumMoney);
        double lqPercent = calculatePercentage(lqMoney, sumMoney);
        double yyqPercent = calculatePercentage(yyqMoney, sumMoney);
        double otherPercent = calculatePercentage(otherMoney, sumMoney);

        // ========== 4. 封装结果（转Double便于前端使用，仍保留两位小数） ==========
        Map<String, Object> resultMap = new HashMap<>();
        // 总/今日/昨日金额（转Double，保留两位小数）
        resultMap.put("sumMoney", sumMoney.doubleValue());
        resultMap.put("nowMoney", nowMoney.doubleValue());
        resultMap.put("yesterdayMoney", yesterdayMoney.doubleValue());
        // 各渠道金额
        resultMap.put("mtMoney", mtMoney.doubleValue());
        resultMap.put("dyMoney", dyMoney.doubleValue());
        resultMap.put("ksMoney", ksMoney.doubleValue());
        resultMap.put("wxMoney", wxMoney.doubleValue());
        resultMap.put("lqMoney", lqMoney.doubleValue());
        resultMap.put("yyqMoney", yyqMoney.doubleValue());
        resultMap.put("otherMoney", otherMoney.doubleValue());
        // 各渠道占比
        resultMap.put("mtPercent", mtPercent);
        resultMap.put("dyPercent", dyPercent);
        resultMap.put("ksPercent", ksPercent);
        resultMap.put("wxPercent", wxPercent);
        resultMap.put("lqPercent", lqPercent);
        resultMap.put("yyqPercent", yyqPercent);
        resultMap.put("otherPercent", otherPercent);

        return Result.success().put("data", resultMap);
    }

    /**
     * 工具方法：Double转BigDecimal（处理null，避免精度丢失）
     */
    private BigDecimal getBigDecimal(Double value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        // 用toString()避免Double直接转BigDecimal的精度问题（如0.1转成0.1000000000000001）
        return new BigDecimal(value.toString());
    }

    /**
     * 重载1：BigDecimal类型金额占比计算（保留两位小数）
     */
    private double calculatePercentage(BigDecimal part, BigDecimal total) {
        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return 0.00;
        }
        return part.divide(total, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

    /**
     * 重载2：Integer类型订单数占比计算（保留两位小数）
     */
    private double calculatePercentage(Integer part, Integer total) {
        if (part == null || total == null || total == 0) {
            return 0.00;
        }
        BigDecimal partBd = new BigDecimal(part);
        BigDecimal totalBd = new BigDecimal(total);
        return partBd.divide(totalBd, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
