package com.sqx.modules.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取对象的IP地址等信息
 * @author fang
 * @date 2020/9/23
 */
public class CusAccessObjectUtil {

    /**
     * 获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,
     * <p>
     * 可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
     * 答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。
     * <p>
     * 如：X-Forwarded-For：192.168.1.110, 192.168.1.120, 192.168.1.130,
     * 192.168.1.100
     * <p>
     * 用户真实IP为： 192.168.1.110
     *
     * @param request
     * @return
     */
    public static String getIpAddrs(HttpServletRequest request)
            throws Exception {
        if (request == null) {
            throw (new Exception("getIpAddr method HttpServletRequest Object is null"));
        }
        String ipString = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(ipString) || "unknown".equalsIgnoreCase(ipString)) {
            ipString = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        String[] arr = ipString.split(",");
        for (String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ipString = str;
                break;
            }
        }


        return ipString;
    }

    public static String getIp(HttpServletRequest request) {
        //目前则是网关ip
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                //只获取第一个值
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            //取不到真实ip则返回空，不能返回内网地址。
            return "";

        }
    }


    public static String getAddress(String ip) {
        //""
        //String url = "http://opendata.baidu.com/api.php?query=" + ip + "&co=&resource_id=6006&t=" + System.currentTimeMillis() + "&ie=utf8&oe=utf-8&format=json";
        String url="https://ip.useragentinfo.com/json?ip="+ip;
        String str = HttpClientUtil.doGet(url);
        //{"country": "中国", "short_name": "CN", "province": "陕西省", "city": "西安市", "area": "雁塔区", "isp": "电信", "net": "", "ip": "222.91.197.83", "code": 200, "desc": "success"}
        com.alibaba.fastjson.JSONObject jsStr = JSONObject.parseObject(str);
        System.err.println(jsStr.toJSONString());
        String country = jsStr.getString("country");
        if("中国".equals(country)){
            //如果为中国则不需要拼接国家
            country="";
        }
        String province = jsStr.getString("province");
        String city = jsStr.getString("city");
        String area = jsStr.getString("area");
        String isp = jsStr.getString("isp");
        return country+province+city+area+" "+isp;
    }


    //    // 测试
    public static void main(String[] args) {
        //String ip = "222.91.197.83";
        //String ip = "125.188.7.127";
        String ip = "129.12.3.75";

        String address = getAddress(ip);
        String[] privinces = {"北京市", "天津市", "河北省", "山西省", "内蒙古", "辽宁省", "吉林省", "黑龙江省", "上海市", "江苏省", "浙江省", "安徽省", "福建省", "江西省", "山东省", "河南省", "湖北省", "湖南省", "广东省", "广西", "海南省", "重庆市", "四川省", "贵州省", "云南省", "西藏", "陕西省", "甘肃省", "青海省", "宁夏", "新疆", "台湾省", "香港", "澳门"};
        boolean china = false;
        for (String privince : privinces) {
            if (address.startsWith(privince)) {
                china = true;
                if (address.startsWith("云南省")) {

                }
                break;
            }
        }
        System.err.println(ip + ";" + address + ";" + china);

    }





}