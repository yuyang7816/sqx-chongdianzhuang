package com.sqx.modules.tearoom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description tearoom
 * @author fang
 * @date 2023-08-17
 */
@Data
public class Tearoom implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 充电桩id
     */
    @TableId(type = IdType.AUTO)
    private Long tearoomId;

    /**
     * 充电桩名称
     */
    private String tearoomName;

    /**
     * 充电桩封面图
     */
    private String tearoomCoverImage;

    /**
     * 充电桩轮播图
     */
    private String tearoomTitleImage;

    /**
     * 充电桩标签
     */
    private String tearoomLabel;

    /**
     * 省
     */
    private String tearoomProvince;

    /**
     * 市
     */
    private String tearoomCity;

    /**
     * 区
     */
    private String tearoomDistrict;

    /**
     * 详细地址
     */
    private String tearoomAddress;

    /**
     * 经度
     */
    private String tearoomLongitude;

    /**
     * 纬度
     */
    private String tearoomLatitude;

    /**
     * 营业时间 开始时间
     */
    private String startTime;

    /**
     * 营业时间 结束时间
     */
    private String endTime;

    /**
     * 评分
     */
    private BigDecimal score;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态 1正常 2打烊 3今日已满
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 店铺电话
     */
    private String tearoomPhone;

    /**
     * 手续费
     */
    private BigDecimal rate;

    /**
     * 设备组
     */
    private String innerCode;

    /**
     * 设备名称
     */
    private String roomName;

    /**
     * 设备编码
     */
    private String roomNo;

    /**
     * 绑定设备组
     */
    private String keyCode;

    /**
     * wifi账号
     */
    private String wifiNo;

    /**
     * wifi密码
     */
    private String wifiPwd;

    /**
     * 权限 1 有加减 2有加 3有减 4没有
     */
    private Integer moneyScope;

    /**
     * 员工id 多个逗号隔开
     */
    private String employeeUserIds;

    /**
     * 是否开启预定 1是 2否
     */
    private Integer isReserve;

    /**
     * 绑定商铺ids
     */
    private String shopIds;

    /**
     * 播报内容
     */
    private String broadcastContent;

    /**
     * 语音播报 token
     */
    private String yyToken;

    /**
     * 语音播报 id
     */
    private String yyId;

    /**
     * 摄像头门店编号
     */
    private String cameraId;

    /**
     * 微信号
     */
    private String wxCode;

    /**
     * 门店群
     */
    private String qunImg;

    /**
     * 是否是线下开台 1是
     */
    private Integer isOpen;

    /**
     * 店铺类型 1自营 2三方
     */
    private Integer shopType;

    /**
     * 是否享受会员权益  1是 2否
     */
    private Integer isVipPrivilege;

    /**
     * 快手门店id
     */
    private String shopPoiId;

    /**
     * 是否开启爱心活动 1是
     */
    private Integer isWelfare;

    /**
     * 爱心活动介绍
     */
    private String welfareContent;

    /**
     * 爱心活动背景图
     */
    private String welfareImg;

    /**
     * 是否开启人脸识别 1是
     */
    private Integer isFaceRecognition;

    /**
     * 门店是否支持点歌 1是 2否
     */
    private Integer isMusic;

    /**
     * 快手appId
     */
    private String ksAppId;

    /**
     * 快手appSecret
     */
    private String ksAppSecret;

    /**
     * 快手核销优惠券token
     */
    private String ksRefreshToken;

    /**
     * 快手核销优惠券token 失效时间
     */
    private String ksRefreshTokenTime;

    /**
     * 美团email
     */
    private String mtEmail;

    /**
     * 美团password
     */
    private String mtPassword;

    /**
     * 美团门店id
     */
    private String mtStoreId;

    /**
     * 抖音门店id
     */
    private String dyPoiId;

    /**
     * 抖音appId
     */
    private String dyAppId;

    /**
     * 抖音secret
     */
    private String dySecret;

    /**
     * 幸运用户序号
     */
    private String luckySort;

    /**
     * 幸运用户奖励优惠券
     */
    private String luckyCouponIds;

    /**
     * 幸运用户播报内容
     */
    private String luckyContent;

    /**
     * 是否允许操作退款 1是 2否
     */
    private Integer isRefund;

    /**
     * 是否允许微信支付 1是 2否
     */
    private Integer isWxPay;

    /**
     * 扫码器是否自动开台 1是 2否
     */
    private Integer isQrCode;

    /**
     * 充电类型 1电瓶车 2汽车桩
     */
    private Integer tearoomType;

    /**
     * 收费标准
     */
    private String tearoomContent;

    @TableField(exist = false)
    private List<SelfCouponIssue> couponIssueList;

    @TableField(exist = false)
    private List<UserEntity> userEntityList;

    @TableField(exist = false)
    private Integer distance;

    @TableField(exist = false)
    private List<Goods> goodsList;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private Integer kuaiCount;

    @TableField(exist = false)
    private Integer kuaiCount2;

    @TableField(exist = false)
    private Integer manCount;

    @TableField(exist = false)
    private Integer manCount2;

    @TableField(exist = false)
    private BigDecimal price;


    public Tearoom() {}
}
