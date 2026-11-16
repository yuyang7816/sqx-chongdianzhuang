package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoneyDetails;

import java.math.BigDecimal;

public interface UserMoneyDetailsService extends IService<UserMoneyDetails> {

    Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type);

    Double monthIncome(String date,Long userId);

    Double selectSumMoneyByClassifyAndUserId(Long userId,Integer classify,String time,String flag);

    Result selectConsortiaPersonMoney(Integer page,Integer limit, Long consortiaId, Long userId);

    Result selectUserMoneyDetailsUserIdAndByUserId(Integer page,Integer limit,Long userId,Long byUserId,Integer classify);

    BigDecimal selectRefundMoney(Integer flag, String time);

    BigDecimal selectFenXiaoMoney(Long userId);

    BigDecimal selectOrdersMoney();

    BigDecimal selectSumRefund(Long userId,String startTime,String endTime);

    Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type,Long shopUserId);

    BigDecimal selectSumMoneyByClassify(Integer classify,String time,Integer flag);

}
