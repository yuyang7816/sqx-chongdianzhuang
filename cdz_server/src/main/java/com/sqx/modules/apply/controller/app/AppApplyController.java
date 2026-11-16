package com.sqx.modules.apply.controller.app;

import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.apply.entity.Apply;
import com.sqx.modules.apply.service.ApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "申请", tags = {"申请"})
@RequestMapping(value = "/app/apply")
public class AppApplyController {

    @Autowired
    private ApplyService applyService;

    @Login
    @PostMapping("/insertApply")
    @ApiOperation("发起申请")
    public Result insertApply(@RequestBody Apply apply,@RequestAttribute Long userId){
        apply.setUserId(userId);
        return applyService.insertApply(apply);
    }

    @Login
    @PostMapping("/updateApply")
    @ApiOperation("修改申请")
    public Result updateApply(@RequestBody Apply apply){
        return applyService.updateApply(apply);
    }

    @Login
    @PostMapping("/deleteApply")
    @ApiOperation("删除申请")
    public Result deleteApply(Long applyId){
        return applyService.deleteApply(applyId);
    }

    @GetMapping("/selectApplyList")
    @ApiOperation("查询申请列表")
    public Result selectApplyList(Integer page,Integer limit,String shopName,String applyName,
                                  String applyPhone,Integer status,String shopAddress,String shopAddressDetails){
        return applyService.selectApplyList(page, limit, shopName, applyName, applyPhone, status, shopAddress, shopAddressDetails);
    }

    @Login
    @GetMapping("/selectApplyByUserIdAndClassify")
    @ApiOperation("查询申请详情")
    public Result selectApplyByUserIdAndClassify(@RequestAttribute Long userId){
        return Result.success().put("data",applyService.selectApplyByUserIdAndClassify(userId));
    }




}
