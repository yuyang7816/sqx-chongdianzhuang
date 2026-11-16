package com.sqx.modules.app.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.UserVipDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.entity.UserVip;
import com.sqx.modules.app.entity.VipDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.app.service.UserVipService;
import com.sqx.modules.app.service.VipDetailsService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@Api(value = "会员管理", tags = {"会员管理"})
@RequestMapping(value = "/vipDetails")
public class VipDetailsController {

    @Autowired
    private VipDetailsService vipDetailsService;
    @Autowired
    private UserVipDao userVipDao;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @ApiParam("添加会员的详情信息")
    @PostMapping("/insertVipDetails")
    public Result insertVipDetails(@RequestBody VipDetails vipDetails) {
        return vipDetailsService.insertVipDetails(vipDetails);
    }


    @ApiParam("修改会员的详情信息")
    @PostMapping("/updateVipDetails")
    public Result updateVipDetails(@RequestBody VipDetails vipDetails) {
        vipDetailsService.updateById(vipDetails);
        return Result.success();
    }

    @ApiParam("删除的详情信息")
    @PostMapping("/deleteVipDetails")
    public Result deleteVipDetails(Long id) {
        vipDetailsService.removeById(id);
        return Result.success();
    }

    @ApiParam("查询会员列表")
    @GetMapping("/selectVipDetailsList")
    public Result selectVipDetailsList(Integer page,Integer limit) {
        return Result.success().put("data",new PageUtils(vipDetailsService.page(new Page<>(page,limit))));
    }

    @PostMapping("/giveUserVip")
    @ApiOperation("赠送用户会员")
    public Result giveUserVip(Long userId,Integer day){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //查看用户是否是会员
        UserVip userVip = userVipDao.selectOne(new QueryWrapper<UserVip>().eq("user_id", userId));
        Calendar cal = Calendar.getInstance();
        if(userVip!=null){
            Date endDate = null;
            try {
                endDate = sdf.parse(userVip.getEndTime());
                if (endDate != null && System.currentTimeMillis() < (endDate.getTime())) {
                    cal.setTime(endDate);
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }else{
            userVip=new UserVip();
            //设置会员类型
            userVip.setVipNameType(1);
            //设置开通会员的用户id
            userVip.setUserId(userId);
            //设置会员的购买时间
            userVip.setCreateTime(sdf.format(new Date()));
            userVip.setIsVip(1);
        }
        BigDecimal money=BigDecimal.ZERO;
        MessageInfo messageInfo=new MessageInfo();
        UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
        userMoneyDetails.setMoney(money);
        userMoneyDetails.setUserId(userId);

        userMoneyDetails.setContent("系统赠送"+day+"天会员");
        messageInfo.setContent("系统赠送"+day+"天会员");
        cal.add(Calendar.DAY_OF_MONTH, day);

        userVip.setEndTime(sdf.format(cal.getTime()));
        if(userVip.getVipId()!=null){
            userVipDao.updateById(userVip);
        }else{
            userVipDao.insert(userVip);
        }
        userMoneyDetails.setTitle("开通会员");
        userMoneyDetails.setType(2);
        userMoneyDetails.setCreateTime(sdf.format(new Date()));
        userMoneyDetailsService.save(userMoneyDetails);
        UserEntity userEntity = userService.selectUserById(userId);
        messageInfo.setTitle("开通会员");
        messageInfo.setState(String.valueOf(4));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(sdf.format(new Date()));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        if(StringUtil.isNotBlank(userEntity.getClientid())){
            userService.pushToSingle(messageInfo.getTitle(),messageInfo.getContent(),userEntity.getClientid());
        }
        return Result.success();
    }

    @PostMapping("/removeUserVip")
    @ApiOperation("取消用户会员")
    public Result removeUserVip(Long userId){
        UserVip userVip = userVipDao.selectOne(new QueryWrapper<UserVip>().eq("user_id", userId));
        if(userVip!=null){
            userVipDao.deleteById(userVip.getVipId());
        }
        UserEntity userEntity = userService.selectUserById(userId);
        MessageInfo messageInfo=new MessageInfo();
        messageInfo.setTitle("取消会员");
        messageInfo.setContent("系统取消了你的会员");
        messageInfo.setState(String.valueOf(4));
        messageInfo.setUserName(userEntity.getUserName());
        messageInfo.setUserId(String.valueOf(userEntity.getUserId()));
        messageInfo.setCreateAt(DateUtils.format(new Date()));
        messageInfo.setIsSee("0");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

}
