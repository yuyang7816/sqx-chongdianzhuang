package com.sqx.modules.app.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.coupon.respository.SelfCouponUserRepository;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.service.UserIntegralService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/userMoney")
@Api("app 钱包 ")
public class AppUserMoneyController {

    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private SelfCouponUserRepository selectSelfCouponUserCount;
    @Autowired
    private UserIntegralService userIntegralService;

    @Login
    @GetMapping("/selectUserDetails")
    @ApiOperation("用户数据")
    public Result selectUserDetails(@RequestAttribute Long userId) {
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        Integer couponCount = selectSelfCouponUserCount.selectSelfCouponUserCount(userId);
        UserIntegral userIntegral = userIntegralService.selectById(userId);
        Map<String,Object> result=new HashMap<>();
        result.put("money",userMoney.getMoney());
        result.put("couponCount",couponCount);
        result.put("userIntegral",userIntegral.getIntegralNum());
        return Result.success().put("data", result);
    }


    @Login
    @GetMapping("/selectConsortiaPersonMoneyDetails")
    @ApiOperation("团队收益明细")
    public Result selectConsortiaPersonMoneyDetails(Integer page, Integer limit, Long byUserId, @RequestAttribute Long userId, Integer classify) {
        return userMoneyDetailsService.selectUserMoneyDetailsUserIdAndByUserId(page, limit, userId, byUserId, classify);
    }

    @Login
    @GetMapping("/selectMyMoney")
    @ApiOperation("我的钱包余额")
    public Result selectMyMoney(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectUserMoneyByUserId(userId));
    }

    /**
     * 我的收益
     */
    @GetMapping("/selectMyProfit")
    @Login
    @ApiOperation("我的收益")
    public Result selectMyProfit(@RequestAttribute Long userId) {
        return Result.success().put("data", userMoneyService.selectMyProfit(userId));
    }



    /**
     * 余额明细
     */
    @GetMapping("/balanceDetailed")
    @Login
    @ApiOperation("余额明细")
    public Result balanceDetailed(@RequestAttribute Long userId, Long page, Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.balanceDetailed(userId, pages));
    }

    @GetMapping("/selectUserMoneyDetailsByUserId")
    @Login
    @ApiOperation("查询用户余额明细")
    public Result selectUserMoneyDetailsByUserId(Long userId, Long page, Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.balanceDetailed(userId, pages));
    }

    @GetMapping("/selectInviteMoneyDetailsUserId")
    @Login
    @ApiOperation("收益")
    public Result selectInviteMoneyDetailsUserId(@RequestAttribute Long userId, Long page, Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.selectInviteMoneyDetailsUserId(userId, pages));
    }



    @Login
    @GetMapping("/selectShopUserMoneyDetailsList")
    @ApiOperation("余额明细")
    public Result selectShopUserMoneyDetailsList(@RequestAttribute Long userId, Long page, Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.selectShopUserMoneyDetailsList(userId, pages));
    }

    @Login
    @GetMapping("/selectUserMoneyByUserId")
    @ApiOperation("查询钱包余额")
    public Result selectUserMoneyByUserId(Long userId) {
        return Result.success().put("data", userMoneyService.selectUserMoneyByUserId(userId));
    }

    @Login
    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit,@RequestAttribute Long userId,Integer classify,Integer type,Long shopUserId) {

        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type,shopUserId);
    }

    @Login
    @ApiOperation("钱包明细")
    @GetMapping("/queryShopMoneyDetails")
    public Result queryShopMoneyDetails(Integer page, Integer limit,Long userId,Integer classify,Integer type,Long shopUserId) {

        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type,shopUserId);
    }


}
