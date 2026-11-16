package com.sqx.modules.app.controller.app;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.service.AppService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.respository.SelfCouponUserRepository;
import com.sqx.modules.file.utils.Md5Utils;
import com.sqx.modules.integral.entity.UserIntegral;
import com.sqx.modules.integral.service.UserIntegralService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.tearoom.dao.TearoomUserDao;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.entity.TearoomUser;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.tearoom.service.TearoomService;
import com.sqx.modules.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * APP登录授权
 */
@Slf4j
@RestController
@RequestMapping("/app/user")
@Api(value = "APP管理", tags = {"APP管理"})
public class AppController {

    public static final String MALE = "male";
    @Autowired
    private UserService userService;
    @Autowired
    private AppService appService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private UserVipService userVipService;
    @Autowired
    private SelfCouponUserRepository selfCouponUserRepository;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserIntegralService userIntegralService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private TearoomUserDao tearoomUserDao;


    @PostMapping("/test")
    @ApiOperation("")
    public void ss(HttpServletRequest request, HttpServletResponse response){
        // 设置请求和响应的字符编码
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        response.setCharacterEncoding("UTF-8");

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 这里 content 包含了整个请求体的内容
        String requestBody = content.toString();
        System.out.println("请求体内容: " + requestBody);

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        out.println("code=500" );
    }

    @GetMapping("/selectBirthdayUserCount")
    @ApiOperation("查询生日用户")
    @ResponseBody
    public Result selectBirthdayUserCount(){
        LocalDate currentDate = LocalDate.now();
        // 获取当前月份
        int currentMonth = currentDate.getMonthValue();
        // 获取下一个月份的日期对象
        LocalDate nextMonthDate = currentDate.plusMonths(1);
        // 获取下一个月份
        int nextMonth = nextMonthDate.getMonthValue();
        String currentMonths=String.valueOf(currentMonth);
        if(currentMonth<10){
            currentMonths="0"+currentMonths;
        }
        String nextMonths=String.valueOf(nextMonth);
        if(nextMonth<10){
            nextMonths="0"+nextMonths;
        }
        int nowUserCount = userService.queryBirthdayCount(currentMonths);
        int nextUserCount = userService.queryBirthdayCount(nextMonths);
        Map<String,Object> result=new HashMap<>();
        result.put("nowMonth",currentMonths);
        result.put("nowUserCount",nowUserCount);
        result.put("nextMonth",nextMonths);
        result.put("nextUserCount",nextUserCount);
        return Result.success().put("data",result);
    }

    @PostMapping("/insertUser")
    @ApiOperation("添加用户")
    @ResponseBody
    public Result insertUser(@RequestBody UserEntity userEntity) {
        return userService.insertUser(userEntity);
    }

    @Login
    @PostMapping("/auditContent")
    @ApiOperation("审核内容")
    public Result auditContent(String content){
        String trim = content.trim();
        if(StringUtils.isEmpty(trim)){
            return Result.error(501,"不能为空！");
        }
        JSONObject jsonObject = BaiDuUtils.auditBaiDuText(content);
        if(jsonObject==null){
            return Result.error("文本审核失败！");
        }
        log.error("百度云审核活动名称返回值："+jsonObject.toJSONString());
        String conclusionType = jsonObject.getString("conclusionType");
        if(!"1".equals(conclusionType)){
            JSONArray data = jsonObject.getJSONArray("data");
            JSONArray hits = data.getJSONObject(0).getJSONArray("hits");
            JSONArray words = hits.getJSONObject(0).getJSONArray("words");
            if(words.size()>0){
                String string = words.getString(0);
                return Result.error("内容不合规关键词："+string);
            }else{
                return Result.error("内容不合规");
            }
        }
        return Result.success();
    }

