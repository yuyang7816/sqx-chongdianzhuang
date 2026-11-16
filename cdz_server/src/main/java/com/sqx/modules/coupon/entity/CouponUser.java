package com.sqx.modules.coupon.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@TableName("coupon_user")
public class CouponUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)

    @ApiModelProperty("id")
    private Long id;


    @ApiModelProperty("用户id")
    private Long userId;


    @ApiModelProperty("优惠券名称")
    private String couponName;


    @ApiModelProperty("优惠券图片")
    private String couponPicture;


    @ApiModelProperty("优惠券领取时间")
    private String createTime;


    @ApiModelProperty("优惠券使用时间")
    private String employTime;


    @ApiModelProperty("优惠券过期时间")
    private String expirationTime;


    @ApiModelProperty("优惠券可使用订单最低金额")
    private BigDecimal minMoney;


    @ApiModelProperty("优惠券金额")
    private BigDecimal money;

    @ApiModelProperty("优惠券状态 0正常 1已使用 2已失效")
    private Integer status;

    @ApiModelProperty("领取id")
    private Long couponIssueId;

    @TableField(exist = false)
    private String nickName;

    @TableField(exist = false)
    private String avatar;

    @TableField(exist = false)
    private String phone;

    public CouponUser() {}
}