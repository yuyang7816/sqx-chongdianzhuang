package com.sqx.modules.tearoom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;


/**
 * @description room_time
 * @author fang
 * @date 2023-08-17
 */
@Data
public class RoomTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 房间时间id
     */
    private Long roomTimeId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 占用日期
     */
    private String roomDate;

    /**
     * 占用小时
     */
    private String roomTime;

    /**
     * 订单号
     */
    private String ordersNo;

    /**
     * 创建时间
     */
    private String createTime;

    public RoomTime() {}
}
