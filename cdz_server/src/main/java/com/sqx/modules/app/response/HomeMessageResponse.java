package com.sqx.modules.app.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 首页信息返回实体
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HomeMessageResponse implements Serializable {
    /**
     * 总用户数
     */
    private  int totalUsers;
    /**
     *今日新增
     */
    private  int newToday;
    /**
     *本月新增
      */
    private  int newMonth;
    /**
     * 本年新增
     */
    private  int newYear;
    /**
     * 总收入
     */
    private Double totalRevenue;
    /**
     * 今日收入
     */
    private Double todayRevenue;
    /**
     * 本月收入
     */
    private Double monthRevenue;
    /**
     * 本年收入
     */
    private Double yearRevenue;

    /**
     * 剩余会员卡金额
     */
    private Double sumMoney;

    /**
     * 支出会员卡金额
     */
    private BigDecimal ordersMoney;

    /**
     * 美团核销总金额
     */
    private BigDecimal mtMoney;

    /**
     * 抖音核销总金额
     */
    private BigDecimal dyMoney;

    /**
     * 快手核销总金额
     */
    private BigDecimal ksMoney;

}
