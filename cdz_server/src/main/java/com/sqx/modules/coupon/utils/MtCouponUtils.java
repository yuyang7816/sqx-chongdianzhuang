package com.sqx.modules.coupon.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class MtCouponUtils {

    @Autowired
    private CommonInfoService commonInfoService;

    public Result verifyCoupon(String couponCode,String mtEmail,String mtPassword,String mtStoreId){
        if(StringUtils.isEmpty(mtEmail)){
            mtEmail=commonInfoService.findOne(895).getValue();
        }
        if(StringUtils.isEmpty(mtPassword)){
            mtPassword=commonInfoService.findOne(896).getValue();
        }
        if(StringUtils.isEmpty(mtStoreId)){
            mtStoreId=commonInfoService.findOne(897).getValue();
        }
        //获取核销token
        String url="http://mtopen.umember.cn";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("email",mtEmail);
        jsonObject.put("password",mtPassword);
        String s = HttpClientUtil.doPostJson(url+"/open/login", jsonObject.toJSONString());
        log.error("美团获取token："+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        if(!"10000".equals(jsonObject1.getString("code"))){
            return ResultUtil.error(500,jsonObject1.getString("msg"));
        }
        JSONObject data = jsonObject1.getJSONObject("data");
        String token = data.getString("token");

        //获取团购劵详情
        Map<String,String> param=new HashMap<>();
        param.put("store_id",mtStoreId);
        param.put("coupon_code",couponCode);
        s = HttpClientUtil.doGetToken(url+"/open/user/meituan/coupon/detail", param,token);
        log.error("美团团购劵返回值："+s);
        jsonObject1 = JSONObject.parseObject(s);
        if(!"10000".equals(jsonObject1.getString("code"))){
            return ResultUtil.error(500,jsonObject1.getString("msg"));
        }
        data = jsonObject1.getJSONObject("data");
        String status = data.getString("status");
        if(!"未使用".equals(status)){
            return ResultUtil.error(500,status);
        }
        String skuName = data.getString("sku_name");
        skuName = skuName.substring(0, skuName.indexOf("["));

        //核销团购劵
        jsonObject=new JSONObject();
        jsonObject.put("store_id",Integer.parseInt(mtStoreId));
        jsonObject.put("coupon_code",couponCode);
        s = HttpClientUtil.doPostJsonMt(url+"/open/user/meituan/coupon/verify", jsonObject.toJSONString(),token);
        log.error("美团验劵返回值："+s);
        jsonObject1 = JSONObject.parseObject(s);
        if(!"10000".equals(jsonObject1.getString("code"))){
            return ResultUtil.error(500,jsonObject1.getString("msg"));
        }
        data = jsonObject1.getJSONObject("data");
        String verifySuccess = data.getString("verify_success");
        if(!"true".equals(verifySuccess)){
            return ResultUtil.error(500,data.getString("verify_message"));
        }
        //核销成功
        return ResultUtil.success(skuName);
    }


    public Result getCoupon(String couponCode,String mtEmail,String mtPassword,String mtStoreId){
        if(StringUtils.isEmpty(mtEmail)){
            mtEmail=commonInfoService.findOne(895).getValue();
        }
        if(StringUtils.isEmpty(mtPassword)){
            mtPassword=commonInfoService.findOne(896).getValue();
        }
        if(StringUtils.isEmpty(mtStoreId)){
            mtStoreId=commonInfoService.findOne(897).getValue();
        }
        //获取核销token
        String url="http://mtopen.umember.cn";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("email",mtEmail);
        jsonObject.put("password",mtPassword);
        String s = HttpClientUtil.doPostJson(url+"/open/login", jsonObject.toJSONString());
        log.error("美团获取token："+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        if(!"10000".equals(jsonObject1.getString("code"))){
            return ResultUtil.error(500,jsonObject1.getString("msg"));
        }
        JSONObject data = jsonObject1.getJSONObject("data");
        String token = data.getString("token");

        //获取团购劵详情
        Map<String,String> param=new HashMap<>();
        param.put("store_id",mtStoreId);
        param.put("coupon_code",couponCode);
        s = HttpClientUtil.doGetToken(url+"/open/user/meituan/coupon/detail", param,token);
        log.error("美团团购劵返回值："+s);
        jsonObject1 = JSONObject.parseObject(s);
        if(!"10000".equals(jsonObject1.getString("code"))){
            return ResultUtil.error(500,jsonObject1.getString("msg"));
        }
        data = jsonObject1.getJSONObject("data");
        String status = data.getString("status");
        if(!"未使用".equals(status)){
            return ResultUtil.error(500,status);
        }
        String skuName = data.getString("sku_name");
        skuName = skuName.substring(0, skuName.indexOf("["));

        //核销成功
        return ResultUtil.success(skuName);
    }

    public static void main(String[] args) {
        String skuName="美团1小时台球[9.90元][1320678619]";
        skuName = skuName.substring(0, skuName.indexOf("["));
        System.out.println(skuName);
    }


}
