package com.sqx.modules.coupon.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.coupon.entity.SelfCouponIssue;
import com.sqx.modules.coupon.respository.SelfCouponIssueJpaRepository;
import com.sqx.modules.tearoom.entity.Tearoom;import com.sqx.modules.tearoom.service.TearoomService;
import com.sqx.modules.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class KsCouponUtils {

    @Autowired
    private SelfCouponIssueJpaRepository couponIssueJpaRepository;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private CommonInfoService commonInfoService;

    public String getShopPoiId(String token,String tearoomName){
        String url="https://lbs-open.kwailocallife.com/goodlife/v1/shop/poi/query";
        Map<String,String> param=new HashMap<>();
        param.put("page","1");
        param.put("size","100");
        String s = HttpClientUtil.doGetDy(url, param, token);
        log.info("快手团购门店列表："+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        JSONObject data = jsonObject1.getJSONObject("data");
        JSONArray pois = data.getJSONArray("pois");
        for (int i=0;i<pois.size();i++){
            JSONObject poi = pois.getJSONObject(i);
            String poiName = poi.getString("poi_name");
            if(poiName.equals(tearoomName)){
                return poi.getString("poi_id");
            }
        }
        return null;
    }

    public Map<String,String> checkCoupon(String token, String code, String encrypted_short_code){
        String url="https://lbs-open.kwailocallife.com/goodlife/v1/fulfilment/certificate/prepare";
        Map<String,String> param=new HashMap<>();
        param.put("code",code);
        if(StringUtils.isNotBlank(encrypted_short_code)){
            param.put("encrypted_short_code",getEncryptedShortCode(encrypted_short_code));
        }
        String s = HttpClientUtil.doGetDy(url, param, token);
        log.info("快手团购验劵准备返回值："+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        String result = jsonObject1.getString("result");
        Map<String,String> resultMap=new HashMap<>();
        if("1".equals(result)){
            resultMap.put("code","500");
            resultMap.put("message",jsonObject1.getString("error_msg"));
        }else{
            JSONObject data = jsonObject1.getJSONObject("data");
            if(data==null){
                JSONObject extra = jsonObject1.getJSONObject("extra");
                resultMap.put("code","500");
                resultMap.put("message",extra.getString("description"));
                return resultMap;
            }
            if(!"0".equals(data.getString("error_code"))){
                resultMap.put("code","500");
                resultMap.put("message",data.getString("description"));
            }else{
                JSONArray certificates = data.getJSONArray("certificates");
                JSONObject sku = certificates.getJSONObject(0).getJSONObject("sku");
                resultMap.put("code","0");
                resultMap.put("orderId",data.getString("order_id"));
                resultMap.put("verifyToken",data.getString("verify_token"));
                String third_sku_id = sku.getString("third_sku_id");
                if(StringUtils.isBlank(third_sku_id)){
                    String title = sku.getString("title");
                    SelfCouponIssue selfCouponIssue = couponIssueJpaRepository.selectCouponByCouponNameKs(title);
                    if(selfCouponIssue!=null){
                        third_sku_id=String.valueOf(selfCouponIssue.getCouponIssueId());
                    }
                }
                resultMap.put("skuId",third_sku_id);
                resultMap.put("encryptedCode",certificates.getJSONObject(0).getString("encrypted_code"));
            }
        }
        return resultMap;
    }

    public Map<String,String> verifyCoupon(String token, String verifyToken, String poiId,String encryptedCode,String orderId){
        String url="https://lbs-open.kwailocallife.com/goodlife/v1/fulfilment/certificate/verify";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("verify_token",verifyToken);
        jsonObject.put("poi_id",poiId);
        jsonObject.put("encrypted_codes",new String[]{encryptedCode});
        jsonObject.put("order_id",orderId);
        String s = HttpClientUtil.doPostJsonDy(url, jsonObject.toJSONString(), token);
        log.info("快手团购验劵返回值："+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        Map<String,String> resultMap=new HashMap<>();
        JSONObject data = jsonObject1.getJSONObject("data");
        if(!"0".equals(data.getString("error_code"))){
            resultMap.put("code","500");
            resultMap.put("message",data.getString("description"));
        }else{
            JSONArray verify_results = data.getJSONArray("verify_results");
            JSONObject verify_result = verify_results.getJSONObject(0);
            if(!"0".equals(verify_result.getString("result"))){
                resultMap.put("code","500");
                resultMap.put("message",verify_result.getString("msg"));
            }else{
                resultMap.put("code","0");
            }
        }
        return resultMap;
    }

    public String oauth2Token(String code,String ksAppId,String ksAppSecret,Long tearoomId){
        if(StringUtils.isEmpty(ksAppId)){
            ksAppId=commonInfoService.findOne(1099).getValue();
        }
        if(StringUtils.isEmpty(ksAppSecret)){
            ksAppSecret=commonInfoService.findOne(1100).getValue();
        }
        String url="https://lbs-open.kuaishou.com/oauth2/access_token";
        Map<String,String> param=new HashMap<>();
        param.put("app_id",ksAppId);
        param.put("app_secret",ksAppSecret);
        param.put("grant_type","code");
        param.put("code",code);
        String s = HttpClientUtil.doGet(url, param);
        log.info("快手授权token返回值："+s);
        try {
            JSONObject jsonObject = JSONObject.parseObject(s);
            String refreshToken = jsonObject.getString("refresh_token");
            jsonObject.getString("access_token");
            Tearoom tearoom = tearoomService.getById(tearoomId);
            tearoom.setKsRefreshToken(refreshToken);
            Calendar calendar=Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH,180);
            tearoom.setKsRefreshTokenTime(DateUtils.format(calendar.getTime()));
            tearoomService.updateById(tearoom);
        }catch (Exception e){
            e.printStackTrace();
            log.info("快手授权token报错："+e.getMessage(),e);
        }
        return s;
    }

    public String getToken(String ksAppId,String ksAppSecret,Long tearoomId){
        if(StringUtils.isEmpty(ksAppId)){
            ksAppId=commonInfoService.findOne(1099).getValue();
        }
        if(StringUtils.isEmpty(ksAppSecret)){
            ksAppSecret=commonInfoService.findOne(1100).getValue();
        }
        Tearoom tearoom = tearoomService.getById(tearoomId);
        if(StringUtils.isEmpty(tearoom.getKsRefreshToken())){
            tearoom.setKsRefreshToken(commonInfoService.findOne(1101).getValue());
        }
        if(StringUtils.isEmpty(ksAppSecret)){
            ksAppSecret=commonInfoService.findOne(1100).getValue();
        }
        String url="https://lbs-open.kuaishou.com/oauth2/refresh_token";
        Map<String,String> param=new HashMap<>();
        param.put("app_id",ksAppId);
        param.put("app_secret",ksAppSecret);
        param.put("grant_type","refresh_token");

        param.put("refresh_token",tearoom.getKsRefreshToken());
        String s = HttpClientUtil.doGet(url, param);
        log.info("快手刷新token返回值："+s);
        JSONObject jsonObject = JSONObject.parseObject(s);
        String refreshToken = jsonObject.getString("refresh_token");

        tearoom.setKsRefreshToken(refreshToken);
        Calendar calendar=Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,180);
        tearoom.setKsRefreshTokenTime(DateUtils.format(calendar.getTime()));
        tearoomService.updateById(tearoom);
        return jsonObject.getString("access_token");
    }

    public static String getEncryptedShortCode(String url) {
        try {
            // 创建 URI 对象
            URI uri = new URI(url);
            // 获取路径部分
            String path = uri.getPath();
            // 去除路径开头的斜杠
            if (path.startsWith("/")) {
                path = path.substring(1);
            }
            return path;
        } catch (URISyntaxException e) {
            // 若 URL 格式有误，打印错误信息并返回空字符串
            System.err.println("Invalid URL: " + url);
            return "";
        }
    }

}
