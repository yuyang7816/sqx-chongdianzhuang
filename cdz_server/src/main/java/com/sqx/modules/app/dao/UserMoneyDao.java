package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.app.entity.UserMoney;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

@Mapper
public interface UserMoneyDao extends BaseMapper<UserMoney> {

    void updateMayMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") Double money);

    void updateInviteMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") Double money);

    void updateShopMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") Double money);

    void updateSumShopMoney(@Param("type") Integer type, @Param("userId") Long userId, @Param("money") Double money);

    Double selectSumMoney();

    void updateCashDeposit(@Param("type") Integer type, @Param("userId") Long userId,@Param("cashDeposit") Double cashDeposit);
}
