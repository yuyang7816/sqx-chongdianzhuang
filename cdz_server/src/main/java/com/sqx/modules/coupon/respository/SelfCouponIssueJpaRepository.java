package com.sqx.modules.coupon.respository;


import com.sqx.modules.coupon.entity.SelfCouponIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelfCouponIssueJpaRepository extends JpaRepository<SelfCouponIssue, Long> {

    //分页查询
    Page<SelfCouponIssue> findAll(Pageable pageable);

    //条件查询
    Page<SelfCouponIssue> findAll(Specification<SelfCouponIssue> specification, Pageable pageable);

    //领券列表
    List<SelfCouponIssue> findAll(Specification<SelfCouponIssue> specification);

    @Query(value = "select * from self_coupon_issue s " +
            "left join tearoom t on t.tearoom_id=s.tearoom_id " +
            "where 1=1 " +
            "and if(:goodsId!='',s.goods_ids like concat('%',:goodsId,'%'),1=1) " +
            "and if(:type!='',s.`type`=:type,1=1) " +
            "and if(:tearoomId!='',s.`tearoom_id` = :tearoomId,1=1) " +
            "and if(:couponName!='',s.`coupon_name` like concat('%',:couponName,'%'),1=1) " +
            "and if(:tearoomName!='',t.tearoom_name like concat('%',:tearoomName,'%'),1=1) order by s.create_time desc ",
            countQuery = "select count(*) from self_coupon_issue s " +
                    "left join tearoom t on t.tearoom_id=s.tearoom_id " +
                    "where 1=1 " +
                    "and if(:goodsId!='',s.goods_ids like concat('%',:goodsId,'%'),1=1) " +
                    "and if(:type!='',s.`type`=:type,1=1) " +
                    "and if(:tearoomId!='',s.`tearoom_id` = :tearoomId,1=1) " +
                    "and if(:couponName!='',s.`coupon_name` like concat('%',:couponName,'%'),1=1) " +
                    "and if(:tearoomName!='',t.tearoom_name like concat('%',:tearoomName,'%'),1=1)  ",
            nativeQuery = true)
    Page<SelfCouponIssue> findAll(Pageable pageable,String goodsId,String tearoomId, String type,String couponName,String tearoomName);

    @Query(value = "select * from self_coupon_issue where REPLACE(REPLACE(coupon_name,'】',''),'【','')=:couponName",nativeQuery = true)
    SelfCouponIssue selectCouponByCouponName(String couponName);

    @Query(value = "select * from self_coupon_issue where coupon_name=:couponName",nativeQuery = true)
    SelfCouponIssue selectCouponByCouponNameKs(String couponName);

    @Query(value = "select count(*) from self_coupon_issue where coupon_name=:couponName",nativeQuery = true)
    Integer selectCouponNameCount(@Param("couponName") String couponName);

}
