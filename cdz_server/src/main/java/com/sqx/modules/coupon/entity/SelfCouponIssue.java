package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 自营商城优惠券发布
 */
@Data
@Entity
public class SelfCouponIssue {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponIssueId; //优惠券发布id
    @Column
    private Integer validDay; //优惠券有效期限(天)
    @Column
    private Integer issueNumber; //发布数量
    @Column
    private Integer remainNumber; //剩余数量
    @Column
    private Integer isLimit; //是否限量（1限量 2不限量）
    @Column
    private Integer status; //状态（1开启 2关闭）
    @Column
    private String createTime; //创建时间
    /**优惠券信息*/
    @Column
    private Long couponId; //优惠券id
    @Column
    private Integer type; //优惠券类型（1团购 2积分兑换 3新人优惠券 4）
    @Column
    private String couponName; //优惠券名称
    @Column
    private Integer hour;
    @Transient
    private SelfCoupon coupon; //优惠券实体
    @Column
    private String tearoomId;//充电桩id
    @Transient
    private Tearoom tearoom;
    /**商品信息*/
    @Column
    private String goodsIds; //商品ids(多个)
    @Column
    private String teamRule; //使用规则
    @Column
    private String buyRule; //购买须知
    /**领取状态*/
    @Transient
    private Integer getStatus; //领取状态（1可领取 2已领取）
    @Column
    private Integer integral;
    @Transient
    private List<Goods> goodsList;
    //1优惠券 2实物 3定时劵
    @Column
    private Integer couponType;
    @Column
    private Integer minHour;
    //是否限制时间 1是 2否
    @Column
    private Integer isLimitTime;
    //限制时间范围 1-7  参数格式 1,2,3,4
    @Column
    private String weeks;
    //时间范围 0-23    参数格式 09:00-11:30,15:00-16:20
    @Column
    private String times;

}
