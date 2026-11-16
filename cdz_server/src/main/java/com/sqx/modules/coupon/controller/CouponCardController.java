package com.sqx.modules.coupon.controller;

import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.coupon.entity.CouponCardType;
import com.sqx.modules.coupon.service.CouponCardService;
import com.sqx.modules.coupon.service.CouponCardTypeService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.utils.excel.ExcelData;
import com.sqx.modules.utils.excel.ExportExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@RestController
@Api(value = "卡密优惠券", tags = {"卡密优惠券"})
@RequestMapping(value = "/couponCard")
public class CouponCardController extends AbstractController {

    @Autowired
    private CouponCardService couponCardService;
    @Autowired
    private CouponCardTypeService couponCardTypeService;


    @PostMapping("/insertCouponCardType")
    @ApiOperation("添加卡密分类")
    public Result insertCouponCardType(@RequestBody CouponCardType couponCardType){
        couponCardType.setCreateTime(DateUtils.format(new Date()));
        couponCardTypeService.save(couponCardType);
        return Result.success();
    }

    @PostMapping("/updateCouponCardType")
    @ApiOperation("修改卡密分类")
    public Result updateCouponCardType(@RequestBody CouponCardType couponCardType){
        couponCardTypeService.updateById(couponCardType);
        return Result.success();
    }

    @PostMapping("/deleteCouponCardTypeById")
    @ApiOperation("删除卡密分类")
    public Result deleteCouponCardTypeById(Long couponCardTypeId){
        couponCardTypeService.removeById(couponCardTypeId);
        return Result.success();
    }

    @GetMapping("/selectCouponCardTypeList")
    @ApiOperation("查询卡密分类列表")
    public Result selectCouponCardTypeList(Integer page,Integer limit,String couponTypeName,Integer status){
        return couponCardTypeService.selectCouponCardTypeList(page, limit, couponTypeName, status);
    }


    @PostMapping("/insertCouponCard")
    @ApiOperation("生成卡密优惠券")
    public Result insertCouponCard(Integer num,Long couponCardTypeId){
        couponCardService.insertCouponCard(num,couponCardTypeId);
        return Result.success();
    }

    @PostMapping("/deleteCouponCard")
    @ApiOperation("删除卡密")
    public Result deleteCouponCard(Long couponCardId){
        couponCardService.removeById(couponCardId);
        return Result.success();
    }

    @GetMapping("/selectCouponCardList")
    @ApiOperation("查询卡密列表")
    public Result selectCouponCardList(Integer page,Integer limit,Integer status,String couponCardNo,Long couponCardTypeId,Long userId){
        return couponCardService.selectCouponCardList(page, limit, status, couponCardNo, couponCardTypeId, userId);
    }

    @GetMapping("/excelCouponCardList")
    @ApiOperation("导出卡密列表")
    public void excelCouponCardList(Integer status,String couponCardNo,Long couponCardTypeId,Long userId, HttpServletResponse response) throws Exception{
        ExcelData excelData = couponCardService.excelCouponCardList(status, couponCardNo, couponCardTypeId, userId);
        ExportExcelUtils.exportExcel(response,"卡密列表.xlsx",excelData);
    }

    @PostMapping("/activateCouponCard")
    @ApiOperation("激活卡密")
    public Result activateCouponCard(Long userId,String couponCardNo){
        return couponCardService.activateCouponCard(userId, couponCardNo);
    }





}