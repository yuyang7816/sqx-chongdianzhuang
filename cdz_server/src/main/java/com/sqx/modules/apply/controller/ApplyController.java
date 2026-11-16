package com.sqx.modules.apply.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.Result;
import com.sqx.modules.apply.entity.Apply;
import com.sqx.modules.apply.service.ApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value = "申请", tags = {"申请"})
@RequestMapping(value = "/apply")
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @PostMapping("/insertApply")
    @ApiOperation("发起申请")
    public Result insertApply(@RequestBody Apply apply){
        return applyService.insertApply(apply);
    }

    @PostMapping("/updateApply")
    @ApiOperation("修改申请")
    public Result updateApply(@RequestBody Apply apply){
        return applyService.updateApply(apply);
    }

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

    @GetMapping("/selectApplyByUserIdAndClassify")
    @ApiOperation("查询申请详情")
    public Result selectApplyByUserIdAndClassify(Long userId){
        return Result.success().put("data",applyService.selectApplyByUserIdAndClassify(userId));
    }

    @PostMapping("/auditApply")
    @ApiOperation("审核申请")
    public Result auditApply(String ids,String content,Integer status){
        return applyService.auditApply(ids, status, content);
    }

    @GetMapping("/selectApplyCount")
    @ApiOperation("查询代理待审核总和")
    public Result selectApplyCount(String city){
        return Result.success().put("data",applyService.count(new QueryWrapper<Apply>().like(StringUtils.isNotBlank(city),"apply_content",city).eq("status",1)));
    }


}
