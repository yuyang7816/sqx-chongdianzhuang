package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.modules.app.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @description coupon_card
 * @author fang
 * @date 2022-11-12
 */
@Data
public class CouponCard implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 卡密优惠券
     */
    @TableId(type = IdType.AUTO)
    private Long couponCardId;

    /**
     * 卡密
     */
    private String couponCardNo;

    /**
     * 卡密类型id
     */
    private Long couponCardTypeId;

    /**
     * 状态 1待使用 2已领取
     */
    private Integer status;

    /**
     * 领取用户id
     */
    private Long userId;

    @TableField(exist = false)
    private UserEntity user;

    /**
     * 创建时间
     */
    private String createTime;

    public CouponCard() {}
}
