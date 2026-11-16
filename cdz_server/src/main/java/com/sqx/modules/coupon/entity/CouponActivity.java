package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description coupon_activity
 * @author fang
 * @date 2025-03-07
 */
@Data
public class CouponActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 优惠券活动id
     */
    private Long couponActivityId;

    /**
     * 活动开始时间
     */
    private String activityTime;

    /**
     * 活动结束时间
     */
    private String endActivityTime;

    /**
     * 触发频率 1每天 2每周 3每月 4每季度 5每年
     */
    private Integer privilegeType;

    /**
     * 赠送优惠券id
     */
    private String couponIds;

    @TableField(exist = false)
    private List<SelfCouponIssue> selfCouponIssueList;

    /**
     * 活动名称
     */
    private String activityName;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 是否已执行 1是
     */
    private Integer isOk;

    public CouponActivity() {}
}
