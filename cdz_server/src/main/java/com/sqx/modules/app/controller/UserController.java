package com.sqx.modules.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.annotation.SysLog;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.dao.UserMoneyDao;
import com.sqx.modules.app.entity.*;
import com.sqx.modules.app.response.HomeMessageResponse;
import com.sqx.modules.app.response.UserMessageResponse;
import com.sqx.modules.app.service.*;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.respository.SelfCouponUserRepository;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.service.UserIntegralService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.pay.entity.PayDetails;
import com.sqx.modules.pay.service.PayDetailsService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.dao.TearoomUserDao;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.entity.TearoomUser;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.TearoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author fang
 * @date 2020/7/30
 */
@RestController
@Api(value = "用户管理", tags = {"用户管理"})
@RequestMapping(value = "/user")
public class UserController  extends AbstractController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private PayDetailsService payDetailsService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private PayDetailsDao payDetailsDao;
    @Autowired
    private MessageService messageService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserMoneyDao userMoneyDao;
    @Autowired
    private SelfCouponUserRepository selfCouponUserRepository;
    @Autowired
    private TearoomUserDao tearoomUserDao;

    @PostMapping("/insertUser")
    @ApiOperation("添加用户")
    @ResponseBody
    public Result insertUser(@RequestBody UserEntity userEntity) {
        return userService.insertUser(userEntity);
    }

    @RequestMapping(value = "/updateUserVipNo", method = RequestMethod.POST)
    @ApiOperation("修改会员编号")
    @ResponseBody
    public Result updateUserVipNo(Long userId, String vipNo) {
        UserEntity user = userService.selectUserById(userId);
        user.setVipNo(vipNo);
        userService.updateById(user);
        return Result.success();
    }


    @RequestMapping(value = "/selectUserByInvitationCode", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserByInvitationCode(String invitationCode) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByInvitationCode(invitationCode);
        Long userId=userEntity.getUserId();
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);

        //查询用户钱包

        Double inviteMoney = userMoney.getInviteMoney().doubleValue();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
        //查询邀请人数
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id",userId));
        if(userVip!=null){
            userEntity.setIsVip(userVip.getIsVip());
            userEntity.setVipEndTime(userVip.getEndTime());
        }
        if(userEntity.getTearoomId()!=null){
            Tearoom tearoom = tearoomService.getById(userEntity.getTearoomId());
            if(tearoom!=null){
                userEntity.setTearoomName(tearoom.getTearoomName());
            }
        }
        if(userEntity.getUserGoodsId()!=null){
            Goods goods = goodsService.getById(userEntity.getUserGoodsId());
            if(goods!=null){
                userEntity.setUserGoodsName(goods.getGoodsName());
            }
        }
        UserIntegral userIntegral = userIntegralService.selectById(userId);
        map.put("userEntity", userEntity);
        map.put("money", userMoney.getMoney());
        map.put("shopMoney", userMoney.getShopMoney());
        map.put("inviteMoney", inviteMoney);
        map.put("cashDeposit", userMoney.getCashDeposit());
        map.put("consume", consume);
        map.put("income", income);
        map.put("count", count);
        map.put("userIntegral", userIntegral.getIntegralNum());
        return Result.success().put("data", map);
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@ApiParam("用户id") @PathVariable Long userId) {
        Map<String, Object> map = new HashMap<>();
        UserEntity userEntity = userService.queryByUserId(userId);
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);

        //查询用户钱包

        Double inviteMoney = userMoney.getInviteMoney().doubleValue();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(new Date());
        //查询本月充值
        Double consume = payDetailsService.instantselectSumPay(date, userId);
        //查询本月提现
        Double income = userMoneyDetailsService.monthIncome(date, userId);
        //查询邀请人数
        int count = userService.queryInviterCount(userEntity.getInvitationCode());
        UserVip userVip = userVipService.getOne(new QueryWrapper<UserVip>().eq("user_id",userId));
        if(userVip!=null){
            userEntity.setIsVip(userVip.getIsVip());
            userEntity.setVipEndTime(userVip.getEndTime());
        }
        if(userEntity.getTearoomId()!=null){
            Tearoom tearoom = tearoomService.getById(userEntity.getTearoomId());
            if(tearoom!=null){
                userEntity.setTearoomName(tearoom.getTearoomName());
            }
        }
        if(userEntity.getUserGoodsId()!=null){
            Goods goods = goodsService.getById(userEntity.getUserGoodsId());
            if(goods!=null){
                userEntity.setUserGoodsName(goods.getGoodsName());
            }
        }
        UserIntegral userIntegral = userIntegralService.selectById(userId);
        map.put("userEntity", userEntity);
        map.put("money", userMoney.getMoney());
        map.put("shopMoney", userMoney.getShopMoney());
        map.put("inviteMoney", inviteMoney);
        map.put("cashDeposit", userMoney.getCashDeposit());
        map.put("consume", consume);
        map.put("income", income);
        map.put("count", count);
        map.put("userIntegral", userIntegral.getIntegralNum());
        return Result.success().put("data", map);
    }

    @PostMapping("/updateSafetyMoney")
    @ApiOperation("修改")
    public Result updateSafetyMoney(Long userId,Integer type, BigDecimal money){
        userMoneyDao.updateCashDeposit(type, userId, money.doubleValue());
        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setClassify(1);
        userMoneyDetails.setUserId(userId);
        if(type==1){
            userMoneyDetails.setTitle("[保证金]增加保证金");
            userMoneyDetails.setContent("系统增加保证金");
        }else{
            userMoneyDetails.setTitle("[保证金]减少保证金");
            userMoneyDetails.setContent("系统减少保证金");
        }
        userMoneyDetails.setType(type);
        userMoneyDetails.setMoney(money);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        if(userMoney.getCashDeposit().doubleValue()>0){
            UserEntity userEntity = userService.selectUserById(userId);
            userEntity.setIsCashDeposit(2);
            userService.updateById(userEntity);
        }else{
            UserEntity userEntity = userService.selectUserById(userId);
            userEntity.setIsCashDeposit(1);
            userService.updateById(userEntity);
        }
        return Result.success();
    }

    @RequestMapping(value = "/addCannotMoney/{userId}/{money}/{type}", method = RequestMethod.POST)
    @ApiOperation("修改金额")
    @ResponseBody
    public Result addCannotMoney(@PathVariable("userId") Long userId,@PathVariable("money") Double money,@PathVariable("type") Integer type,Integer isVip){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        if(type==1){
            userMoneyService.updateMoney(1,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统充值："+money);
            userMoneyDetails.setTitle("系统充值金额");
            userMoneyDetails.setType(1);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
            PayDetails payDetails = new PayDetails();
            payDetails.setState(1);
            payDetails.setCreateTime(DateUtils.format(new Date()));
            payDetails.setOrderId("系统充值");
            payDetails.setUserId(userId);
            payDetails.setMoney(money);
            payDetails.setClassify(6);
            payDetails.setType(1);
            payDetailsDao.insert(payDetails);
        }else{
            userMoneyService.updateMoney(2,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统减少："+money);
            userMoneyDetails.setTitle("系统减少金额");
            userMoneyDetails.setType(2);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
            PayDetails payDetails = new PayDetails();
            payDetails.setState(1);
            payDetails.setCreateTime(DateUtils.format(new Date()));
            payDetails.setOrderId("系统减少");
            payDetails.setUserId(userId);
            payDetails.setMoney(-money);
            payDetails.setClassify(6);
            payDetails.setType(1);
            payDetailsDao.insert(payDetails);
        }
        return Result.success();
    }

    @RequestMapping(value = "/updateInviteMoney/{userId}/{money}/{type}", method = RequestMethod.POST)
    @ApiOperation("修改收益金额")
    @ResponseBody
    public Result updateInviteMoney(@PathVariable("userId") Long userId,@PathVariable("money") Double money,@PathVariable("type") Integer type){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        if(type==1){
            userMoneyService.updateInviteMoney(1,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setClassify(3);
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统增加："+money);
            userMoneyDetails.setTitle("系统修改收益");
            userMoneyDetails.setType(1);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
        }else{
            userMoneyService.updateInviteMoney(2,userId,money);
            UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
            userMoneyDetails.setMoney(BigDecimal.valueOf(money));
            userMoneyDetails.setClassify(3);
            userMoneyDetails.setUserId(userId);
            userMoneyDetails.setContent("系统减少："+money);
            userMoneyDetails.setTitle("系统修改收益");
            userMoneyDetails.setType(2);
            userMoneyDetails.setCreateTime(format);
            userMoneyDetailsService.save(userMoneyDetails);
        }
        return Result.success();
    }


    @RequestMapping(value = "/selectUserList", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserList(Integer page, Integer limit,
                                 @ApiParam("用户id 手机号 昵称 模糊搜索") String phone,
                                 @ApiParam("性别 1男 2女") Integer sex,
                                 @ApiParam("来源  ") String platform,
                                 @ApiParam("手机终端") String sysPhone,
                                 @ApiParam("-1所有人 0普通用户 1会员用户") Integer member,
                                 @ApiParam("开始时间") String startTime,
                                 @ApiParam("结束时间") String endTime,
                                 @ApiParam("用户昵称") String userName,
                                 @ApiParam("邀请码") String inviterCode,
                                 @ApiParam("状态 1正常 2封禁") Integer status,
                                 @ApiParam(" 1普通用户 2团长 3保洁") Integer isAuthentication,Integer flag,String date,
                                 Long tearoomId,Integer isRate,Integer isVip,Integer vipIndex,Integer moneyUser,
                                 String vipNo,Integer isBirthday,String birthdayMonth,Integer integralNum,
                                 String ordersStartTime,String ordersEndTime,Integer userVipDetails,Long tearoomUser) {
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status,member,
                startTime,endTime,userName,inviterCode,isAuthentication,flag,date,tearoomId,isRate,isVip,vipIndex,moneyUser,vipNo,
                isBirthday,birthdayMonth,integralNum,ordersStartTime,ordersEndTime,userVipDetails,tearoomUser));
    }

    @RequestMapping(value = "/selectUserLists", method = RequestMethod.GET)
    @ApiOperation("查询所有用户列表")
    @ResponseBody
    public Result selectUserLists(Integer page, Integer limit,
                                  @ApiParam("用户id 手机号 昵称 模糊搜索") String phone,
                                  @ApiParam("性别 1男 2女") Integer sex,
                                  @ApiParam("来源  ") String platform,
                                  @ApiParam("手机终端") String sysPhone,
                                  @ApiParam("-1所有人 0普通用户 1会员用户") Integer member,
                                  @ApiParam("开始时间") String startTime,
                                  @ApiParam("结束时间") String endTime,
                                  @ApiParam("用户昵称") String userName,
                                  @ApiParam("状态 1正常 2封禁") Integer status,
                                  @ApiParam("-1全部用户  0普通用户  1陪玩管用户") Integer isAuthentication,Integer flag,String date,
                                  Long tearoomId,Integer isRate,Integer isVip,Integer vipIndex,Integer moneyUser,String vipNo,
                                  Integer isBirthday,String birthdayMonth,Integer integralNum,
                                  String ordersStartTime,String ordersEndTime,Integer userVipDetails,Long tearoomUser) {
        String inviterCode=getUser().getInviterCode();
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status,member,
                startTime,endTime,userName,inviterCode,isAuthentication,flag,date,tearoomId,isRate,isVip,vipIndex,moneyUser,vipNo,
                isBirthday,birthdayMonth,integralNum,ordersStartTime,ordersEndTime,userVipDetails,tearoomUser));
    }


    @PostMapping("/insertTearoomUser")
    @ApiOperation("添加充电桩用户")
    public Result insertTearoomUser(String phone,Long tearoomId){
        UserEntity user = userService.queryByPhone(phone);
        if(user==null){
            return Result.error("用户不存在！");
        }
        Tearoom tearoom = tearoomService.getById(tearoomId);
        for (String s : tearoom.getEmployeeUserIds().split(",")) {
            if(s.equals(user.getUserId().toString())){
                return Result.error("该用户已添加！");
            }
        }
        tearoom.setEmployeeUserIds(tearoom.getEmployeeUserIds()+","+user.getUserId());
        tearoomService.updateById(tearoom);
        return Result.success();
    }


    @PostMapping("/deleteTearoomUser")
    @ApiOperation("删除充电桩用户")
    public Result deleteTearoomUser(Long userId,Long tearoomId){
        UserEntity user = userService.selectUserById(userId);
        if(user==null){
            return Result.error("用户不存在！");
        }
        Tearoom tearoom = tearoomService.getById(tearoomId);
        StringBuilder stringBuilder=new StringBuilder();
        for (String s : tearoom.getEmployeeUserIds().split(",")) {
            if(!s.equals(user.getUserId().toString())){
                stringBuilder.append(s).append(",");
            }
        }
        String employeeUserIds = stringBuilder.toString();
        if(!employeeUserIds.isEmpty()){
            employeeUserIds=employeeUserIds.substring(0,employeeUserIds.length()-1);
        }
        tearoomService.update(null, Wrappers.<Tearoom>lambdaUpdate()
                .set(Tearoom::getEmployeeUserIds,employeeUserIds)
                .eq(Tearoom::getTearoomId,tearoomId));

        return Result.success();
    }

    @PostMapping("/updateTearoomUserTime")
    @ApiOperation("设置员工时间")
    public Result updateTearoomUserTime(Long userId,Long tearoomId,String startTime,String endTime,Integer status){
        TearoomUser tearoomUser = tearoomUserDao.selectOne(new QueryWrapper<TearoomUser>().eq("tearoom_id", tearoomId).eq("user_id", userId));
        if(tearoomUser!=null){
            tearoomUser.setStartTime(startTime);
            tearoomUser.setEndTime(endTime);
            tearoomUser.setStatus(status);
            tearoomUserDao.updateById(tearoomUser);
        }else{
            tearoomUser=new TearoomUser();
            tearoomUser.setTearoomId(tearoomId);
            tearoomUser.setUserId(userId);
            tearoomUser.setStartTime(startTime);
            tearoomUser.setEndTime(endTime);
            tearoomUser.setStatus(status);
            tearoomUserDao.insert(tearoomUser);
        }
        return Result.success();
    }

    @RequestMapping(value = "/deleteUserByUserId/{userId}", method = RequestMethod.POST)
    @ApiOperation("删除用户")
    @ResponseBody
    public Result deleteUserByUserId(@PathVariable("userId") Long userId,Long messageId) {
        userService.removeById(userId);
        if(messageId!=null){
            MessageInfo messageInfo = messageService.getById(messageId);
            if(messageInfo!=null){
                messageInfo.setStatus(1);
                messageService.updateById(messageInfo);
            }
        }
        return Result.success();
    }

    @RequestMapping(value = "/updateUserByUserId", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserByUserId(@RequestBody UserEntity userEntity) {
        UserEntity userEntity1 = userService.selectUserById(userEntity.getUserId());
        if(StringUtils.isNotBlank(userEntity.getPhone())){
            if(userEntity1.getPhone()!=null && !userEntity1.getPhone().equals(userEntity.getPhone())){
                int phone = userService.count(new QueryWrapper<UserEntity>().eq("phone", userEntity.getPhone()));
                if(phone>0){
                    return Result.error("当前手机号已被注册！");
                }
            }
        }
        if(StringUtils.isNotBlank(userEntity.getInvitationCode())){
            userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                    .set(UserEntity::getInviterCode,userEntity.getInvitationCode())
                    .eq(UserEntity::getInviterCode,userEntity1.getInvitationCode()));
        }
        userService.updateById(userEntity);
        return Result.success();
    }

    @RequestMapping(value = "/updateUserVip", method = RequestMethod.POST)
    @ApiOperation("修改用户")
    @ResponseBody
    public Result updateUserVip(@RequestBody UserEntity userEntity) {
        userService.updateById(userEntity);
        if(userEntity.getVipIndex()==0){
            userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                    .set(UserEntity::getIsSysVip,null)
                    .eq(UserEntity::getUserId,userEntity.getUserId()));
        }
        return Result.success();
    }

    @PostMapping("/updateUserIntInviterCode")
    @ApiOperation("修改用户邀请人邀请码")
    public Result updateUserIntInviterCode(Long userId,String inviterCode){
        UserEntity userEntity = userService.selectUserById(userId);
        UserEntity userEntity1 = userService.queryByInvitationCode(inviterCode);
        if(userEntity1==null){
            return Result.error("邀请码用户不存在！");
        }
        userEntity.setInviterCode(inviterCode);
        userService.updateById(userEntity);
        return Result.success();
    }

    @PostMapping("/updateTearoomUser")
    @ApiOperation("修改充电桩用户")
    public Result updateTearoomUser(Long userId, Long tearoomId) {
        userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                .set(UserEntity::getTearoomId,tearoomId)
                .eq(UserEntity::getUserId,userId));
        return Result.success();
    }

    @PostMapping("/updateOnLine")
    @ApiOperation("修改在线状态")
    public Result updateOnLine(Long userId, Integer onLine) {
        UserEntity user = userService.getById(userId);
        user.setOnLine(onLine);
        userService.updateById(user);
        return Result.success();
    }



    @RequestMapping(value = "/updateUserStatusByUserId", method = RequestMethod.GET)
    @ApiOperation("禁用或启用用户")
    @ResponseBody
    public Result updateUserByUserId(Long userId) {
        UserEntity byId = userService.getById(userId);
        if (byId.getStatus().equals(1)) {
            byId.setStatus(2);
        } else {
            byId.setStatus(1);
        }
        userService.updateById(byId);
        return Result.success();
    }


    /**
     * 获取openid
     *
     * @param code 微信code
     * @return openid
     */
    @GetMapping("/openId/{code:.+}/{userId}")
    @ApiOperation("根据code获取openid")
    public Result getOpenid(@PathVariable("code") String code, @PathVariable("userId") Long userId) {
        return userService.getOpenId(code, userId);
    }

    /**
     * 信息分析
     *
     * @return
     */
    @GetMapping("/homeMessage")
    @ApiOperation("信息分析")
    public Result homeMessage() {
        HomeMessageResponse homeMessageResponse = new HomeMessageResponse();
        //   0查总   1查天  2查月  3查年
        //设置总用户人数
        homeMessageResponse.setTotalUsers(userService.queryUserCount(0, null));
        //设置今日新增
        homeMessageResponse.setNewToday(userService.queryUserCount(1, null));
        //设置本月新增
        homeMessageResponse.setNewMonth(userService.queryUserCount(2, null));
        //设置本年新增
        homeMessageResponse.setNewYear(userService.queryUserCount(3, null));

        //设置总收入
        homeMessageResponse.setTotalRevenue(userService.queryPayMoney(0));
        //设置今日收入
        homeMessageResponse.setTodayRevenue(userService.queryPayMoney(1));
        //设置本月收入
        homeMessageResponse.setMonthRevenue(userService.queryPayMoney(2));
        //设置本年收入
        homeMessageResponse.setYearRevenue(userService.queryPayMoney(3));
        //剩余钱包余额
        homeMessageResponse.setSumMoney(userMoneyService.selectSumMoney());
        //支出钱包余额
        homeMessageResponse.setOrdersMoney(userMoneyDetailsService.selectOrdersMoney());

        homeMessageResponse.setMtMoney(selfCouponUserRepository.selectCouponMoneyByPlatformType(1));
        homeMessageResponse.setDyMoney(selfCouponUserRepository.selectCouponMoneyByPlatformType(2));
        homeMessageResponse.setKsMoney(selfCouponUserRepository.selectCouponMoneyByPlatformType(3));

        return Result.success().put("data", homeMessageResponse);
    }

    /**
     * 接单分析
     *
     * @return
     */
    @GetMapping("/takingOrdersMessage")
    @ApiOperation("接单分析")
    public Result takingOrdersMessage(Long page, Long limit, String date, Long type) {
        Page<Map<String, Object>> iPage = new Page<>(page, limit);
        return userService.takingOrdersMessage(iPage, type, date);
    }

    /**
     * 用户分析
     */
    @GetMapping("/userMessage")
    @ApiOperation("用户分析")
    public Result userMessage(String date, int type) {
        UserMessageResponse userMessageResponse = new UserMessageResponse();
        userMessageResponse.setTotalNumber(userService.queryUserCount(type, date));
        userMessageResponse.setVipUserNumber(userService.userMessage(date, type));
        userMessageResponse.setUserNumber(userService.queryUserCount(type, date) - userService.userMessage(date, type));
        return Result.success().put("data", userMessageResponse);
    }

    @GetMapping("/userPlatform")
    @ApiOperation("用户来源统计")
    public Result userPlatForm(String data,Integer type){
        int appCount = userService.userPlatform("app", data, type);
        int wxCount = userService.userPlatform("小程序", data, type);
//        int qq = userService.userPlatform("qq", data, type);
        int wx = userService.userPlatform("公众号", data, type);


        int userCount = userService.queryUserCount(type, data);
        int userPhoneCount = userService.queryUserByPhoneCount(type, data);
        int userNullPhoneCount = userService.queryUserByNullPhoneCount(type, data);
        int userTearoomCount = userService.queryUserTearoomCount(type, data);
        Integer tearoomCount = tearoomService.queryTearoomCount(type, data);

        Map<String,Object> result=new HashMap<>();
        result.put("appCount",appCount);result.put("wxCount",wxCount);
        result.put("userCount",userCount);result.put("userPhoneCount",userPhoneCount);
        result.put("wx",wx);result.put("tearoomCount",tearoomCount);
        result.put("userNullPhoneCount",userNullPhoneCount);result.put("userTearoomCount",userTearoomCount);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectUserByOrdersCount")
    @ApiOperation("根据接单数量进行排序")
    public Result selectUserByOrdersCount(Integer page,Integer limit){
        return userService.selectUserByOrdersCount(page, limit);
    }

    @GetMapping("/selectUserCountStatisticsByTime")
    @ApiOperation("用户统计")
    public Result selectUserCountStatisticsByTime(String startTime,String endTime){
        List<Integer> userCountList=new ArrayList<>();
        List<String> year=new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parse);
        while (true){
            String dateTime = simpleDateFormat.format(calendar.getTime());
            int i = userService.queryUserCount(1, dateTime);
            userCountList.add(i);
            year.add(dateTime);
            if(dateTime.equals(endTime)){
                break;
            }
            calendar.add(Calendar.DATE,1);
        }
        Map<String,Object> result=new HashMap<>();
        result.put("userCountList",userCountList);
        result.put("year",year);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectTearoomUserTimeByUserId")
    @ApiOperation("查找员工时间")
    public Result selectTearoomUserTimeByUserId(Long userId,Long tearoomId){
        TearoomUser tearoomUser = tearoomUserDao.selectOne(new QueryWrapper<TearoomUser>().eq("tearoom_id", tearoomId).eq("user_id", userId));
        return Result.success().put("data",tearoomUser);
    }

    @PostMapping("/adminUpdateTearoomUserTime")
    @ApiOperation("设置员工权限")
    public Result adminUpdateTearoomUserTime(@RequestBody TearoomUser tearoomUser){
        if(tearoomUser.getTearoomUserId()!=null){
            tearoomUserDao.updateById(tearoomUser);
        }else{
            tearoomUser.setCreateTime(DateUtils.format(new Date()));
            tearoomUserDao.insert(tearoomUser);
        }
        return Result.success();
    }


}