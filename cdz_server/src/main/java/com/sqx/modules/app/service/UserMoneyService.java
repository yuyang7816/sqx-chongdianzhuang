package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.math.BigDecimal;

public interface UserMoneyService extends IService<UserMoney> {

    UserMoney selectUserMoneyByUserId(Long userId);

    Double selectSumMoney();

    void updateMoney(int i, Long userId, double money);

    void updateInviteMoney(int i, Long userId, double money);

    void updateShopMoney(int i, Long userId, double money);

    void updateSumShopMoney(int i, Long userId, double money);

    Double selectMyProfit(Long userId);

    PageUtils balanceDetailed(Long userId, Page<UserMoneyDetails> ipage);

    PageUtils selectInviteMoneyDetailsUserId(Long userId, Page<UserMoneyDetails> pages);

    PageUtils selectShopUserMoneyDetailsList(Long userId, Page<UserMoneyDetails> pages);


}
