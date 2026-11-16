package com.sqx.modules.coupon.service;

import com.sqx.modules.chats.utils.DateUtil;
import com.sqx.modules.coupon.utils.Result;
import com.sqx.modules.coupon.utils.ResultUtil;
import com.sqx.modules.coupon.entity.SelfCoupon;
import com.sqx.modules.coupon.respository.SelfCouponJpaRepository;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.TearoomService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SelfCouponServiceImpl implements SelfCouponService {
    @Autowired
    private SelfCouponJpaRepository jpaRepository;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private GoodsService goodsService;

    @Override
    public Result findAll(Integer page, Integer size,Integer type,String couponName,String status) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(new Sort.Order(Sort.Direction.DESC, "sort")));
        return ResultUtil.success(jpaRepository.findAll(pageable,type,StringUtils.isNotBlank(couponName)?couponName:"",StringUtils.isNotBlank(status)?status:""));
    }


    @Override
    public Result saveBody(SelfCoupon entity) {
        entity.setCreateTime(DateUtil.createTime());
        Integer i = jpaRepository.selectCouponNameCount(entity.getCouponName());
        if(i>0){
            return ResultUtil.error(500,"已有相同名称的优惠卷");
        }
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result updateBody(SelfCoupon entity) {
        SelfCoupon selfCoupon = jpaRepository.findById(entity.getCouponId()).orElse(null);
        if(!selfCoupon.getCouponName().equals(entity.getCouponName())){
            Integer i = jpaRepository.selectCouponNameCount(entity.getCouponName());
            if(i>0){
                return ResultUtil.error(500,"已有相同名称的优惠卷");
            }
        }
        return ResultUtil.success(jpaRepository.save(entity));
    }

    @Override
    public Result findOne(Long id) {
        SelfCoupon selfCoupon = jpaRepository.findById(id).orElse(null);
        if(selfCoupon!=null && StringUtils.isNotBlank(selfCoupon.getGoodsIds())){
            String[] split = selfCoupon.getGoodsIds().split(",");
            List<Goods> goodsList=new ArrayList<>();
            for (int i=0;i<split.length;i++){
                Goods goods = goodsService.getById(Long.parseLong(split[i]));
                if(goods!=null){
                    goodsList.add(goods);
                }
            }
            selfCoupon.setGoodsList(goodsList);
            if(selfCoupon.getTearoomId()!=null){
                selfCoupon.setTearoom(tearoomService.getById(selfCoupon.getTearoomId()));
            }

        }
        return ResultUtil.success(selfCoupon);
    }

    @Override
    public Result delete(Long id) {
        jpaRepository.deleteById(id);
        return ResultUtil.success();
    }
}
