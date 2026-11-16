package com.sqx.modules.orders.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import lombok.Data;

import javax.persistence.Column;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description orders
 * @author fang
 * @date 2023-08-17
 */
@Data
public class Orders implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 订单id
     */
    private Long ordersId;

    /**
     * 充电桩id
     */
    private Long tearoomId;

    /**
     * 商品id
     */
    private Long goodsId;

    @TableField(exist = false)
    private Goods goods;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品封面图
     */
    private String goodsCoverImage;

    /**
     * 首次付款订单金额
     */
    private BigDecimal onePayMoney;

    /**
     * 支付金额
     */
    private BigDecimal payMoney;

    /**
     * 原金额
     */
    private BigDecimal oldPayMoney;

    /**
     * 状态 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价 7待补费 8挂账
     */
    private Integer status;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 支付方式 1微信 2支付宝 3零钱
     */
    private Integer payWay;

    /**
     * 订单号
     */
    private String ordersNo;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 优惠券id
     */
    private Long couponId;

    /**
     * 优惠券抵扣
     */
    private BigDecimal couponMoney;

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 是否拥有权限 1拥有 2未拥有
     */
    private Integer openDoor;

    /**
     * 包时id
     */
    private Long productId;

    /**
     * 包时名称
     */
    private String productName;

    /**
     * 每分钟价格
     */
    private BigDecimal minutePrice;

    /**
     * 超出时间
     */
    private Integer minuteNum;

    /**
     * 扣除费用 超出时长后 按照每分钟进行计费
     */
    private BigDecimal minuteMoney;

    /**
     * 剩余余额 默认为押金金额 超出时长后 按照分钟每分钟进行计费
     */
    private BigDecimal sumMoney;

    /**
     * 押金
     */
    private BigDecimal cashPledge;

    /**
     * 余额超出时间
     */
    private Integer userMoneyMinuteNum;

    /**
     * 余额扣除费用
     */
    private BigDecimal userMoneyMinuteMoney;

    /**
     * 续费优惠券
     */
    private String couponIds;

    /**
     * 续费优惠券抵扣
     */
    private BigDecimal couponMoneys;

    /**
     * 续费优惠券名称
     */
    private String couponNames;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退款金额
     */
    private BigDecimal refundMoney;

    /**
     * 退款原因
     */
    private String refundRemark;

    /**
     * 是否删除 1是
     */
    private Integer isDelete;

    /**
     * 直推用户id
     */
    private Long zhiUserId;

    /**
     * 直推收益
     */
    private BigDecimal zhiMoney;

    /**
     * 间推用户id
     */
    private Long feiUserId;

    /**
     * 间推用户收益
     */
    private BigDecimal feiMoney;

    /**
     *
     */
    private BigDecimal money;

    /**
     * 加时
     */
    private Integer addHour;

    /**
     * 加时支付金额
     */
    private BigDecimal addPayMoney;

    /**
     * 时长
     */
    private Double hour;

    /**
     * 订单类型 1电瓶车 2汽车
     */
    private Integer ordersType;

    /**
     * 柜子商品id
     */
    private Long smallHardwareId;

    /**
     * 商品id
     */
    private Long smallGoodsId;

    /**
     * 预约金是否退还 1是 2否
     */
    private Integer isSubscription;

    /**
     * 套餐时间即将结束 通知 1已通知
     */
    private Integer isHourMsg;

    /**
     * 押金时间即将结束 通知 1已通知
     */
    private Integer isCashMsg;

    /**
     * 小时费用
     */
    private BigDecimal hourMoney;

    /**
     * 商家最终收益
     */
    private BigDecimal shopMoney;

    /**
     * 商家开台 1是
     */
    private Integer isShop;

    /**
     * 类型 1台球 2棋牌
     */
    private Integer goodsCategory;

    /**
     * 优惠金额
     */
    private BigDecimal vipMoney;

    /**
     * 会员等级
     */
    private Integer vipIndex;

    /**
     * 备注
     */
    private String userRemark;

    /**
     * 是否是幸运订单 1是
     */
    private Integer isLuck;

    /**
     * 是否是生日订单 1是
     */
    private Integer isBirthday;

    //来源平台 1美团 2抖音 3快手
    private Integer platformType;

    /**
     * 订单计费方式 1到期关闭 2持续计费
     */
    private Integer priceType;

    /**
     * 开台员工id
     */
    private Long openUserId;

    /**
     * 关台员工id
     */
    private Long closeUserId;

    /**
     * 商家开台支付方式 1微信 2现金  3门店会员 4美团核销 5快手核销 6抖音核销
     */
    private Integer shopWay;

    /**
     * 支付折扣
     */
    private String shopDiscount;

    /**
     * 实际支付金额
     */
    private BigDecimal shopPayMoney;

    /**
     * 挂账桌子id
     */
    private Long ordersGoodsId;

    /**
     * 管理员结束订单id
     */
    private Long sysUserId;

    @TableField(exist = false)
    private String tearoomName;

    @TableField(exist = false)
    private String tearoomTitleImage;

    @TableField(exist = false)
    private SelfCouponUser couponUser;

    @TableField(exist = false)
    private Tearoom tearoom;

    @TableField(exist = false)
    private String zhiUserName;

    @TableField(exist = false)
    private String feiUserName;

    @TableField(exist = false)
    private String payUserName;

    @TableField(exist = false)
    private String payAvatar;

    @TableField(exist = false)
    private String payPhone;

    @TableField(exist = false)
    private String iconImg;

    @TableField(exist = false)
    private BigDecimal payMinuteMoney;

    @TableField(exist = false)
    private String openUserName;

    @TableField(exist = false)
    private String closeUserName;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

    public Orders() {}
}
