package com.sqx.modules.app.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.service.BlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/blacklist")
@Api("拉黑")
public class AppBlacklistController {

    @Autowired
    private BlacklistService blacklistService;


    @Login
    @PostMapping("/insertBlacklist")
    @ApiOperation("拉黑和取消拉黑")
    public Result insertBlacklist(@RequestAttribute("userId") Long userId,Long byUserId){
        return blacklistService.insertBlacklist(userId, byUserId);
    }

    @Login
    @GetMapping("/selectBlacklistByUserId")
    @ApiOperation("拉黑和取消拉黑")
    public Result selectBlacklistByUserId(@RequestAttribute("userId") Long userId,Long byUserId){
        return Result.success().put("data",blacklistService.selectBlacklistByUserId(userId, byUserId));
    }

    @Login
    @GetMapping("/selectBlacklist")
    @ApiOperation("查询拉黑列表")
    public Result selectBlacklist(Integer page,Integer limit,@RequestAttribute Long userId){
        return blacklistService.selectBlacklist(page, limit, userId);
    }







}
