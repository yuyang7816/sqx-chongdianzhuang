package com.sqx.modules.tearoom.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PriceResult {
    private Integer goodsId;       // 商品ID
    private String queryTime;      // 时间点（HH:mm）
    private BigDecimal hourPrice;  // 小时价格
    private BigDecimal minutePrice;// 分钟价格
    private Integer matchedRuleType; // 匹配的规则类型（1=普通日，2=节假日）

}

