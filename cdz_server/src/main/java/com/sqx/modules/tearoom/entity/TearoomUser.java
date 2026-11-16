package com.sqx.modules.tearoom.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("tearoom_user")
public class TearoomUser {

    @TableId(type = IdType.AUTO)
    private Long tearoomUserId;

    /**
     * 门店id
     */
    private Long tearoomId;

    /**
     * 员工id
     */
    private Long userId;

    /**
     * 上班时间
     */
    private String startTime;

    /**
     * 下班时间
     */
    private String endTime;

    /**
     * 状态 1正常 2上班 3下班
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;


    /**
     * 语音播报 1开 2关
     */
    private Integer isYuyin;

    /**
     * 门店状态
     */
    private Integer isStatus;

    /**
     * 排号
     */
    private Integer isPaihao;

    /**
     * 提现
     */
    private Integer isTixian;

    /**
     * 会员权益
     */
    private Integer isHuiyuanquanyi;

    /**
     * 总收益
     */
    private Integer isShouyi;

    /**
     * 桌台管理
     */
    private Integer isZhuotaiguanli;

    /**
     * 桌台数据
     */
    private Integer isZhuotaishuju;

    /**
     * 实物核销
     */
    private Integer isShiwuhexiao;

    /**
     * 会员管理
     */
    private Integer isHuiyuanguanli;

    /**
     * 商家开台
     */
    private Integer isShangjiakaitai;

    /**
     * 点餐管理
     */
    private Integer isDiancanguanli;

    /**
     * 赠送优惠券
     */
    private Integer isZengsongyouhuijuan;

    /**
     * 活动中心
     */
    private Integer isHuodongzhongxin;

    /**
     * 点歌管理
     */
    private Integer isDiange;

    /**
     * 门店会员
     */
    private Integer isMendianhuiyuan;

    /**
     * 员工管理
     */
    private Integer isYuangongguanli;

    /**
     * 商家开台统计
     */
    private Integer isShangjiakaitaitongji;

    /**
     * 订单管理
     */
    private Integer isDingdanguanli;

    /**
     * 待结算统计
     */
    private Integer isDaijiesuan;

    /**
     * 开台收益
     */
    private Integer isKaitaishouyi;

    /**
     * 核销统计
     */
    private Integer isHexiaotongji;

    /**
     * 消费排行榜
     */
    private Integer isXiaofeipaihang;

    /**
     * 时长统计
     */
    private Integer isShichangtongji;

    /**
     * 收益明细
     */
    private Integer isShouyimingxi;

    /**
     * 账单对账
     */
    private Integer isZhangdantongji;

}
