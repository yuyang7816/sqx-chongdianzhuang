package com.sqx.modules.tearoom.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.*;

@RestController
@Api(value = "设备信息", tags = {"设备信息"})
@RequestMapping(value = "/app/room")
@Slf4j
public class AppRoomController extends AbstractController {

    @Autowired
    private RoomService roomService;
    @Autowired
    private GoodsService goodsService;

    @GetMapping("/selectRoomList")
    @ApiOperation("查询设备列表")
    public Result selectRoomList(Integer page,Integer limit,String roomName,Long tearoomId){
        IPage<Room> roomIPage = roomService.page(new Page<>(page, limit),
                new QueryWrapper<Room>()
                        .eq(StringUtils.isNotEmpty(roomName), "room_name", roomName).notIn("room_type",5)
                        .eq(tearoomId != null, "goods_id", tearoomId));
        List<Room> records = roomIPage.getRecords();
        for (Room room:records){
            if(room.getIsOpen()==null){
                room.setIsOpen(2);
            }
        }
        return Result.success().put("data",new PageUtils(roomIPage));
    }


    @PostMapping("/issueInstruct")
    @ApiOperation("操作设备")
    public Result issueInstruct(Long roomId,Integer isOpen){
        Room room = roomService.getById(roomId);
        room.setIsOpen(isOpen);
        roomService.updateById(room);
        return roomService.airConditionerControl(room.getRoomNo(),isOpen);
    }

    @GetMapping("/lightControl")
    @ApiOperation("操作设备")
    public Result lightControl(Long goodsId,Integer status){
        if(status==0){
            status=2;
        }
        Goods goods = goodsService.getById(goodsId);
        if(goods.getGoodsCategory()==4){
            Room room1 = roomService.getOne(new QueryWrapper<Room>().eq("goods_id",goodsId).eq("room_type",3));
            roomService.issueInstruct(room1.getRoomId(),status);
        }else{
            List<Room> lightRoom = roomService.list(new QueryWrapper<Room>().eq("goods_id",goodsId));
            for (Room room:lightRoom){
                try {
                    roomService.issueInstruct(room.getRoomId(),status);
                }catch (Exception e){
                    e.printStackTrace();
                    log.error("调用关闭设备报错1："+e.getMessage(),e);
                }
            }
        }

        return Result.success();
    }


}
