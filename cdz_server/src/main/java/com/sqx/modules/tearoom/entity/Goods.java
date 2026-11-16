package com.sqx.modules.tearoom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.sqx.modules.orders.entity.Orders;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description goods
 * @author fang
 * @date 2023-08-17
 */
@Data
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @TableId(type = IdType.AUTO)
    private Long goodsId;

    /**
     * 充电桩id
     */
    private Long tearoomId;

    /**
     * 房间id
     */
    private Long roomId;

    /**
     * 商品名称
     */
    private String goodsName;

    /**
     * 商品封面图
     */
    private String goodsCoverImage;

    /**
     * 商品轮播图
     */
    private String goodsTitleImage;

    /**
     * 商品类型 使用人数
     */
    private String goodsType;

    /**
     * 售价
     */
    private BigDecimal price;

    /**
     * 销量
     */
    private Integer sales;

    /**
     * 套餐详情
     */
    private String goodsDetails;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 温馨提示
     */
    private String goodsTips;

    /**
     * 商品图文介绍
     */
    private String detailsImage;

    /**
     * 状态 1正常 2下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 必选产品 ids
     */
    private String productIds;


    /**
     * 房间wifi
     */
    private String roomWifi;

    /**
     * 房间编号
     */
    private String roomNo;

    /**
     * 房间密码
     */
    private String roomPwd;

    /**
     * 设备组编号
     */
    private String deviceNo;

    private String innerCode;

    /**
     * 标签
     */
    private String labels;

    /**
     * 绑定灯和锁球器
     */
    private String channels;

    /**
     * 当前使用状态 1空闲  2正在使用
     */
    private Integer goodsStatus;

    /**
     * 正在占用改的订单id
     */
    private Long ordersId;

    /**
     * 类型 1快充 2慢充
     */
    private Integer goodsCategory;


    /**
     * 预约费
     */
    private BigDecimal subscription;

    /**
     * 押金
     */
    private BigDecimal cashPledge;

    /**
     * 语音播报 token
     */
    private String yyToken;

    /**
     * 语音播报 id
     */
    private String yyId;

    /**
     * 摄像头桌子编号
     */
    private String cameraId;

    /**
     * 小图标
     */
    private String iconImg;

    /**
     * 是否是线下开台 1是
     */
    private Integer isOpen;

    /**
     * 重量
     */
    private BigDecimal weightNum;

    /**
     * 误差
     */
    private BigDecimal weightRange;

    /**
     * 小时价格
     */
    private BigDecimal hourPrice;


    /**
     * 分钟价格
     */
    private BigDecimal minutePrice;

    /**
     * 结束时间
     */
    @TableField(exist = false)
    private String ordersEndTime;

    @TableField(exist = false)
    private String payTime;

    @TableField(exist = false)
    private Long userId;

    @TableField(exist = false)
    private Orders orders;

    @TableField(exist = false)
    private Integer counts;

    @TableField(exist = false)
    private Double sumMoney;

    @TableField(exist = false)
    private List<Orders> ordersList;

    @TableField(exist = false)
    private Double ordersMinute;

    @TableField(exist = false)
    private Double ordersMoney;

    public Goods() {}
}
