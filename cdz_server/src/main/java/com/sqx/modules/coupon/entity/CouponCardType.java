package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @description coupon_card_type
 * @author fang
 * @date 2022-11-12
 */
@Data
public class CouponCardType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 卡密类型id
     */
    private Long couponCardTypeId;

    /**
     * 卡密类型名称
     */
    private String couponCardName;

    /**
     * 包含优惠券
     */
    private String couponIds;
    @TableField(exist = false)
    private List<Coupon> couponList;
    /*//总优惠券数量
    @TableField(exist = false)
    private Integer sumCount;
    //未使用数量
    @TableField(exist = false)
    private String unusedCount;
    //已使用数量
    @TableField(exist = false)
    private String finishCount;*/


    /**
     * 状态 1开启 2关闭
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    public CouponCardType() {}
}
