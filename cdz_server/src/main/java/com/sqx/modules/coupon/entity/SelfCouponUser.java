package com.sqx.modules.coupon.entity;

import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

/**
 * 自营商城优惠券用户领取记录
 */
@Data
@Entity
public class SelfCouponUser {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponUserId; //优惠券用户id
    @Column
    private String failureTime; //失效时间
    @Column
    private Integer status; //状态（1未使用 2已使用 3已过期）
    @Column
    private String createTime; //创建时间
    /**优惠券信息*/
    @Column
    private Long couponId; //优惠券id
    @Column
    private Integer type; //优惠券类型（1团购 2优惠券 3实物 4定时劵）
    @Column
    private String tearoomId;//充电桩id
    @Transient
    private Tearoom tearoom;
    /**商品信息*/
    @Column
    private String goodsIds; //商品ids(多个)
    @Column
    private String couponName; //优惠券名称
    @Column
    private String lessMoney; //优惠券面值
    @Column
    private String minMoney; //优惠券最低消费
    @Transient
    private SelfCoupon coupon; //优惠券实体
    /**优惠券发布信息*/
    @Column
    private Long couponIssueId; //优惠券发布id
    /**用户信息*/
    @Column
    private Long userId; //用户id
    @Column
    private String nickName; //用户昵称
    @Column
    private String phone; //用户昵称
    @Column
    private Integer hour;
    @Column
    private String teamRule; //使用规则
    @Column
    private String buyRule; //购买须知
    @Transient
    private List<Goods> goodsList;
    //1app微信 2微信公众号 3微信小程序 4支付宝app 5支付宝h5 6零钱
    @Column
    private Integer payWay;
    @Column
    private BigDecimal payMoney;
    @Column
    private String ordersId;
    //来源 1新用户领取 2邀请新用户 3邀请用户下单 4购买 5兑换 6商家赠送
    @Column
    private Integer platform;
    //来源平台 1美团 2抖音 3快手
    @Column
    private Integer platformType;
    //来源id
    @Column
    private Long platformId;
    //兑换码
    @Column
    private String cdkCode;
    //核销商家用户
    @Column
    private Long shopUserId;
    //核销门店id
    @Column
    private Long shopTearoomId;
    @Column
    private String completeTime;
    @Column
    private Long newUserId;
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
    @Column
    private String errorMsg;

}
