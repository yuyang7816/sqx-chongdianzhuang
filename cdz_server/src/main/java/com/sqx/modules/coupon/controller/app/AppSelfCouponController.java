package com.sqx.modules.coupon.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.modules.coupon.entity.SelfCoupon;
import com.sqx.modules.coupon.service.SelfCouponService;
import com.sqx.modules.coupon.utils.KsCouponUtils;
import com.sqx.modules.coupon.utils.Result;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.TearoomService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(value="自营商城优惠券制作",tags={"自营商城优惠券制作"})
@RequestMapping(value = "/app/selfCoupon")
public class AppSelfCouponController {
    @Autowired
    private SelfCouponService service;
    @Autowired
    private KsCouponUtils ksCouponUtils;
    @Autowired
    private TearoomService tearoomService;

    @GetMapping("/oauth2Token")
    @ApiOperation("快手授权token")
    public String oauth2Token(String code,String state){
        Tearoom tearoom = tearoomService.getOne(new QueryWrapper<Tearoom>().eq("ks_app_id", state));
        return ksCouponUtils.oauth2Token(code,tearoom.getKsAppId(),tearoom.getKsAppSecret(),tearoom.getTearoomId());
    }

    @GetMapping("/list")
    @ApiOperation("列表")
    public Result findAll(Integer page, Integer size,Integer type,String couponName,String status) {
        return service.findAll(page, size,type,couponName,status);
    }

    
    @GetMapping("/find")
    @ApiOperation("查询")
    public Result findOne(Long id) {
        return service.findOne(id);
    }

    
    @PostMapping("/save")
    @ApiOperation("添加")
    public Result saveBody(@RequestBody SelfCoupon entity) {
        return service.saveBody(entity);
    }

    
    @PostMapping("/update")
    @ApiOperation("修改")
    public Result updateBody(@RequestBody SelfCoupon entity) {
        return service.updateBody(entity);
    }


    
    @GetMapping("/delete")
    @ApiOperation("删除")
    public Result delete(Long id) {
        return service.delete(id);
    }

}
