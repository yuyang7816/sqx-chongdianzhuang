package com.sqx.modules.app.controller;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.service.BlacklistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blacklist")
@Api("拉黑")
public class BlacklistController {

    @Autowired
    private BlacklistService blacklistService;


    @PostMapping("/insertBlacklist")
    @ApiOperation("拉黑和取消拉黑")
    public Result insertBlacklist(Long userId,Long byUserId){
        return blacklistService.insertBlacklist(userId, byUserId);
    }

    @GetMapping("/selectBlacklist")
    @ApiOperation("查询拉黑列表")
    public Result selectBlacklist(Integer page,Integer limit,Long userId){
        return blacklistService.selectBlacklist(page, limit, userId);
    }







}