    @Login
    @GetMapping("/selectCleanKeepingUserList")
    @ApiOperation("查询保洁用户")
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
                                 Long tearoomId,Integer isRate,Integer isVip,Integer vipIndex,Integer moneyUser,String vipNo,
                                 Integer isBirthday,String birthdayMonth,Integer integralNum,String ordersStartTime,String ordersEndTime,
                                 Integer userVipDetails,Long tearoomUser) {
        return Result.success().put("data", userService.selectUserPage(page, limit, phone, sex, platform, sysPhone, status, member,
                startTime, endTime, userName, inviterCode, isAuthentication, flag, date,tearoomId,isRate,isVip,vipIndex,moneyUser,vipNo,
                isBirthday,birthdayMonth,integralNum,ordersStartTime,ordersEndTime,userVipDetails,tearoomUser));
    }

    @Login
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

    @Login
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
        TearoomUser tearoomUser = tearoomUserDao.selectOne(new QueryWrapper<TearoomUser>().eq("tearoom_id", tearoomId).eq("user_id", userId));
        if(tearoomUser!=null){
            tearoomUserDao.deleteById(tearoomUser.getTearoomUserId());
        }
        return Result.success();
    }

    @Login
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

    @Login
    @PostMapping("/updateTearoomUser")
    @ApiOperation("设置员工权限")
    public Result updateTearoomUserTime(@RequestBody TearoomUser tearoomUser){
        if(tearoomUser.getTearoomUserId()!=null){
            tearoomUserDao.updateById(tearoomUser);
        }else{
            tearoomUser.setCreateTime(DateUtils.format(new Date()));
            tearoomUserDao.insert(tearoomUser);
        }
        return Result.success();
    }

    @Login
    @GetMapping("/selectTearoomUserTime")
    @ApiOperation("查找员工时间")
    public Result selectTearoomUserTime(@RequestAttribute Long userId,Long tearoomId){
        TearoomUser tearoomUser = tearoomUserDao.selectOne(new QueryWrapper<TearoomUser>().eq("tearoom_id", tearoomId).eq("user_id", userId));
        return Result.success().put("data",tearoomUser);
    }

    @Login
    @GetMapping("/selectTearoomUserTimeByUserId")
    @ApiOperation("查找员工时间")
    public Result selectTearoomUserTimeByUserId(Long userId,Long tearoomId){
        TearoomUser tearoomUser = tearoomUserDao.selectOne(new QueryWrapper<TearoomUser>().eq("tearoom_id", tearoomId).eq("user_id", userId));
        return Result.success().put("data",tearoomUser);
    }


    @Login
    @PostMapping("/deleteCleanKeepingUser")
    @ApiOperation("删除保洁用户")
    public Result deleteCleanKeepingUser(Long userId){
        userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                .set(UserEntity::getTearoomId,null)
                .eq(UserEntity::getUserId,userId));
        return Result.success();
    }


    @Login
    @GetMapping("/selectUserDetailsCount")
    @ApiOperation("查询用户信息数据数量")
    public Result selectUserDetailsCount(@RequestAttribute Long userId){

        //优惠券
        Integer yhjCount = selfCouponUserRepository.selectSelfCouponUserCount(2,userId);
        //团购劵
        Integer tgjCount = selfCouponUserRepository.selectSelfCouponUserCount(1,userId);
        //我的钱包
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(userId);
        //我的积分
        UserIntegral userIntegral = userIntegralService.selectById(userId);
        Map<String,Object> result=new HashMap<>();
        result.put("yhjCount",yhjCount);
        result.put("tgjCount",tgjCount);
        result.put("userMoney",userMoney.getMoney());
        result.put("userIntegral",userIntegral.getIntegralNum());
        return Result.success().put("data",result);
    }

    @Login
    @PostMapping("/updateOnLine")
    @ApiOperation("修改在线状态")
    public Result updateOnLine(@LoginUser UserEntity user, Integer onLine) {
        user.setOnLine(onLine);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @PostMapping("/updateSendMsg")
    @ApiOperation("修改是否接受消息推送")
    public Result updateSendMsg(@LoginUser UserEntity user,Integer isSendMsg){
        user.setIsSendMsg(isSendMsg);
        userService.updateById(user);
        return Result.success();
    }


    @Login
    @RequestMapping(value = "/updatePwd", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("用户端修改密码")
    public Result updatePwd(@LoginUser UserEntity user, String pwd, String oldPwd) {
        if (!user.getPassword().equals(DigestUtils.sha256Hex(oldPwd))) {
            return Result.error("原始密码不正确！");
        }
        if (pwd.equals(oldPwd)) {
            return Result.error("新密码不能与旧密码相同！");
        }
        user.setPassword(DigestUtils.sha256Hex(pwd));
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updatePhone", method = RequestMethod.POST)
    @ApiOperation("用户端换绑手机号")
    @ResponseBody
    public Result updatePhone(@RequestAttribute("userId") Long userId, @RequestParam String phone, @RequestParam String msg) {
        return userService.updatePhone(phone, msg, userId);
    }

    @Login
    @RequestMapping(value = "/updateUser", method = RequestMethod.POST)
    @ApiOperation("用户修改个人信息")
    @ResponseBody
    public Result updateUser(@RequestBody UserEntity userEntity,@RequestAttribute("userId") Long userId) {
        userEntity.setUserId(userId);
        userService.updateById(userEntity);
        if(StringUtils.isNotBlank(userEntity.getUserPhoto())){
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("name",userEntity.getUserName());
            jsonObject.put("personId",userEntity.getUserId());
            jsonObject.put("serialNumber",userEntity.getUserId());
            jsonObject.put("age",userEntity.getAge());
            if(userEntity.getSex()==1){
                jsonObject.put("gender","male");
            }else{
                jsonObject.put("gender","female");
            }
            jsonObject.put("phone",userEntity.getPhone());
            jsonObject.put("email","123@qq.com");

            jsonObject.put("certificateType","111");
            jsonObject.put("certificateNumber","");

            try {
                String base64 = ImageToBase64.convertImageUrlToBase64(userEntity.getUserPhoto());
                JSONArray jsonArray=new JSONArray();
                JSONObject jsonObject1=new JSONObject();
                jsonObject1.put("base64",base64);
                jsonArray.add(jsonObject1);
                jsonObject.put("pictureData",jsonArray);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String url = commonInfoService.findOne(1109).getValue();
            log.info("人脸请求参数:"+jsonObject.toJSONString());
            String s = HttpClientUtil.doPostJsonSXT(url+"/openApi/faceCamera/v1.0.0/uploadPersonInfo/001518230499",
                    jsonObject.toJSONString(), roomService.getDamToken());
            log.info("人脸调用返回值："+s);
        }
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserImageUrl", method = RequestMethod.POST)
    @ApiOperation("用户修改头像")
    @ResponseBody
    public Result updateUserImageUrl(@LoginUser UserEntity user, String avatar) {
        user.setAvatar(avatar);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserVipNo", method = RequestMethod.POST)
    @ApiOperation("修改会员编号")
    @ResponseBody
    public Result updateUserVipNo(Long userId, String vipNo) {
        UserEntity user=new UserEntity();
        user.setUserId(userId);
        user.setVipNo(vipNo);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/updateUserName", method = RequestMethod.POST)
    @ApiOperation("用户修改昵称")
    @ResponseBody
    public Result updateUserName(@LoginUser UserEntity user, String userName) {
        user.setUserName(userName);
        userService.updateById(user);
        return Result.success();
    }

    @Login
    @RequestMapping(value = "/selectUserById", method = RequestMethod.GET)
    @ApiOperation("获取用户详细信息")
    @ResponseBody
    public Result selectUserById(@LoginUser UserEntity user, HttpServletRequest request) {
        return userService.selectUserDetails(user, request);
    }


    @RequestMapping(value = "/selectNewApp", method = RequestMethod.GET)
    @ApiOperation("升级检测")
    @ResponseBody
    public Result selectNewApp() {
        return Result.success().put("data", appService.selectNewApp());
    }

    @RequestMapping(value = "/updateClientId", method = RequestMethod.POST)
    @ApiOperation("用户端绑定消息推送clientId")
    @ResponseBody
    @Login
    public Result updateClientId(String clientId,@RequestAttribute Long userId,Integer sysPhone) {
        UserEntity userEntity = userService.getById(userId);
        if (userEntity != null) {
            //检查是否有其他用户设置这个clientId 如果有 则清除其他用户
            userService.updateUserClientIdIsNull(clientId);
            userEntity.setClientid(clientId);
            userEntity.setSysPhone(sysPhone);
            userService.updateById(userEntity);
            return Result.success();
        }
        return Result.error("用户不存在！");
    }

    @GetMapping("/selectUserByUserId")
    @ApiOperation("查看用户信息")
    @ResponseBody
    @Login
    public Result selectUserByUserId(@LoginUser UserEntity user,Long userId){
        return userService.selectUserByUserId(userId,user.getUserId());
    }

    @GetMapping("/selectUserDetailsByUserId")
    @ApiOperation("查询用户主页信息")
    @ResponseBody
    public Result selectUserDetailsByUserId(Long userId){
        return userService.selectUserDetailsByUserId(userId);
    }

    @GetMapping("/selectUserByOrdersCount")
    @ApiOperation("根据接单数量进行排序")
    public Result selectUserByOrdersCount(Integer page,Integer limit){
        return userService.selectUserByOrdersCount(page, limit);
    }

    @GetMapping("/selectUserByGood")
    @ApiOperation("查询给我点赞的用户列表")
    public Result selectUserByGood(Integer page,Integer limit,Long userId){
        return userService.selectUserByGood(page, limit, userId);
    }

    @GetMapping("/selectUserByPhone")
    @ApiOperation("根据手机号搜索用户")
    public Result selectUserByPhone(String phone){
        return Result.success().put("data",userService.queryByPhone(phone));
    }

    @PostMapping("/updateUserVip")
    @ApiOperation("修改用户")
    public Result updateUserVip(@RequestBody UserEntity userEntity) {
        userService.updateById(userEntity);
        if(userEntity.getVipIndex()==0){
            userService.update(null, Wrappers.<UserEntity>lambdaUpdate()
                    .set(UserEntity::getIsSysVip,null)
                    .eq(UserEntity::getUserId,userEntity.getUserId()));
        }
        return Result.success();
    }



}
