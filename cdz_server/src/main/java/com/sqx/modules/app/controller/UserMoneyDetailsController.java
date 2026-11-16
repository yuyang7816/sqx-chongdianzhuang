package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.pay.entity.PayDetails;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/Details")
@AllArgsConstructor
@Api("钱包明细")
public class UserMoneyDetailsController {

    private UserMoneyService userMoneyService;
    private UserMoneyDetailsService userMoneyDetailsService;
    private UserService userService;

    @ApiOperation("钱包明细")
    @GetMapping("/queryUserMoneyDetails")
    public Result queryUserMoneyDetails(Integer page, Integer limit, Long userId,Integer classify,Integer type) {

        return userMoneyDetailsService.queryUserMoneyDetails(page, limit, userId,classify,type);
    }

    @GetMapping("/selectShopUserMoneyDetailsList")
    @ApiOperation("余额明细")
    public Result selectShopUserMoneyDetailsList(Long userId, Long page, Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.selectShopUserMoneyDetailsList(userId, pages));
    }

    @GetMapping("/selectUserVipExp")
    @ApiOperation("余额明细")
    public Result selectUserVipExp(Integer page,Integer limit,@RequestAttribute Long userId,String userName,String phone,
                                   String startTime,String endTime,String ordersNo) {
        return Result.success().put("data", userService.selectUserVipExp(page, limit, userId, userName, phone, startTime, endTime, ordersNo));
    }

    @PostMapping("/updateShopMoney")
    @ApiOperation("修改商家金额")
    public Result updateShopMoney(Integer type,Long userId,Double money){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        if(type==1){
            userMoneyService.updateShopMoney(1,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setClassify(5);
            userMoneyDetails.setShopUserId(userId);
            userMoneyDetails.setContent("系统充值："+money);
            userMoneyDetails.setTitle("系统充值金额");
            userMoneyDetails.setType(1);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
        }else{
            userMoneyService.updateShopMoney(2,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setClassify(5);
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setShopUserId(userId);
            userMoneyDetails.setContent("系统减少："+money);
            userMoneyDetails.setTitle("系统减少金额");
            userMoneyDetails.setType(2);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
        }
        return Result.success();
    }

    @PostMapping("/cashShopMoney")
    @ApiOperation("结算商家金额")
    public Result cashShopMoney(Long userId,Double money){
        userMoneyService.updateShopMoney(2,userId,money);
        userMoneyService.updateSumShopMoney(1,userId,money);
        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setClassify(5);
        userMoneyDetails.setMoney(BigDecimal.valueOf(money));
        userMoneyDetails.setShopUserId(userId);
        userMoneyDetails.setContent("减少："+money);
        userMoneyDetails.setTitle("账户结算");
        userMoneyDetails.setType(2);
        userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        return Result.success();
    }

    @GetMapping("/selectMyMoney")
    @ApiOperation("我的钱包余额")
    public Result selectMyMoney(Long userId) {
        return Result.success().put("data", userMoneyService.selectUserMoneyByUserId(userId));
    }

    @GetMapping("/balanceDetailed")
    @ApiOperation("余额明细")
    public Result balanceDetailed(Long userId, Long page, Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.balanceDetailed(userId, pages));
    }

    @GetMapping("/selectInviteMoneyDetailsUserId")
    @ApiOperation("收益")
    public Result selectInviteMoneyDetailsUserId(Long userId, Long page, Long limit) {
        Page<UserMoneyDetails> pages = new Page<>(page, limit);
        return Result.success().put("data", userMoneyService.selectInviteMoneyDetailsUserId(userId, pages));
    }

    @GetMapping("/selectGiveTakeMoneyCount")
    @ApiOperation("转赠接受金额统计")
    public Result selectGiveTakeMoneyCount(String time,Integer flag){
        BigDecimal giveMoney=userMoneyDetailsService.selectSumMoneyByClassify(10,time,flag);
        BigDecimal takeMoney=userMoneyDetailsService.selectSumMoneyByClassify(20,time,flag);
        Map<String,Object> result=new HashMap<>();
        result.put("giveMoney",giveMoney);
        result.put("takeMoney",takeMoney);
        return Result.success().put("data",result);
    }

}
