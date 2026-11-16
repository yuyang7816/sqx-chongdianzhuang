package com.sqx.modules.coupon.entity;

import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * 自营商城优惠券制作
 */
@Data
@Entity
public class SelfCoupon {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long couponId; //优惠券id
    @Column
    private Integer type; //优惠券类型（1团购 2积分兑换 3新人优惠券）
    @Column
    private String couponName; //优惠券名称
    @Column
    private String oldPrice;
    @Column
    private String lessMoney; //优惠券面值
    @Column
    private String minMoney; //优惠券最低消费
    @Column
    private Integer validDay; //优惠券有效期限(天)
    @Column
    private Integer sort; //排序
    @Column
    private Integer status; //状态（1开启 2关闭）
    @Column
    private String createTime; //创建时间
    @Column
    private String tearoomId;//充电桩id
    /**商品信息*/
    @Column
    private String goodsIds; //商品ids(多个)
    @Transient
    private List<Goods> goodsList;
    @Transient
    private Tearoom tearoom;
    @Column
    private Integer hour;
    @Column
    private Integer integral;
    @Column
    private String goodsImages;
    //1优惠券 2实物 3定时劵
    @Column
    private Integer couponType;
    @Column
    private Integer minHour;
}
