package com.sqx.modules.apply.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.modules.app.entity.UserEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @description apply
 * @author fang
 * @date 2022-08-05
 */
@Data
public class Apply implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 申请id
     */
    @TableId(type = IdType.AUTO)
    private Long applyId;

    /**
     * 商户名称
     */
    private String shopName;

    /**
     * 商户负责人
     */
    private String applyName;

    /**
     * 电话
     */
    private String applyPhone;

    /**
     * 地址
     */
    private String shopAddress;

    /**
     * 详细地址
     */
    private String shopAddressDetails;

    /**
     * 证件类型
     */
    private Integer cardType;

    /**
     * 证件号码
     */
    private String cardNo;

    /**
     * 证件照片 正面
     */
    private String cardFrontImg;

    /**
     * 证件照片 正面
     */
    private String cardReverseImg;

    /**
     * 商铺图片
     */
    private String shopImg;

    /**
     * 营业执照图片
     */
    private String businessLicenseImg;

    /**
     * 内容
     */
    private String applyContent;


    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 1待审核 2通过 3拒绝
     */
    private Integer status;

    /**
     * 审核内容
     */
    private String auditContent;

    /**
     * 创建时间
     */
    private String createTime;

    private String longitude;

    private String latitude;

    @TableField(exist = false)
    private UserEntity user;

    public Apply() {}
}
