package com.sqx.modules.app.entity.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/4/26 10:00
 */
@Data
public class DistributionInfoDTO {
    private Long firstUserId;
    private BigDecimal firstIncom;
    private String firstUserName;
    private Long secondUserId;
    private BigDecimal secondIncom;
    private String secondUserName;
    private Long cityUserId;
    private BigDecimal cityIncom;
    private String cityUserName;
    private BigDecimal platformIncom;
}
