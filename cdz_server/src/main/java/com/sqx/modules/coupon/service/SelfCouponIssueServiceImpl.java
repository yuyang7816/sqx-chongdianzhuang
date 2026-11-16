package com.sqx.modules.coupon.service;

import com.sqx.modules.chats.utils.DateUtil;
import com.sqx.modules.coupon.utils.Result;
import com.sqx.modules.coupon.utils.ResultUtil;
import com.sqx.modules.coupon.entity.SelfCoupon;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponJpaRepository;
import com.sqx.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.TearoomService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SelfCouponIssueServiceImpl implements SelfCouponIssueService {
    @Autowired
    private SelfCouponIssueJpaRepository jpaRepository;
    @Autowired
    private SelfCouponUserJpaRepository couponUserJpaRepository;
    @Autowired
    private SelfCouponJpaRepository couponJpaRepository;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public Result findAll(Integer page, Integer size, String goodsId,String tearoomId, String type,String couponName,String tearoomName) {
        Pageable pageable = PageRequest.of(page, size);

        Page<SelfCouponIssue> all = jpaRepository.findAll(pageable,StringUtils.isNotBlank(goodsId)?goodsId:"",
                StringUtils.isNotBlank(tearoomId)?tearoomId:"",
                StringUtils.isNotBlank(type)?type:"",StringUtils.isNotBlank(couponName)?couponName:"",StringUtils.isNotBlank(tearoomName)?tearoomName:"");
        for (SelfCouponIssue selfCouponIssue:all.getContent()){
            Tearoom tearoom = tearoomService.getById(selfCouponIssue.getTearoomId());
            selfCouponIssue.setTearoom(tearoom);
            List<Goods> goodsList=new ArrayList<>();
            if(StringUtils.isNotEmpty(selfCouponIssue.getGoodsIds())){
                for (String id:selfCouponIssue.getGoodsIds().split(",")){
                    Goods goods = goodsService.getById(Long.parseLong(id));
                    goodsList.add(goods);
                }
            }
            selfCouponIssue.setGoodsList(goodsList);
        }
        return ResultUtil.success(all);
    }

    /**
     * 用户可领取的优惠券列表
     * @param goodsId
     * @param userId
     * @return
     */
    @Override
    public Result useList(Integer page,Integer size,String goodsId,Integer type,Integer couponType,Long tearoomId) {
        Pageable pageable = PageRequest.of(page, size);
        Specification<SelfCouponIssue> queryCondition = new Specification<SelfCouponIssue>() {
            @Override
            public Predicate toPredicate(Root<SelfCouponIssue> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
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

                if (couponType != null) {
                    predicates.add(criteriaBuilder.equal(root.get("couponType"), couponType));
                }
                if (tearoomId != null) {
                    predicates.add(criteriaBuilder.equal(root.get("tearoomId"), tearoomId));
                }
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("remainNumber"), 0));
                predicates.add(criteriaBuilder.equal(root.get("status"), 1));
                if(type!=null){
                    predicates.add(criteriaBuilder.equal(root.get("type"), type));
                }

                // 将所有 goodsId 的谓词用 OR 连接起来
                Predicate goodsIdOrPredicate = criteriaBuilder.or(goodsIdPredicates.toArray(new Predicate[goodsIdPredicates.size()]));


                // 将所有谓词（包括其他固定的条件和 goodsId 的 OR 谓词）用 AND 连接起来
                Predicate finalPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                if (!goodsIdPatterns.isEmpty()) { // 确保 goodsIdPatterns 不为空才添加
                    finalPredicate = criteriaBuilder.and(finalPredicate, goodsIdOrPredicate);
                }
                return finalPredicate;
            }
        };
        Page<SelfCouponIssue> pages = jpaRepository.findAll(queryCondition,pageable);
        List<SelfCouponIssue> list = pages.getContent();
        List<SelfCoupon> couponList = couponJpaRepository.findAll(); //优惠券信息：金额
        //筛选可领取的券
        for (SelfCouponIssue c : list) {
            for (SelfCoupon s : couponList) {
                if (c.getCouponId().equals(s.getCouponId())){
                    c.setCoupon(s);
                    break;
                }
            }
            //2.领取限制
            c.setGetStatus(1);
        }
        return ResultUtil.success(pages);
    }

    @Override
    public Result selectTearoomListByCoupon(Long couponIssueId){
        SelfCouponIssue selfCouponIssue = jpaRepository.findById(couponIssueId).orElse(null);
        if(selfCouponIssue.getTearoomId()!=null){
            Tearoom tearoom = tearoomService.getById(selfCouponIssue.getTearoomId());
            if(StringUtils.isNotBlank(selfCouponIssue.getGoodsIds())){
                List<Goods> goodsList=new ArrayList<>();
                if(StringUtils.isNotEmpty(selfCouponIssue.getGoodsIds())){
                    for (String id:selfCouponIssue.getGoodsIds().split(",")){
                        Goods goods = goodsService.getById(Long.parseLong(id));
                        goodsList.add(goods);
                    }
                }
                tearoom.setGoodsList(goodsList);
            }
            return ResultUtil.success(tearoom);
        }
        return ResultUtil.success();
    }


    @Override
    public Result saveBody(SelfCouponIssue entity) {
        entity.setCreateTime(DateUtil.createTime());
        entity.setCouponType(entity.getCouponType());
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCouponIssue entity) {
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long couponIssueId) {
        SelfCouponIssue selfCouponIssue = jpaRepository.findById(couponIssueId).orElse(null);
        if(selfCouponIssue==null){
            return ResultUtil.error(500,"优惠券已失效");
        }
        if(selfCouponIssue.getCouponId()!=null){
            selfCouponIssue.setCoupon(couponJpaRepository.findById(selfCouponIssue.getCouponId()).orElse(null));
        }
        if(selfCouponIssue.getTearoomId()!=null){
            Tearoom tearoom = tearoomService.getById(selfCouponIssue.getTearoomId());
            if(StringUtils.isNotBlank(selfCouponIssue.getGoodsIds())){
                List<Goods> goodsList=new ArrayList<>();
                for (String id:selfCouponIssue.getGoodsIds().split(",")){
                    Goods goods = goodsService.getById(Long.parseLong(id));
                    goodsList.add(goods);
                }
                tearoom.setGoodsList(goodsList);
            }
            selfCouponIssue.setTearoom(tearoom);
            return ResultUtil.success(selfCouponIssue);
        }
        return ResultUtil.success(selfCouponIssue);
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }
}
