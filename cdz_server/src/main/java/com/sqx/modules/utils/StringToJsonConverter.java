package com.sqx.modules.utils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串解析工具类，用于将对象字符串表示转换为JSON
 */
public class StringToJsonConverter {

    /**
     * 将类似Orders(...)的字符串转换为JSON
     * @param objectString 形如"Orders(ordersId=3553, ...)"的字符串
     * @return 解析后的JSON字符串
     */
    public static String convertToJson(String objectString) {
        if (objectString == null || objectString.trim().isEmpty()) {
            return "{}";
        }

        // 提取类名和属性部分
        String className = "";
        String attributesPart = objectString;

        // 匹配类名和属性部分 (Orders(...) -> Orders 和 (...)内的部分)
        Pattern pattern = Pattern.compile("(\\w+)\\((.*)\\)");
        Matcher matcher = pattern.matcher(objectString);

        if (matcher.find()) {
            className = matcher.group(1);
            attributesPart = matcher.group(2);
        }

        // 解析属性对
        Map<String, String> attributes = parseAttributes(attributesPart);

        // 构建JSON
        return buildJson(attributes);
    }

    /**
     * 解析属性字符串为键值对
     */
    private static Map<String, String> parseAttributes(String attributesString) {
        Map<String, String> result = new HashMap<>();
        if (attributesString == null || attributesString.trim().isEmpty()) {
            return result;
        }

        // 分割属性对 (使用正则表达式处理带逗号的情况，如URL中的逗号)
        String[] keyValuePairs = attributesString.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String pair : keyValuePairs) {
            pair = pair.trim();
            if (pair.isEmpty()) {
                continue;
            }

            // 分割键值 (处理值中包含=的情况，如onePayMoney=17.500)
            int equalsIndex = pair.indexOf('=');
            if (equalsIndex > 0 && equalsIndex < pair.length() - 1) {
                String key = pair.substring(0, equalsIndex).trim();
                String value = pair.substring(equalsIndex + 1).trim();
                result.put(key, value);
            }
        }

        return result;
    }

    /**
     * 构建JSON字符串
     */
    private static String buildJson(Map<String, String> attributes) {
        if (attributes.isEmpty()) {
            return "{}";
        }

        StringBuilder sb = new StringBuilder("{");
        boolean first = true;

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            if (!first) {
                sb.append(", ");
            }
            first = false;

            // 键需要用双引号包裹
            sb.append("\"").append(entry.getKey()).append("\": ");

            String value = entry.getValue();
            if (value == null || value.equals("null")) {
                sb.append("null");
            }
            // 数值类型判断
            else if (value.matches("\\d+(\\.\\d+)?") || value.matches("\\d+")) {
                sb.append(value);
            }
            // 布尔类型判断
            else if (value.equals("true") || value.equals("false")) {
                sb.append(value);
            }
            // 字符串类型，需要转义并包裹双引号
            else {
                sb.append("\"").append(escapeJson(value)).append("\"");
            }
        }

        sb.append("}");
        return sb.toString();
    }

    /**
     * 转义JSON字符串中的特殊字符
     */
    private static String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    public static void main(String[] args) {
        String data = "Orders(ordersId=3553, tearoomId=21, goodsId=42, goods=null, goodsName=码上台球一金腿, goodsCoverImage=https://tu.mianyazuju.com/20250415/11a7d059e8b94774bbcc682bc29792cf.png, onePayMoney=17.500, payMoney=17.500, oldPayMoney=35.00, status=1, startTime=null, endTime=null, roomId=null, userId=1114, userName=null, phone=13895585204, payWay=null, ordersNo=202506051703008869, payTime=null, createTime=2025-06-05 17:03:00, couponId=null, couponMoney=null, couponName=null, openDoor=null, productId=null, productName=null, minutePrice=0.58, minuteNum=0, minuteMoney=0, sumMoney=0, cashPledge=0, userMoneyMinuteNum=null, userMoneyMinuteMoney=null, couponIds=null, couponMoneys=null, couponNames=null, product=null, remark=null, refundMoney=null, refundRemark=null, isDelete=null, zhiUserId=1114, zhiMoney=3.50000, feiUserId=1114, feiMoney=1.75000, money=null, addHour=null, addPayMoney=null, hour=1, ordersType=1, smallHardwareId=null, smallGoodsId=null, isSubscription=null, isHourMsg=null, isCashMsg=null, hourMoney=35.00, shopMoney=null, isShop=null, goodsCategory=1, vipMoney=17.500, vipIndex=4, userRemark=null, isLuck=null, isBirthday=null, platformType=null, priceType=1, vip=null, tearoomName=null, tearoomTitleImage=null, couponUser=null, tearoom=null, zhiUserName=null, feiUserName=null, payUserName=null, payAvatar=null, payPhone=null, iconImg=null, vipList=null, tbOrder=null, payMinuteMoney=null)";

        String json = convertToJson(data);
        System.out.println(json);
    }
}
