package com.sqx.modules.tearoom.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.coupon.entity.CouponIssue;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.tearoom.service.TearoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@Api(value = "充电桩信息", tags = {"充电桩信息"})
@RequestMapping(value = "/tearoom")
public class TearoomController extends AbstractController {

    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private SelfCouponIssueJpaRepository selfCouponIssueJpaRepository;

    @PostMapping("/insertTearoom")
    @ApiOperation("添加充电桩")
    public Result insertTearoom(@RequestBody Tearoom tearoom){
        tearoom.setCreateTime(DateUtils.format(new Date()));
        tearoomService.save(tearoom);
        return Result.success();
    }
    
    @PostMapping("/updateTearoom")
    @ApiOperation("修改充电桩")
    public Result updateTearoom(@RequestBody Tearoom tearoom){
        tearoomService.updateById(tearoom);
        return Result.success();
    }

    @PostMapping("/deleteTearoomById")
    @ApiOperation("删除充电桩")
    public Result deleteTearoomById(Long tearoomId){
        tearoomService.removeById(tearoomId);
        return Result.success();
    }

    @GetMapping("/selectTearoomList")
    @ApiOperation("查询充电桩列表")
    public Result selectTearoomList(Integer page,Integer limit,String city,Integer status,String tearoomName,
                                    String longitude,String latitude,Integer sort,Long userId,String userName,Long tearoomUserId,
                                    String tearoomPhone,Integer shopType,Integer isVipPrivilege,Integer tearoomType,Long byUserId){
        return tearoomService.selectTearoomList(page, limit, city, status, tearoomName, longitude, latitude, sort,userId,
                userName,tearoomUserId,tearoomPhone,shopType,isVipPrivilege,tearoomType,byUserId);
    }
    
    @GetMapping("/selectTearoomById")
    @ApiOperation("查询充电桩详情")
    public Result selectTearoomById(Long tearoomId){
        Tearoom tearoom = tearoomService.getById(tearoomId);
        if(tearoom.getUserId()!=null){
            UserEntity userEntity = userService.selectUserById(tearoom.getUserId());
            if(userEntity!=null){
                tearoom.setUserName(userEntity.getUserName());
            }
        }
        if(StringUtils.isNotBlank(tearoom.getEmployeeUserIds())){
            List<UserEntity> list = userService.list(new QueryWrapper<UserEntity>().in("user_id", tearoom.getEmployeeUserIds().split(",")));
            tearoom.setUserEntityList(list);
        }
        if(StringUtils.isNotBlank(tearoom.getLuckyCouponIds())){
            List<SelfCouponIssue> couponIssueList=new ArrayList<>();
            for (String str:tearoom.getLuckyCouponIds().split(",")){
                SelfCouponIssue selfCouponIssue = selfCouponIssueJpaRepository.findById(Long.parseLong(str)).orElse(null);
                if(selfCouponIssue!=null){
                    couponIssueList.add(selfCouponIssue);
                }
            }
            tearoom.setCouponIssueList(couponIssueList);
        }

        return Result.success().put("data",tearoom);
    }

    @PostMapping("/insertGoodsGate")
    @ApiOperation("添加大门指令")
    public Result insertGoodsGate(Long tearoomId,String roomNo,String roomName,String innerCode){
        return tearoomService.insertGoodsGate(tearoomId, roomNo, roomName, innerCode);
    }

    @GetMapping("/broadcastContent")
    @ApiOperation("语音播报")
    public Result broadcastContent(Long tearoomId){
        Tearoom tearoom = tearoomService.getById(tearoomId);
        return roomService.musicControl(tearoom.getYyToken(),tearoom.getYyId(),tearoom.getBroadcastContent());
    }

    @GetMapping("/selectTearoomByOrdersUserId")
    @ApiOperation("查询常去充电桩")
    public Result selectTearoomByOrdersUserId(Integer page,Integer limit, String longitude, String latitude,Long userId,Integer tearoomType){
        return Result.success().put("data",new PageUtils(tearoomService.selectTearoomByOrdersUserId(page, limit,longitude, latitude, userId,tearoomType)));
    }


}
