package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("user_money")
@ApiModel("用户钱包")
public class UserMoney implements Serializable {
    /**
     * 主键id
     */
    @ApiModelProperty("主键id")
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 钱包金额
     */
    @ApiModelProperty("钱包金额")
    private BigDecimal money;

    @ApiModelProperty("收益金额")
    private BigDecimal inviteMoney;

    /**
     * 保证金
     */
    @ApiModelProperty("保证金")
    private BigDecimal cashDeposit;

    /**
     * 商家钱包 当前金额
     */
    private BigDecimal shopMoney;

    /**
     * 累计收益
     */
    private BigDecimal sumShopMoney;

    /**
     * 用户id
     */
    @ApiModelProperty("用户id")
    @TableField("user_id")
    private Long userId;

}
