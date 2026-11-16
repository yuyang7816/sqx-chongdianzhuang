package com.sqx.modules.utils;


import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * 金额计算工具类
 * @author fang
 * @date 2020-04-17
 */
public class AmountCalUtils {


    //金额计算 加法
    public static BigDecimal add(BigDecimal b1,BigDecimal b2){
        return b1.add(b2);
    }

    public static double adds(double value1,double value2){
        BigDecimal b1 = new BigDecimal(Double.valueOf(value1));
        BigDecimal b2 = new BigDecimal(Double.valueOf(value2));
        return b1.add(b2).doubleValue();
    }

    //金额计算 减法
    public static BigDecimal sub(BigDecimal n1, BigDecimal n2) {
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        double v = n1.subtract(n2).doubleValue();
        return new BigDecimal(formater.format(v));
    }

    //金额计算 乘法
    public static Double mul(double v1, double v2) {
        BigDecimal n1 = new BigDecimal(Double.toString(v1));
        BigDecimal n2 = new BigDecimal(Double.toString(v2));
        return n1.multiply(n2).doubleValue();
    }

    //金额计算 乘法
    public static BigDecimal mulMoney(BigDecimal n1, BigDecimal n2) {
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        BigDecimal multiply = n1.multiply(n2);
        return new BigDecimal(formater.format(multiply));
    }

    //金额计算 除法
    public static Double divide(double v1, double v2) {
        BigDecimal n1 = new BigDecimal(Double.toString(v1));
        BigDecimal n2 = new BigDecimal(Double.toString(v2));
        return n1.divide(n2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private final static DecimalFormat formater = new DecimalFormat();

    //金额计算除法，保留小数点后两位
    public static Double moneyDivide(BigDecimal n1, BigDecimal n2){
        BigDecimal v = n1.divide(n2, 2, BigDecimal.ROUND_HALF_UP);
        System.out.println(v);
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return Double.parseDouble(formater.format(v));
    }

    public static Double moneyDivides(BigDecimal n1, BigDecimal n2){
        BigDecimal v = n1.divide(n2, 2, BigDecimal.ROUND_HALF_UP);
        System.out.println(v);
        formater.setMaximumFractionDigits(2);
        formater.setGroupingSize(0);
        formater.setRoundingMode(RoundingMode.FLOOR);
        return Double.parseDouble(formater.format(v));
    }

    public static Double moneyMul(double v1, double v2){
        BigDecimal n1 = new BigDecimal(Double.toString(v1));
        BigDecimal n2 = new BigDecimal(Double.toString(v2));
        double v = n1.multiply(n2).doubleValue();
        return Double.parseDouble(df.format(v));
    }

    private final static DecimalFormat df = new DecimalFormat("######0.00");
    //金额计算乘法，保留小数点后两位
    public static Double moneySum(String sum){
        if (StringUtils.isEmpty(sum)){
            sum = "0";
        }
        double v = Double.parseDouble(sum);
        return Double.parseDouble(df.format(v));
    }

}
