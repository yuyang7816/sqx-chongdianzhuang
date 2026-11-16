
package com.sqx.modules.coupon.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.respository.SelfCouponUserRepository;
import com.sqx.modules.coupon.utils.*;
import com.sqx.modules.coupon.entity.SelfCoupon;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.orders.dao.OrdersDao;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.controller.app.AliPayController;
import com.sqx.modules.pay.service.WxService;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.tearoom.service.TearoomService;
import com.sqx.modules.utils.CusAccessObjectUtil;
import com.sqx.modules.utils.HttpClientUtil;
import com.sqx.modules.utils.SenInfoCheckUtil;
import com.sqx.modules.utils.TimeCalculator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.io.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SelfCouponUserServiceImpl implements SelfCouponUserService {
    @Autowired
    private SelfCouponUserJpaRepository jpaRepository;
    @Autowired
    private SelfCouponUserRepository selfCouponUserRepository;
    @Autowired
    private SelfCouponIssueJpaRepository couponIssueJpaRepository;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private WxService wxService;
    @Autowired
    private AliPayController aliPayController;
    @Autowired
    private DyCouponUtils dyCouponUtils;
    @Autowired
    private MtCouponUtils mtCouponUtils;
    @Autowired
    private KsCouponUtils ksCouponUtils;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

    @Override
    public Result findAll(Integer page, Integer size, String couponName, String nickName, Integer type, Long tearoomId,
                          Long userId, String ordersId, Integer payWay, String phone,Integer status,Integer platformType) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if (StringUtils.isNotEmpty(couponName)) {
                    predicateList.add(criteriaBuilder.equal(root.get("couponName"), couponName));
                }
                if (type != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("type"), type));
                }
                if (platformType != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("platformType"), platformType));
                }
                if (status != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("status"), status));
                }
                if (tearoomId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("tearoomId"), tearoomId));
                }
                if (userId != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                }
                if (StringUtils.isNotEmpty(nickName)) {
                    predicateList.add(criteriaBuilder.equal(root.get("nickName"), nickName));
                }
                if (StringUtils.isNotEmpty(phone)) {
                    predicateList.add(criteriaBuilder.equal(root.get("phone"), phone));
                }
                if (StringUtils.isNotEmpty(ordersId)) {
                    predicateList.add(criteriaBuilder.like(root.get("ordersId"), ordersId));
                }
                if (payWay != null) {
                    predicateList.add(criteriaBuilder.equal(root.get("payWay"), payWay));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        Page<SelfCouponUser> all = jpaRepository.findAll(queryCondition, pageable);
        for (SelfCouponUser selfCouponUser : all.getContent()) {
            Tearoom tearoom = tearoomService.getById(selfCouponUser.getTearoomId());
            selfCouponUser.setTearoom(tearoom);
        }

        return ResultUtil.success(all);
    }

    /**
     * 用户优惠券列表
     */
    @Override
    public Result userList(Integer page, Integer size, Long userId, String tearoomId, String goodsId, Integer status, Integer type,BigDecimal money) {
        if (status == null) {
            status = 1;
        }
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "createTime")));
        Integer finalStatus = status;
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                // 添加其他固定的条件
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
                if (finalStatus != null) {
                    predicates.add(criteriaBuilder.equal(root.get("status"), finalStatus));
                }
                if (money != null) {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("minMoney"), money));
                }
                if (type != null) {
                    if(type==2){
                        predicates.add(criteriaBuilder.in(root.get("type")).value(2).value(4));
                    }else{
                        predicates.add(criteriaBuilder.equal(root.get("type"), type));
                    }

                }
                List<Predicate> goodsIdPredicates = new ArrayList<>();
                List<String> goodsIdPatterns = new ArrayList<>();
                if (goodsId != null) {
                    // 假设 goodsId 是我们要查找的ID
                    // 构建多个以逗号分隔的字符串模式来匹配 goodsId
                    goodsIdPatterns = Arrays.asList(
                            "%," + goodsId, //匹配 goodsId 结尾的情况（前面有逗号）
                            goodsId + ",%", // 匹配 goodsId 开头的情况（后面可能有逗号）
                            "%," + goodsId + ",%", // 匹配 goodsId 中间的情况（前后都有逗号）
                            goodsId // goodsId 单独存在（没有逗号）
                    );

                    // 为每个模式构建一个 like 谓词
                    for (String pattern : goodsIdPatterns) {
                        goodsIdPredicates.add(criteriaBuilder.like(root.get("goodsIds"), pattern));
                        goodsIdPredicates.add(criteriaBuilder.isNull(root.get("goodsIds")));
                        goodsIdPredicates.add(criteriaBuilder.equal(root.get("goodsIds"), ""));
                    }
                }

                List<Predicate> tearoomIdPredicates = new ArrayList<>();
                List<String> tearoomIdPatterns = new ArrayList<>();
                if (tearoomId != null) {
                    // 假设 goodsId 是我们要查找的ID
                    // 构建多个以逗号分隔的字符串模式来匹配 goodsId
                    tearoomIdPatterns = Arrays.asList(
                            "%," + tearoomId, //匹配 goodsId 结尾的情况（前面有逗号）
                            tearoomId + ",%", // 匹配 goodsId 开头的情况（后面可能有逗号）
                            "%," + tearoomId + ",%", // 匹配 goodsId 中间的情况（前后都有逗号）
                            tearoomId // goodsId 单独存在（没有逗号）
                    );

                    // 为每个模式构建一个 like 谓词
                    for (String pattern : tearoomIdPatterns) {
                        tearoomIdPredicates.add(criteriaBuilder.like(root.get("tearoomId"), pattern));
                        tearoomIdPredicates.add(criteriaBuilder.isNull(root.get("tearoomId")));
                        tearoomIdPredicates.add(criteriaBuilder.equal(root.get("tearoomId"), ""));
                    }
                }

                // 将所有 goodsId 的谓词用 OR 连接起来
                Predicate goodsIdOrPredicate = criteriaBuilder.or(goodsIdPredicates.toArray(new Predicate[goodsIdPredicates.size()]));
                Predicate tearoomIdOrPredicate = criteriaBuilder.or(tearoomIdPredicates.toArray(new Predicate[tearoomIdPredicates.size()]));

                // 将所有谓词（包括其他固定的条件和 goodsId 的 OR 谓词）用 AND 连接起来
                Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                if (!goodsIdPatterns.isEmpty()) { // 确保 goodsIdPatterns 不为空才添加
                    finalPredicate = criteriaBuilder.and(finalPredicate, goodsIdOrPredicate);
                }
                if (!tearoomIdPatterns.isEmpty()) { // 确保 goodsIdPatterns 不为空才添加
                    finalPredicate = criteriaBuilder.and(finalPredicate, tearoomIdOrPredicate);
                }

                return finalPredicate;


            }
        };
        Page<SelfCouponUser> all = jpaRepository.findAll(queryCondition, pageable);
        List<SelfCouponUser> content = all.getContent();

        List<String> tearoomIdList = content.stream().map(SelfCouponUser::getTearoomId).collect(Collectors.toList());
        Map<Long, Tearoom> tearoomMap = new HashMap<>();
        List<Tearoom> tearoomList = tearoomService.list(new QueryWrapper<Tearoom>().in(!tearoomIdList.isEmpty(), "tearoom_id", tearoomIdList));
        tearoomMap = tearoomList.stream().collect(Collectors.toMap(Tearoom::getTearoomId, tearoom -> tearoom));

        List<Long> goodsIdList = new ArrayList<>();
        for (SelfCouponUser couponUser : content) {
            if (StringUtils.isNotBlank(couponUser.getTearoomId())) {
                couponUser.setTearoom(tearoomMap.get(Long.parseLong(couponUser.getTearoomId())));
            }
            if (StringUtils.isNotEmpty(couponUser.getGoodsIds())) {
                for (String id : couponUser.getGoodsIds().split(",")) {
                    goodsIdList.add(Long.parseLong(id));
                }
            }
        }
        List<Goods> goodsList = goodsService.list(new QueryWrapper<Goods>().in(!goodsIdList.isEmpty(), "goods_id", goodsIdList));
        Map<Long, Goods> goodsMap = goodsList.stream().collect(Collectors.toMap(Goods::getGoodsId, goods -> goods));
        for (SelfCouponUser couponUser : content) {
            List<Goods> goodsLists = new ArrayList<>();
            if (StringUtils.isNotEmpty(couponUser.getGoodsIds())) {
                for (String id : couponUser.getGoodsIds().split(",")) {
                    goodsLists.add(goodsMap.get(Long.parseLong(id)));
                }
            }
            couponUser.setGoodsList(goodsLists);
        }

        return ResultUtil.success(all);
    }

    @Override
    public Result shopList(Integer page, Integer size, String tearoomId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "completeTime")));
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                // 添加其他固定的条件
                predicates.add(criteriaBuilder.equal(root.get("shopTearoomId"), tearoomId));
                predicates.add(criteriaBuilder.equal(root.get("status"), 2));
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        };
        Page<SelfCouponUser> all = jpaRepository.findAll(queryCondition, pageable);
        List<SelfCouponUser> content = all.getContent();
        for (SelfCouponUser couponUser : content) {
            if (couponUser.getTearoomId() != null) {
                couponUser.setTearoom(tearoomService.getById(couponUser.getTearoomId()));
            }
            List<Goods> goodsList = new ArrayList<>();
            if (org.apache.commons.lang.StringUtils.isNotEmpty(couponUser.getGoodsIds())) {
                for (String id : couponUser.getGoodsIds().split(",")) {
                    Goods goods = goodsService.getById(Long.parseLong(id));
                    goodsList.add(goods);
                }
            }
            couponUser.setGoodsList(goodsList);
        }
        return ResultUtil.success(all);
    }

    /**
     * 用户优惠券使用列表
     */
    @Override
    public Result useList(Long userId, String tearoomId, String goodsId) {
        Specification<SelfCouponUser> queryCondition = new Specification<SelfCouponUser>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponUser> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                /*List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.equal(root.get("userId"), userId));
                if (StringUtils.isNotEmpty(tearoomId)) {
                    predicateList.add(criteriaBuilder.like(root.get("tearoomId"), "%"+tearoomId+"%"));
                }
                if (StringUtils.isNotEmpty(goodsId)) {
                    predicateList.add(criteriaBuilder.like(root.get("goodsIds"), "%"+goodsId+"%"));
                }
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1)); //未使用
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));*/

                List<Predicate> predicates = new ArrayList<>();

                // 添加其他固定的条件
                predicates.add(criteriaBuilder.equal(root.get("userId"), userId));
                predicates.add(criteriaBuilder.equal(root.get("status"), 1)); // 未使用
                List<Predicate> goodsIdPredicates = new ArrayList<>();
                List<String> goodsIdPatterns = new ArrayList<>();
                if (goodsId != null) {
                    // 假设 goodsId 是我们要查找的ID
                    // 构建多个以逗号分隔的字符串模式来匹配 goodsId
                    goodsIdPatterns = Arrays.asList(
                            "%," + goodsId, //匹配 goodsId 结尾的情况（前面有逗号）
                            goodsId + ",%", // 匹配 goodsId 开头的情况（后面可能有逗号）
                            "%," + goodsId + ",%", // 匹配 goodsId 中间的情况（前后都有逗号）
                            goodsId // goodsId 单独存在（没有逗号）
                    );

                    // 为每个模式构建一个 like 谓词
                    for (String pattern : goodsIdPatterns) {
                        goodsIdPredicates.add(criteriaBuilder.like(root.get("goodsIds"), pattern));
                        goodsIdPredicates.add(criteriaBuilder.isNull(root.get("goodsIds")));
                    }
                }

                List<Predicate> tearoomIdPredicates = new ArrayList<>();
                List<String> tearoomIdPatterns = new ArrayList<>();
                if (tearoomId != null) {
                    // 假设 goodsId 是我们要查找的ID
                    // 构建多个以逗号分隔的字符串模式来匹配 goodsId
                    tearoomIdPatterns = Arrays.asList(
                            "%," + tearoomId, //匹配 goodsId 结尾的情况（前面有逗号）
                            tearoomId + ",%", // 匹配 goodsId 开头的情况（后面可能有逗号）
                            "%," + tearoomId + ",%", // 匹配 goodsId 中间的情况（前后都有逗号）
                            tearoomId // goodsId 单独存在（没有逗号）
                    );

                    // 为每个模式构建一个 like 谓词
                    for (String pattern : tearoomIdPatterns) {
                        tearoomIdPredicates.add(criteriaBuilder.like(root.get("tearoomId"), pattern));
                        tearoomIdPredicates.add(criteriaBuilder.isNull(root.get("tearoomId")));
                    }
                }

                // 将所有 goodsId 的谓词用 OR 连接起来
                Predicate goodsIdOrPredicate = criteriaBuilder.or(goodsIdPredicates.toArray(new Predicate[goodsIdPredicates.size()]));
                Predicate tearoomIdOrPredicate = criteriaBuilder.or(tearoomIdPredicates.toArray(new Predicate[tearoomIdPredicates.size()]));

                // 将所有谓词（包括其他固定的条件和 goodsId 的 OR 谓词）用 AND 连接起来
                Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                if (!goodsIdPatterns.isEmpty()) { // 确保 goodsIdPatterns 不为空才添加
                    finalPredicate = criteriaBuilder.and(finalPredicate, goodsIdOrPredicate);
                }
                if (!tearoomIdPatterns.isEmpty()) { // 确保 goodsIdPatterns 不为空才添加
                    finalPredicate = criteriaBuilder.and(finalPredicate, tearoomIdOrPredicate);
                }

                return finalPredicate;


            }
        };
        List<SelfCouponUser> list = jpaRepository.findAll(queryCondition);
        return ResultUtil.success(list);
    }

    @Override
    public Result userCoupon(Long userId) {
        Specification<SelfCouponIssue> queryCondition = new Specification<SelfCouponIssue>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponIssue> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("remainNumber"), 0));
                predicateList.add(criteriaBuilder.equal(root.get("status"), 1));
                predicateList.add(criteriaBuilder.equal(root.get("type"), 3));
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        List<SelfCouponIssue> list = couponIssueJpaRepository.findAll(queryCondition);
        for (SelfCouponIssue selfCouponIssue : list) {
            SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
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
            }else if (selfCouponIssue.getCouponType() == 3) {
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
            selfCouponUser.setPlatform(1);
            selfCouponUser.setCdkCode(getGeneralOrder());
            selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
            selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
            selfCouponUser.setTimes(selfCouponIssue.getTimes());
            jpaRepository.save(selfCouponUser);
        }
        UserEntity userEntity = userService.selectUserById(userId);
        userEntity.setIsNewCoupon(1);
        userService.updateById(userEntity);
        return ResultUtil.success();
    }


    public static String processLink(String shortLink) {
        try {
            URL url = new URL(shortLink);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setInstanceFollowRedirects(false); // 禁止自动重定向

            int statusCode = connection.getResponseCode();

            if (statusCode >= 300 && statusCode < 400) {
                String redirectUrl = connection.getHeaderField("Location");
                if (redirectUrl != null) {
                    log.info("重定向后的地址: " + redirectUrl);
                    String query = redirectUrl.split("\\?")[1];
                    for (String param : query.split("&")) {
                        if (param.startsWith("object_id=")) {
                            log.info("object_id: " + param.split("=")[1]);
                            return param.split("=")[1];
                        }
                    }
                }
            } else {
                log.info("无重定向或其他状态码: " + statusCode);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //classify 1美团  2抖音  3快手
    @Override
    public Result conversionCoupon(Long userId, String key, Integer classify, Integer codeType,Long tearoomId) {
        if(tearoomId==null){
            tearoomId=Long.parseLong(commonInfoService.findOne(1103).getValue());
        }
        Tearoom tearoom1 = tearoomService.getById(tearoomId);
        Long couponIssueId=null;
        String content=key;
        SelfCouponIssue selfCouponIssue = null;
        if(classify==2){
            String encrypted_data="";
            if(codeType==2){
                encrypted_data=processLink(key);
                key="";
            }
            Result result = dyCouponUtils.checkCoupon(encrypted_data, key,tearoom1.getDyPoiId(),tearoom1.getDyAppId(),tearoom1.getDySecret());
            Integer status = result.getStatus();
            if(0!=status){
                return result;
            }
            couponIssueId=Long.parseLong(String.valueOf(result.getData()));
        }else if(classify==1){
            Result result = mtCouponUtils.verifyCoupon(key,tearoom1.getMtEmail(),tearoom1.getMtPassword(),tearoom1.getMtStoreId());
            Integer status = result.getStatus();
            if(0!=status){
                return result;
            }
            selfCouponIssue = couponIssueJpaRepository.selectCouponByCouponName(String.valueOf(result.getData()));
        }else{
            String token = ksCouponUtils.getToken(tearoom1.getKsAppId(),tearoom1.getKsAppSecret(),tearoom1.getTearoomId());
            Map<String, String> stringStringMap = null;
            if(codeType==1){
                stringStringMap = ksCouponUtils.checkCoupon(token, key, null);
            }else{
                stringStringMap = ksCouponUtils.checkCoupon(token,null , key);
            }
            if("500".equals(stringStringMap.get("code"))){
                return ResultUtil.error(500,stringStringMap.get("message"));
            }
            String orderId = stringStringMap.get("orderId");
            String verifyToken = stringStringMap.get("verifyToken");
            couponIssueId = Long.parseLong(stringStringMap.get("skuId"));
            String encryptedCode = stringStringMap.get("encryptedCode");

            selfCouponIssue = couponIssueJpaRepository.findById(couponIssueId).orElse(null);
            Tearoom tearoom = tearoomService.getById(selfCouponIssue.getTearoomId());

            Map<String, String> stringStringMap1 = ksCouponUtils.verifyCoupon(token, verifyToken, tearoom.getShopPoiId(), encryptedCode, orderId);
            if("500".equals(stringStringMap1.get("code"))){
                return ResultUtil.error(500,stringStringMap1.get("message"));
            }
        }
        if(selfCouponIssue==null){
            selfCouponIssue = couponIssueJpaRepository.findById(couponIssueId).orElse(null);
        }
        SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
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
        }else if (selfCouponIssue.getCouponType() == 3) {
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
        selfCouponUser.setPlatform(5);
        selfCouponUser.setPlatformType(classify);
        selfCouponUser.setCdkCode(getGeneralOrder());
        selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
        selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
        selfCouponUser.setTimes(selfCouponIssue.getTimes());
        jpaRepository.save(selfCouponUser);
        MessageInfo messageInfo = new MessageInfo();
        messageInfo.setUserId(String.valueOf(userId));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setImage(userEntity.getAvatar());
        messageInfo.setType(String.valueOf(classify));
        messageInfo.setContent(content);
        messageInfo.setCreateAt(DateUtil.createTime());
        messageInfo.setState("100");
        messageInfo.setPlatformId(Long.parseLong(selfCouponIssue.getTearoomId()));
        messageInfo.setPhone(userEntity.getPhone());
        messageService.save(messageInfo);
        return ResultUtil.success(selfCoupon);
    }

    @Override
    public Result sendUserCoupon(Long userId, Long couponId, Integer num) {
        SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.findById(couponId).orElse(null);
        SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
        for (int i = 0; i < num; i++) {
            SelfCouponUser selfCouponUser = new SelfCouponUser();
            selfCouponUser.setStatus(1);
            String time = DateUtil.createTime();
            selfCouponUser.setCreateTime(time);
            Integer days = selfCouponIssue.getValidDay();
            selfCouponUser.setFailureTime(DateUtil.addDays(time, days));
            selfCouponUser.setCouponIssueId(couponId);
            selfCouponUser.setCouponId(selfCouponIssue.getCouponId());
            selfCouponUser.setCouponName(selfCouponIssue.getCouponName());
            selfCouponUser.setLessMoney(selfCoupon.getLessMoney());
            selfCouponUser.setMinMoney(selfCoupon.getMinMoney());
            UserEntity userEntity = userService.selectUserById(userId);
            selfCouponUser.setNickName(userEntity.getUserName());
            selfCouponUser.setPhone(userEntity.getPhone());
            if (selfCouponIssue.getCouponType() == 2) {
                selfCouponUser.setType(3);
            }else if (selfCouponIssue.getCouponType() == 3) {
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
            selfCouponUser.setPlatform(6);
            selfCouponUser.setCdkCode(getGeneralOrder());
            selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
            selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
            selfCouponUser.setTimes(selfCouponIssue.getTimes());
            jpaRepository.save(selfCouponUser);
        }
        selfCouponIssue.setRemainNumber(selfCouponIssue.getRemainNumber() - num);
        couponIssueJpaRepository.save(selfCouponIssue);
        return ResultUtil.success();
    }

    @Override
    public Result sendNewUserCoupon(Long userId, Long couponId) {
        int i = jpaRepository.selectNewUserCoupon(userId, couponId);
        if (i > 0) {
            return ResultUtil.error(500, "您已经领取过了！");
        }
        SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.findById(couponId).orElse(null);
        SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
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
        }else if (selfCouponIssue.getCouponType() == 3) {
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
        selfCouponUser.setPlatform(6);
        selfCouponUser.setCdkCode(getGeneralOrder());
        selfCouponUser.setNewUserId(userId);
        selfCouponUser.setCouponIssueId(couponId);
        selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
        selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
        selfCouponUser.setTimes(selfCouponIssue.getTimes());
        jpaRepository.save(selfCouponUser);
        selfCouponIssue.setRemainNumber(selfCouponIssue.getRemainNumber() - 1);
        couponIssueJpaRepository.save(selfCouponIssue);
        return ResultUtil.success();
    }


    @Override
    public Result sendUserCoupon(String userIds, String couponIds, Integer type) {
        if (type == 1) {
            CommonInfo templateId = commonInfoService.findOne(1045);
            for (String sCouponId : couponIds.split(",")) {
                Long couponId = Long.parseLong(sCouponId);
                SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.findById(couponId).orElse(null);
                SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
                for (String sUserId : userIds.split(",")) {
                    Long userId = Long.parseLong(sUserId);
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
                    }else if (selfCouponIssue.getCouponType() == 3) {
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
                    selfCouponUser.setPlatform(6);
                    selfCouponUser.setCdkCode(getGeneralOrder());
                    selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
                    selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
                    selfCouponUser.setTimes(selfCouponIssue.getTimes());
                    jpaRepository.save(selfCouponUser);
                    if(userEntity!=null &&  StringUtils.isNotEmpty(userEntity.getOpenId())){
                        List<String> data = new ArrayList<>();
                        String title="平台赠送您优惠券";
                        data.add(title);
                        String content=selfCouponUser.getCouponName();
                        if(content.length()>20){
                            content=content.substring(0,15)+"...";
                        }
                        data.add(content);
                        data.add(com.sqx.common.utils.DateUtils.format(new Date()));
                        SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), templateId.getValue(), data, 8);
                    }

                }
                selfCouponIssue.setRemainNumber(selfCouponIssue.getRemainNumber() - userIds.split(",").length);
                couponIssueJpaRepository.save(selfCouponIssue);
            }
        } else {
            send(couponIds);
        }
        return ResultUtil.success();
    }


    @Async
    public void send(String couponIds) {
        CommonInfo templateId = commonInfoService.findOne(1045);
        List<UserEntity> list = userService.list();
        for (UserEntity user : list) {
            Long userId = user.getUserId();
            for (String sCouponId : couponIds.split(",")) {
                Long couponId = Long.parseLong(sCouponId);
                SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.findById(couponId).orElse(null);
                SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
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
                }else if (selfCouponIssue.getCouponType() == 3) {
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
                selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
                selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
                selfCouponUser.setTimes(selfCouponIssue.getTimes());
                jpaRepository.save(selfCouponUser);
                selfCouponIssue.setRemainNumber(selfCouponIssue.getRemainNumber() - 1);
                couponIssueJpaRepository.save(selfCouponIssue);
                List<String> data = new ArrayList<>();
                String title="平台赠送您优惠券";
                data.add(title);
                String content=selfCouponUser.getCouponName();
                if(content.length()>20){
                    content=content.substring(0,15)+"...";
                }
                data.add(content);
                data.add(com.sqx.common.utils.DateUtils.format(new Date()));
                SenInfoCheckUtil.sendMsg(userEntity.getOpenId(), templateId.getValue(), data, 8);
            }

        }
    }

    /**
     * 领取优惠券
     *
     * @param entity
     * @return
     */
    @Override
    public Result saveBody(SelfCouponUser entity) {
        SelfCouponIssue couponIssue = couponIssueJpaRepository.findById(entity.getCouponIssueId()).orElse(null);
        if (couponIssue.getRemainNumber() < 1) {
            return ResultUtil.error(-1, "优惠券领取完了");
        }
        String time = DateUtil.createTime();
        entity.setCreateTime(time); //创建时间
        entity.setStatus(1); //状态（1未使用 2已使用 3已过期）
        //失效时间
        SelfCoupon coupon = couponJpaRepository.findById(entity.getCouponId()).orElse(null);
        Integer days = coupon.getValidDay();
        entity.setFailureTime(DateUtil.addDays(time, days));
        //优惠券剩余数量-1
        couponIssue.setRemainNumber(couponIssue.getRemainNumber() - 1);
        entity.setHour(couponIssue.getHour());
        entity.setMinHour(couponIssue.getMinHour());
        entity.setMinHour(couponIssue.getMinHour());
        entity.setGoodsIds(couponIssue.getGoodsIds());
        entity.setTearoomId(couponIssue.getTearoomId());
        entity.setTeamRule(couponIssue.getTeamRule());
        entity.setBuyRule(couponIssue.getBuyRule());
        entity.setIsLimitTime(couponIssue.getIsLimitTime());
        entity.setWeeks(couponIssue.getWeeks());
        entity.setTimes(couponIssue.getTimes());
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCouponUser entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateStatus(SelfCouponUser entity) {
        SelfCouponUser selfCouponUser = jpaRepository.findById(entity.getCouponUserId()).orElse(null);
        if (selfCouponUser != null) {
            if (selfCouponUser.getStatus() == 1) {
                selfCouponUser.setStatus(2);
            } else {
                selfCouponUser.setStatus(1);
            }
        }
        return ResultUtil.success(jpaRepository.save(selfCouponUser));
    }

    @Override
    public Result findOne(Long id) {
        SelfCouponUser selfCouponUser = jpaRepository.findById(id).orElse(null);
        if(selfCouponUser!=null){
            if(selfCouponUser.getTearoomId()!=null){
                selfCouponUser.setTearoom(tearoomService.getById(selfCouponUser.getTearoomId()));
            }
        }
        return ResultUtil.success(selfCouponUser);
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public Result payCoupon(Long userId, Long couponIssueId) {
        reentrantReadWriteLock.writeLock().lock();
        try {
            UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
            SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.findById(couponIssueId).orElse(null);
            if (selfCouponIssue.getIsLimit() == 1 && selfCouponIssue.getRemainNumber() <= 0) {
                return ResultUtil.error(500, "优惠券剩余数量不足！");
            }
            SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
            double payMoney = Double.parseDouble(selfCoupon.getLessMoney());
            if (userMoney.getMoney().doubleValue() < payMoney) {
                return ResultUtil.error(-200, "账户余额不足！");
            }
            userMoneyService.updateMoney(2, userId, payMoney);
            UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(payMoney));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("支付金额：" + payMoney);
            userMoneyDetails.setTitle("购买团购劵：" + selfCouponIssue.getCouponName());
            userMoneyDetails.setType(2);
            userMoneyDetails.setCreateTime(DateUtil.createTime());
            userMoneyDetailsService.save(userMoneyDetails);

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
            }else {
                selfCouponUser.setType(2);
            }
            selfCouponUser.setUserId(userId);
            selfCouponUser.setHour(selfCouponIssue.getHour());
            selfCouponUser.setMinHour(selfCouponIssue.getMinHour());
            selfCouponUser.setGoodsIds(selfCouponIssue.getGoodsIds());
            selfCouponUser.setTearoomId(selfCouponIssue.getTearoomId());
            selfCouponUser.setTeamRule(selfCouponIssue.getTeamRule());
            selfCouponUser.setBuyRule(selfCouponIssue.getBuyRule());
            selfCouponUser.setPayWay(6);
            selfCouponUser.setPayMoney(BigDecimal.valueOf(payMoney));
            selfCouponUser.setOrdersId(getGeneralOrder());
            selfCouponUser.setPlatform(4);
            selfCouponUser.setCdkCode(getGeneralOrder());
            selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
            selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
            selfCouponUser.setTimes(selfCouponIssue.getTimes());
            jpaRepository.save(selfCouponUser);
            selfCouponIssue.setRemainNumber(selfCouponIssue.getRemainNumber() - 1);
            couponIssueJpaRepository.save(selfCouponIssue);
            return ResultUtil.success();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return ResultUtil.error(500, "系统繁忙");
    }

    @Scheduled(cron = "0 */1 * * * ?")
    public void updateEndCoupon() {
        jpaRepository.updateEndCoupon();
    }

    @Override
    public Result refundMoney(Long couponUserId) {
        SelfCouponUser couponUser = jpaRepository.findById(couponUserId).orElse(null);
        if (couponUser.getPayWay() == null || couponUser.getPayWay() == 6) {
            //退款零钱
            userMoneyService.updateMoney(1, couponUser.getUserId(), couponUser.getPayMoney().doubleValue());
        } else if (couponUser.getPayWay() == 1 || couponUser.getPayWay() == 2 || couponUser.getPayWay() == 3) {
            boolean refund = wxService.refund(couponUser.getOrdersId(), couponUser.getPayMoney(), couponUser.getPayMoney());
            if (!refund) {
                return ResultUtil.error(500, "退款失败，请联系客服！");
            }
        } else {
            //退款支付宝
            String data = aliPayController.alipayRefund(couponUser.getOrdersId(), couponUser.getPayMoney());
            if (org.apache.commons.lang.StringUtils.isNotBlank(data)) {
                log.error(data);
                JSONObject jsonObject = JSON.parseObject(data);
                JSONObject alipay_trade_refund_response = jsonObject.getJSONObject("alipay_trade_refund_response");
                String code1 = alipay_trade_refund_response.getString("code");
                if (!"10000".equals(code1)) {
                    return ResultUtil.error(500, "退款失败！" + alipay_trade_refund_response.getString("sub_msg"));
                }
            } else {
                return ResultUtil.error(500, "退款失败！");
            }
        }
        UserMoneyDetails userMoneyDetails = new UserMoneyDetails();
        userMoneyDetails.setMoney(couponUser.getPayMoney());
        userMoneyDetails.setUserId(couponUser.getUserId());
        userMoneyDetails.setContent("优惠券：" + couponUser.getCouponName() + ",已原路退款！");
        userMoneyDetails.setTitle("优惠券退款");
        userMoneyDetails.setType(1);
        userMoneyDetails.setCreateTime(DateUtil.createTime());
        userMoneyDetailsService.save(userMoneyDetails);
        couponUser.setStatus(3);
        jpaRepository.save(couponUser);
        return ResultUtil.success();
    }


    public String getGeneralOrder() {
        while (true){
            Date date = new Date();
            String newString = String.format("%0" + 4 + "d", (int) ((Math.random() * 9 + 1) * 10000));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = sdf.format(date);
            String cdkCode=format + newString;
            SelfCouponUser selfCouponUser = jpaRepository.selectSelfCouponUserByCdkCode(cdkCode);
            if(selfCouponUser==null){
                return cdkCode;
            }
        }
    }

    @Override
    public Result verificationCoupon(Long userId, Long tearoomId, String cdkCode) {
        SelfCouponUser selfCouponUser = jpaRepository.selectSelfCouponUserByCdkCode(cdkCode);
        if (selfCouponUser == null) {
            return ResultUtil.error(500, "劵不存在！");
        } else if (selfCouponUser.getStatus() == 2) {
            return ResultUtil.error(500, "劵已使用！");
        } else if (selfCouponUser.getStatus() == 3) {
            return ResultUtil.error(500, "劵已过期！");
        }
        selfCouponUser.setStatus(2);
//        selfCouponUser.setShopUserId(userId);
        selfCouponUser.setShopTearoomId(tearoomId);
        selfCouponUser.setCompleteTime(DateUtil.createTime());
        jpaRepository.save(selfCouponUser);
        if(StringUtils.isNotEmpty(selfCouponUser.getTearoomId())){
            Tearoom tearoom = tearoomService.getById(selfCouponUser.getTearoomId());
            if(tearoom!=null && tearoom.getUserId()!=null && selfCouponUser.getPayMoney()!=null && selfCouponUser.getPayMoney().doubleValue()>0){
                userMoneyService.updateShopMoney(1,tearoom.getUserId(),selfCouponUser.getPayMoney().doubleValue());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(selfCouponUser.getPayMoney());
                userMoneyDetails.setShopUserId(tearoom.getUserId());
                userMoneyDetails.setContent("到账收益："+selfCouponUser.getPayMoney());
                userMoneyDetails.setTitle("核销优惠券："+selfCouponUser.getCouponName());
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(5);
                userMoneyDetails.setCreateTime(DateUtil.createTime());
                userMoneyDetailsService.save(userMoneyDetails);
            }
        }

        return ResultUtil.success();
    }

    @Override
    public Integer selectCouponCountByTimeAndPlatform(String time, Integer platform) {
        return selfCouponUserRepository.selectCouponCountByTimeAndPlatform(time, platform);
    }

    @Override
    public Integer selectCouponCountByTimeAndPlatformType(String time, Integer platformType,String tearoomId) {
        return selfCouponUserRepository.selectCouponCountByTimeAndPlatformType(time, platformType,StringUtils.isNotBlank(tearoomId)?tearoomId:"");
    }

    @Override
    public BigDecimal selectCouponMoneyByTimeAndPlatformType(String time, Integer platformType,String tearoomId) {
        return selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformType(time, platformType,StringUtils.isNotBlank(tearoomId)?tearoomId:"");
    }

    @Override
    public Integer selectCouponCountByTimeAndPlatforms(String time,String tearoomId) {
        return selfCouponUserRepository.selectCouponCountByTimeAndPlatforms(time,StringUtils.isNotBlank(tearoomId)?tearoomId:"");
    }

    @Override
    public BigDecimal selectCouponMoneyByTimeAndPlatforms(String time,String tearoomId) {
        return selfCouponUserRepository.selectCouponMoneyByTimeAndPlatforms(time,StringUtils.isNotBlank(tearoomId)?tearoomId:"");
    }

    @Override
    public Integer selectCouponCountByTimeAndPlatformsPay(String time,String tearoomId) {
        return selfCouponUserRepository.selectCouponCountByTimeAndPlatformsPay(time,StringUtils.isNotBlank(tearoomId)?tearoomId:"");
    }

    @Override
    public BigDecimal selectCouponMoneyByTimeAndPlatformsPay(String time,String tearoomId) {
        return selfCouponUserRepository.selectCouponMoneyByTimeAndPlatformsPay(time,StringUtils.isNotBlank(tearoomId)?tearoomId:"");
    }

    @Override
    public Result sendCoupon(String sCouponId, Long userId) {
        Long couponId = Long.parseLong(sCouponId);
        SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.findById(couponId).orElse(null);
        if (selfCouponIssue != null) {
            SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
            if (selfCoupon != null) {
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
                }else if (selfCouponIssue.getCouponType() == 3) {
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
                selfCouponUser.setPlatform(1);
                selfCouponUser.setCdkCode(getGeneralOrder());
                selfCouponUser.setIsLimitTime(selfCouponIssue.getIsLimitTime());
                selfCouponUser.setWeeks(selfCouponIssue.getWeeks());
                selfCouponUser.setTimes(selfCouponIssue.getTimes());
                jpaRepository.save(selfCouponUser);
            }
        }
        return ResultUtil.success();
    }

    @Override
    public Integer selectCouponUserCount(Long userId,Long couponIssueId){
        return jpaRepository.selectCouponUserCount(userId, couponIssueId);
    }

    @Autowired
    private OrdersDao ordersDao;

    @Override
    public Result testInsertCoupon(){
        List<Orders> ordersList = ordersDao.selectList(new QueryWrapper<Orders>().isNotNull("coupon_name").isNull("coupon_id"));
        for (Orders orders:ordersList){
            SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.selectCouponByCouponNameKs(orders.getCouponName());
            SelfCoupon selfCoupon = couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null);
            SelfCouponUser selfCouponUser = new SelfCouponUser();
            if(orders.getStatus()==4){
                selfCouponUser.setStatus(2);
            }else{
                selfCouponUser.setStatus(3);
            }

            String time = orders.getCreateTime();
            selfCouponUser.setCreateTime(orders.getCreateTime());
            Integer days = selfCouponIssue.getValidDay();
            selfCouponUser.setFailureTime(DateUtil.addDays(time, days));
            selfCouponUser.setCouponId(selfCouponIssue.getCouponId());
            selfCouponUser.setCouponName(selfCouponIssue.getCouponName());
            selfCouponUser.setLessMoney(selfCoupon.getLessMoney());
            selfCouponUser.setMinMoney(selfCoupon.getMinMoney());
            selfCouponUser.setNickName("扫码器核销");
            if (selfCouponIssue.getCouponType() == 2) {
                selfCouponUser.setType(3);
            }else if (selfCouponIssue.getCouponType() == 3) {
                selfCouponUser.setType(4);
            } else {
                selfCouponUser.setType(2);
            }
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
            selfCouponUser.setPlatformType(2);
            selfCouponUser.setShopTearoomId(orders.getTearoomId());
            jpaRepository.save(selfCouponUser);
            Orders orders1=new Orders();
            orders1.setOrdersId(orders.getOrdersId());
            orders1.setCouponId(selfCouponUser.getCouponId());
            ordersDao.updateById(orders1);
        }
        return ResultUtil.success();
    }

}