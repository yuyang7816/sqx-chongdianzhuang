package com.sqx.modules.tearoom.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.chats.utils.DateUtil;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Room;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.tearoom.service.TearoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "设备信息", tags = {"设备信息"})
@RequestMapping(value = "/room")
public class RoomController extends AbstractController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private GoodsService goodsService;

    @PostMapping("/insertRoom")
    @ApiOperation("添加设备")
    public Result insertRoom(@RequestBody Room room){
        return roomService.insertRoom(room);
    }

    @PostMapping("/updateRoom")
    @ApiOperation("修改设备")
    public Result updateRoom(@RequestBody Room room){
        return roomService.updateRoom(room);
    }

    @PostMapping("/deleteRoomById")
    @ApiOperation("删除设备")
    public Result deleteRoomById(Long roomId){
        return roomService.removeById(roomId);
    }

    @GetMapping("/selectRoomList")
    @ApiOperation("查询设备列表")
    public Result selectRoomList(Integer page,Integer limit,String roomName,Long goodsId){
        return Result.success().put("data",new PageUtils(roomService.page(new Page<>(page,limit),
                new QueryWrapper<Room>()
                        .eq(StringUtils.isNotEmpty(roomName),"room_name",roomName)
                        .eq(goodsId!=null,"goods_id",goodsId))));
    }

    @GetMapping("/selectGroup")
    @ApiOperation("查询设备组")
    public Result selectGroup(String innerCode){
        return roomService.selectGroup(innerCode);
    }

    @GetMapping("/selectDevice")
    @ApiOperation("查询设备详情")
    public Result selectDevice(String deviceNo){
        return Result.success().put("data",roomService.selectDevice(deviceNo));
    }

    @GetMapping("/issueInstruct")
    @ApiOperation("操作设备")
    public Result issueInstruct(Long roomId){
        return roomService.issueInstruct(roomId,null);
    }

    @GetMapping("/lightControl")
    @ApiOperation("操作设备")
    public Result lightControl(Long goodsId,Integer status){
        Goods goods = goodsService.getById(goodsId);
        Tearoom tearoom = tearoomService.getById(goods.getTearoomId());
        return roomService.lightControl(tearoom.getKeyCode(),goods.getChannels(),status);
    }

}
