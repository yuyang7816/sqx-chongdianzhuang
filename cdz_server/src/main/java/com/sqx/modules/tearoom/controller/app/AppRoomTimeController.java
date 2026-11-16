package com.sqx.modules.tearoom.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.service.RoomTimeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "房间忙时", tags = {"房间忙时"})
@RequestMapping(value = "/app/roomTime")
public class AppRoomTimeController extends AbstractController {

    @Autowired
    private RoomTimeService roomTimeService;

    @GetMapping("/selectRoomTimeByDate")
    @ApiOperation("查询房间忙时")
    public Result selectRoomTimeByDate(String date, Long goodsId){
        return Result.success().put("data",roomTimeService.selectRoomTimeListByDate(goodsId, date));
    }
    





}
