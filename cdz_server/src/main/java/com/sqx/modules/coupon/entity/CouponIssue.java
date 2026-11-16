package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


/**
 * @description coupon_issue
 * @author fang
 * @date 2022-11-12
 */
@Data
public class CouponIssue implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 发布优惠券
     */
    private Long couponIssueId;

    /**
     * 优惠券ids
     */
    private String couponIds;

    @TableField(exist = false)
    private List<Coupon> couponList;

    /**
     * 发布优惠券名称
     */
    private String couponIssueName;

    /**
     * 发布优惠券图片
     */
    private String couponIssueImage;

    /**
     * 优惠券主页图
     */
    private String couponContentImage;

    /**
     * 分类 1活动优惠券 2售卖优惠券
     */
    private Integer classify;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 价格
     */
    private BigDecimal originalPrice;

    /**
     * 购买须知
     */
    private String payNotice;

    /**
     * 是否限购 1是 2否
     */
    private Integer isLimit;

    /**
     * 发放数量
     */
    private Integer couponNum;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    public CouponIssue() {}
}
