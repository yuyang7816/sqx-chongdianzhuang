package com.sqx.modules.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *  充值记录
 * @author fang 2020-05-14
 */
@Data
@TableName("pay_details")
public class PayDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 充值记录id
     */
    @TableId(type = IdType.INPUT)
    private Long id;

    /**
     * 分类（1微信 1app微信 2微信公众号 3微信小程序 4支付宝app 5支付宝h5）
     */
    private Integer classify;

    /**
     * 订单id
     */
    private String orderId;

    /**
     * 支付宝交易订单号
     */
    private String tradeNo;

    /**
     * 充值金额
     */
    private Double money;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 0待支付 1支付成功 2失败
     */
    private Integer state;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 支付时间
     */
    private String payTime;

    /**
     * 支付类型 1充值 2订单 3预约 4优惠券 5结束 6补费 9续费 10助教 11助教保证金  12柜子订单
     */
    private Integer type;

    private String remark;

    private BigDecimal refundMoney;

    /**
     * 关联id
     */
    private Long parentId;

    @TableField(exist = false)
    private String refundContent;

    @TableField(exist = false)
    private String outRequestNo;

}
