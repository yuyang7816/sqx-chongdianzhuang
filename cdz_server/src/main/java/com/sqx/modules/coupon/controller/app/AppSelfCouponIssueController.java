package com.sqx.modules.coupon.controller.app;

import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.service.SelfCouponIssueService;
import com.sqx.modules.coupon.utils.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城优惠券发布",tags={"自营商城优惠券发布"})
@RequestMapping(value = "/app/selfCouponIssue")
public class AppSelfCouponIssueController {
    @Autowired
    private SelfCouponIssueService service;

    @Login
    @GetMapping("/list")
    @ApiOperation("后台列表")
    public Result findAll(Integer page, Integer size,@RequestParam(required = false) String goodsId,String tearoomId,
                          String type,String couponName,String tearoomName) {
        return service.findAll(page, size,goodsId,tearoomId,type,couponName,tearoomName);
    }

    @GetMapping("/useList")
    @ApiOperation("用户端领券列表")
    public Result useList(Integer page,Integer size,String goodsId,Integer type,Integer couponType,Long tearoomId) {
        return service.useList(page,size,goodsId,type,couponType,tearoomId);
    }

    @Login
    @GetMapping("/selectTearoomListByCoupon")
    @ApiOperation("根据优惠券查询充电桩列表")
    public Result selectTearoomListByCoupon(Long couponIssueId){
        return service.selectTearoomListByCoupon(couponIssueId);
    }
    
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    
    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCouponIssue entity) {
        return service.saveBody(entity);
    }

    
    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCouponIssue entity) {
        return service.updateBody(entity);
    }

    
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
