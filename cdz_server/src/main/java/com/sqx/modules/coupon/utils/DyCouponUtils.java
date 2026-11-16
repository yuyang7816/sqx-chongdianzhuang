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
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class DyCouponUtils {

    @Autowired
    private CommonInfoService commonInfoService;

    public Result checkCoupon(String encrypted_data,String code,String dyPoiId,String dyAppId,String dySecret){
        if(StringUtils.isEmpty(dyPoiId)){
            dyPoiId=commonInfoService.findOne(829).getValue();
        }
        if(StringUtils.isEmpty(dyAppId)){
            dyAppId=commonInfoService.findOne(827).getValue();
        }
        if(StringUtils.isEmpty(dySecret)){
            dySecret=commonInfoService.findOne(828).getValue();
        }
        JSONObject data = null;
        Map<String, String> param=new HashMap<>();
        param.put("encrypted_data",encrypted_data);
        param.put("code",code);
        param.put("poi_id",dyPoiId);
        String s = HttpClientUtil.doGetDy("https://open.douyin.com/goodlife/v1/fulfilment/certificate/prepare/", param, getAccessToken(dyAppId,dySecret));
        log.info("抖音验劵准备返回值："+s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        data = jsonObject.getJSONObject("data");
        String errorCode = data.getString("error_code");
        if("3000002".equals(errorCode)){
            return ResultUtil.error(500,data.getString("description"));
        }
        if(!"0".equals(errorCode)){
            return ResultUtil.error(500,data.getString("description"));
        }
        data = jsonObject.getJSONObject("data");

        if(data==null){
            return ResultUtil.error(500,"核销失败，请联系客服！");
        }

        JSONArray certificatess = data.getJSONArray("certificates");
        if(certificatess==null || certificatess.size()==0){
            return ResultUtil.error(500,"核销失败，请检查是否已经使用过了！");
        }
        JSONObject certificates =certificatess.getJSONObject(0);
        String encryptedCode = certificates.getString("encrypted_code");
        String verifyToken = data.getString("verify_token");
        JSONObject sku = certificates.getJSONObject("sku");
        String thirdSkuId = sku.getString("third_sku_id");
        return verifyCoupon(encryptedCode,verifyToken,thirdSkuId,dyPoiId,dyAppId,dySecret);
    }

    public Result verifyCoupon(String encryptedCode,String verifyToken,String couponId,String dyPoiId,String dyAppId,String dySecret){
        JSONObject param=new JSONObject();
        param.put("verify_token",verifyToken);
        param.put("poi_id",dyPoiId);
        List<String> encrypted_codes=new ArrayList<>();
        encrypted_codes.add(encryptedCode);
        param.put("encrypted_codes",encrypted_codes);
        String s = HttpClientUtil.doPostJsonDy("https://open.douyin.com/goodlife/v1/fulfilment/certificate/verify/", param.toJSONString(), getAccessToken(dyAppId,dySecret));
        log.info("抖音验劵返回值："+s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        JSONObject data = jsonObject.getJSONObject("data");
        String errorCode = data.getString("error_code");
        if(!"0".equals(errorCode)){
            return ResultUtil.error(500,data.getString("description"));
        }
        JSONArray verify_results = data.getJSONArray("verify_results");
        if(verify_results==null || verify_results.size()==0){
            return ResultUtil.error(500,"核销失败，请检查是否已经使用过了！");
        }
        JSONObject jsonObject1 = verify_results.getJSONObject(0);
        String result = jsonObject1.getString("result");
        //0 验券成功
        //三方码：1205 抖音码：3  券码正在退款中（用户主动退款）
        //三方码：1206 抖音码：5  券码未到可使用日期 或 团购有效期尚未开始
        //三方码：1207 抖音码：32 券码退款中（过期自动退）
        //三方码：1208 抖音码：2  券码已核销
        //三方码：1209 抖音码：4  券码已退款
        //三方码：1211  其他错误
        //抖音码：31  券码退款中（用户申请）
        //抖音码：6 券码已过期，系统将自动发起退款

        if(!"0".equals(result)){
            return ResultUtil.error(500,jsonObject1.getString("msg"));
        }
        //验劵成功  走业务
        return ResultUtil.success(couponId);
    }



    public String getAccessToken(String dyAppId,String dySecret){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("client_key",dyAppId);
        jsonObject.put("client_secret",dySecret);
        jsonObject.put("grant_type","client_credential");
        String jsonResult = HttpClientUtil.doPostJson("https://open.douyin.com/oauth/client_token/",jsonObject.toJSONString());
        JSONObject parseObject = JSON.parseObject(jsonResult);
        log.info("=========抖音accessTokenOut========="+parseObject.toJSONString());

        JSONObject data = parseObject.getJSONObject("data");
        return data.getString("access_token");
    }

    public Result getCoupon(String encrypted_data,String code,String dyPoiId,String dyAppId,String dySecret){
        if(StringUtils.isEmpty(dyPoiId)){
            dyPoiId=commonInfoService.findOne(829).getValue();
        }
        if(StringUtils.isEmpty(dyAppId)){
            dyAppId=commonInfoService.findOne(827).getValue();
        }
        if(StringUtils.isEmpty(dySecret)){
            dySecret=commonInfoService.findOne(828).getValue();
        }
        JSONObject data = null;
        Map<String, String> param=new HashMap<>();
        param.put("encrypted_data",encrypted_data);
        param.put("code",code);
        param.put("poi_id",dyPoiId);
        String s = HttpClientUtil.doGetDy("https://open.douyin.com/goodlife/v1/fulfilment/certificate/prepare/", param, getAccessToken(dyAppId,dySecret));
        log.info("抖音验劵准备返回值："+s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        data = jsonObject.getJSONObject("data");
        String errorCode = data.getString("error_code");
        if("3000002".equals(errorCode)){
            return ResultUtil.error(500,data.getString("description"));
        }
        if(!"0".equals(errorCode)){
            return ResultUtil.error(500,data.getString("description"));
        }
        data = jsonObject.getJSONObject("data");

        if(data==null){
            return ResultUtil.error(500,"核销失败，请联系客服！");
        }

        JSONArray certificatess = data.getJSONArray("certificates");
        if(certificatess==null || certificatess.size()==0){
            return ResultUtil.error(500,"核销失败，请检查是否已经使用过了！");
        }
        JSONObject certificates =certificatess.getJSONObject(0);
        String encryptedCode = certificates.getString("encrypted_code");
        String verifyToken = data.getString("verify_token");
        JSONObject sku = certificates.getJSONObject("sku");
        String thirdSkuId = sku.getString("third_sku_id");
        return ResultUtil.success(thirdSkuId);
    }

    /*public static void main(String[] args) {
        String appid = "awnzk8a71856r4bk";
        String secret = "71bef8ee0945dbbc8c9d61bf62038d41";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("client_key",appid);
        jsonObject.put("client_secret",secret);
        jsonObject.put("grant_type","client_credential");
        String jsonResult = HttpClientUtil.doPostJson("https://open.douyin.com/oauth/client_token/",jsonObject.toJSONString());
        JSONObject parseObject = JSON.parseObject(jsonResult);
        log.info("=========抖音accessTokenOut========="+parseObject.toJSONString());

        JSONObject data = parseObject.getJSONObject("data");
        String accessToken = data.getString("access_token");
        Map<String, String> param=new HashMap<>();
        param.put("code","102692315741346");
        param.put("poi_id","7334226874245056538");
        String s = HttpClientUtil.doGetDy("https://open.douyin.com/goodlife/v1/fulfilment/certificate/prepare/", param, accessToken);
        log.info("抖音验劵准备返回值："+s);
        jsonObject = JSONObject.parseObject(s);
        data = jsonObject.getJSONObject("data");
        String errorCode = data.getString("error_code");
        JSONArray certificatess = data.getJSONArray("certificates");
        if(certificatess==null || certificatess.size()==0){

        }
        JSONObject certificates =certificatess.getJSONObject(0);
        String encryptedCode = certificates.getString("encrypted_code");
        String verifyToken = data.getString("verify_token");
        JSONObject sku = certificates.getJSONObject("sku");
        String thirdSkuId = sku.getString("third_sku_id");

        JSONObject params=new JSONObject();
        params.put("verify_token",verifyToken);
        params.put("poi_id","7334226874245056538");
        List<String> encrypted_codes=new ArrayList<>();
        encrypted_codes.add(encryptedCode);
        params.put("encrypted_codes",encrypted_codes);
        s = HttpClientUtil.doPostJsonDy("https://open.douyin.com/goodlife/v1/fulfilment/certificate/verify/", params.toJSONString(), accessToken);
        log.info("抖音验劵返回值："+s);
        jsonObject = JSONObject.parseObject(s);
        data = jsonObject.getJSONObject("data");
        errorCode = data.getString("error_code");

    }*/

}
