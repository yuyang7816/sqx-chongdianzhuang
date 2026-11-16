package com.sqx.modules.pay.service;

import com.sqx.common.utils.Result;
import org.springframework.web.bind.annotation.RequestAttribute;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;


/**
 * @author fang
 * @date 2020/2/26
 */
public interface WxService {

    String payBack(String resXml,Integer type);

    Result payMoney(Long payClassifyId, Integer classify, Long userId, HttpServletRequest request) throws Exception;

    Result payOrders(String ordersNo, Integer classify,Long userId, HttpServletRequest request) throws Exception;

    Result payCoupon(Long couponIssueId, Integer classify,Long userId, HttpServletRequest request) throws Exception;

    Result payMember(Long vipDetailsId, Integer classify, Long userId, HttpServletRequest request) throws Exception;

    boolean refund(String ordersNo,BigDecimal payMoney,BigDecimal refundMoney);

    String payBacks(String resXml, Integer type);

}