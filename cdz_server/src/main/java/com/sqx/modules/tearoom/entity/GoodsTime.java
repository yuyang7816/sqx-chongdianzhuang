package com.sqx.modules.tearoom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description goods_time
 * @author fang
 * @date 2025-02-19
 */
@Data
public class GoodsTime implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 超时费用
     */
    private Long goodsTimeId;

    /**
     * 门店id
     */
    private Long tearoomId;

    /**
     * 开始时间
     */
    private String startTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 小时价格
     */
    private BigDecimal hourPrice;

    /**
     * 分钟价格
     */
    private BigDecimal minutePrice;

    /**
     * 会员小时价格
     */
    private BigDecimal memberHourPrice;

    /**
     * 会员分钟价格
     */
    private BigDecimal memberMinutePrice;

    /**
     * 用户每度电价格
     */
    private BigDecimal electricityPrice;

    /**
     * 会员每度电价格
     */
    private BigDecimal memberElectricityPrice;

    /**
     * 创建时间
     */
    private String createTime;

    public GoodsTime() {}
}
