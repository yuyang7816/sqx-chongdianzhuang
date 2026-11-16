package com.sqx.modules.tearoom.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.tearoom.service.TearoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@Api(value = "充电桩信息", tags = {"充电桩信息"})
@RequestMapping(value = "/app/tearoom")
public class AppTearoomController extends AbstractController {

    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private RoomService roomService;


    @GetMapping("/selectTearoomList")
    @ApiOperation("查询充电桩列表")
    public Result selectTearoomList(Integer page,Integer limit,String city,Integer status,String search,
                                    String longitude,String latitude,Integer sort,Long userId,Long tearoomUserId,
                                    Integer shopType,Integer isVipPrivilege,Integer tearoomType,Long byUserId){
        return tearoomService.selectTearoomList(page, limit, city, status, search, longitude, latitude, sort,userId,
                null,tearoomUserId,null,shopType,isVipPrivilege,tearoomType,byUserId);
    }
    
    @GetMapping("/selectTearoomById")
    @ApiOperation("查询充电桩详情")
    public Result selectTearoomById(Long tearoomId,String longitude,String latitude,Integer status){
        Tearoom tearoom = tearoomService.selectTearoomById(tearoomId, longitude, latitude);
        return Result.success().put("data",tearoom);
    }

    @GetMapping("/selectTearoomByDistance")
    @ApiOperation("查询距离自己最近的充电桩")
    public Result selectTearoomByDistance(String longitude,String latitude){
        return Result.success().put("data",tearoomService.selectTearoomByDistance(longitude,latitude));
    }

    @Login
    @PostMapping("/updateTearoom")
    @ApiOperation("修改充电桩")
    public Result updateTearoom(@RequestBody Tearoom tearoom){
        tearoomService.updateById(tearoom);
        return Result.success();
    }

    @Login
    @GetMapping("/selectTearoomByOrdersUserId")
    @ApiOperation("查询常去充电桩")
    public Result selectTearoomByOrdersUserId(Integer page,Integer limit, String longitude, String latitude,@RequestAttribute Long userId,Integer tearoomType){
        return Result.success().put("data",new PageUtils(tearoomService.selectTearoomByOrdersUserId(page, limit,longitude, latitude, userId,tearoomType)));
    }


}
