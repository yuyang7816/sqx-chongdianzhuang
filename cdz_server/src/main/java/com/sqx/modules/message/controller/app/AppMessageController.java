package com.sqx.modules.message.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "消息管理", tags = {"消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Login
    @RequestMapping(value = "/selectMessageByUserIds", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessageLists(int page, int limit,@RequestAttribute("userId") Long userId,Integer state){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("state",state);
        PageUtils pageUtils = messageService.selectMessageLists(map);
        messageService.updateSendState(userId,state);
        return Result.success().put("data",pageUtils);
    }

    @RequestMapping(value = "/selectMessageList", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessageList(int page, int limit,Integer state){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("state",state);
        PageUtils pageUtils = messageService.selectMessageList(map);
        return Result.success().put("data",pageUtils);
    }

    @RequestMapping(value = "/selectMessageByType", method = RequestMethod.GET)
    @ApiOperation("获取消息 type1为公告2位用户反馈 3为投诉消息 4为订单信息 5为用户消息 6客服消息 ")
    @ResponseBody
    public Result selectMessageByType(int page, int limit,Integer state,Integer platform,Integer status,String content){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",null);
        map.put("state",state);
        map.put("platform",platform);
        map.put("status",status);
        map.put("content",content);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @Login
    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(int page, int limit,@RequestAttribute("userId") Long userId,Integer state,Integer platform,Integer status){
        Map<String,Object> map=new HashMap<>();
        map.put("page",page);
        map.put("limit",limit);
        map.put("userId",userId);
        map.put("state",state);
        map.put("platform",platform);
        map.put("status",status);
        PageUtils pageUtils = messageService.selectMessageList(map);
        messageService.updateSendState(userId,state);
        return Result.success().put("data",pageUtils);
    }

    @Login
    @RequestMapping(value = "/selectMessageByUserIdLimit1", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessageByUserIdLimit1(@RequestAttribute("userId") Long userId){
        Map<String,Object> map=new HashMap<>();
        map.put("page",1);
        map.put("limit",1);
        map.put("userId",userId);
        return Result.success().put("data",messageService.selectMessageList(map));
    }

    @Login
    @GetMapping("/selectMessageById")
    @ApiOperation("根据id查详情")
    public Result selectMessageById(Long id){
        return Result.success().put("data",messageService.getById(id));
    }


    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestAttribute Long userId,@RequestBody MessageInfo messageInfo){
        messageInfo.setIsSee("2");
        if("4".equals(messageInfo.getState())){
            messageInfo.setUserId(String.valueOf(userId));
        }else if("3".equals(messageInfo.getState())){
            messageInfo.setByUserId(String.valueOf(messageInfo.getPlatformId()));
        }
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/updateMessage")
    @ApiOperation("修改")
    public Result updateMessage(@RequestBody MessageInfo messageInfo){
        messageService.update(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/deleteMessage")
    @ApiOperation("删除")
    public Result deleteMessage(Long id){
        messageService.delete(id);
        return Result.success();
    }



}