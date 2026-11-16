package com.sqx.modules.orders.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description orders_video
 * @author fang
 * @date 2025-03-05
 */
@Data
public class OrdersVideo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 订单视频id
     */
    private Long ordersVideoId;

    /**
     * 订单id
     */
    private Long ordersId;

    /**
     * 棋牌室id
     */
    private Long tearoomId;

    /**
     * 门店摄像头标识
     */
    private String tearoomCameraId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 门店摄像头标识
     */
    private String goodsCameraId;

    /**
     * 视频内容
     */
    private String videoUrl;

    /**
     * 类型 1原视频 2剪辑后
     */
    private Integer classify;

    /**
     * 视频时间
     */
    private String videoTime;

    /**
     * 创建时间
     */
    private String createTime;

    @TableField(exist = false)
    private String montageVideoUrl;

    @TableField(exist = false)
    private String tearoomName;

    @TableField(exist = false)
    private String goodsName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    @TableField(exist = false)
    private String ordersNo;

    @TableField(exist = false)
    private Long userId;

    public OrdersVideo() {}
}
