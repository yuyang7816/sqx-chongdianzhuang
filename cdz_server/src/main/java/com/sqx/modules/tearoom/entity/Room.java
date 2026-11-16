package com.sqx.modules.tearoom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @description room
 * @author fang
 * @date 2023-08-17
 */
@Data
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 设备id
     */
    @TableId(type = IdType.AUTO)
    private Long roomId;

    /**
     * 商品id
     */
    private Long goodsId;

    /**
     * 设备名称
     */
    private String roomName;

    /**
     * 设备编号
     */
    private String roomNo;

    /**
     * 继电器序号
     */
    private String roomSelect;

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 重量
     */
    private BigDecimal roomWeight;

    /**
     * 服务编号
     */
    private String serveCode;

    /**
     * 模型编号
     */
    private String equipmentCode;

    /**
     * 设备类型 1设备 2电闸 3闪开闪关 4语音 5称重
     */
    private String roomType;

    /**
     * 开启语音
     */
    private String musicSelectStart;

    /**
     * 关闭语音
     */
    private String musicSelectEnd;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 开关 1开启 2关闭
     */
    private Integer isOpen;

    public Room() {}
}
