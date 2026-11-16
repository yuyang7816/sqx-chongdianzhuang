package com.sqx.modules.utils;

import com.sqx.common.utils.Result;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

public class TimeCalculator {

    /**
     * 计算当前时间到结束时间之间的小时差（带小数）
     * @param currentTime 当前时间
     * @param endTime 结束时间
     * @return 小时差（如2.65小时），如果结束时间在当前时间之前则返回0
     */
    public static double getHoursBetween(LocalTime currentTime, LocalTime endTime) {
        // 判断结束时间是否在当前时间之后
        if (endTime.isBefore(currentTime) || endTime.equals(currentTime)) {
            return 0.0;
        }

        // 计算两个时间之间的持续时间
        Duration duration = Duration.between(currentTime, endTime);

        // 转换为总分钟数，再换算成小时（保留两位小数）
        long totalMinutes = duration.toMinutes();
        return Math.round((totalMinutes / 60.0) * 100.0) / 100.0;
    }

    public static void main(String[] args) {
        // 示例：计算9:21到12:00的小时差
        LocalTime current = LocalTime.of(12, 0);
        LocalTime end = LocalTime.of(12, 0);

        double hours = getHoursBetween(current, end);
        if(hours<=0){
            System.out.println("当前时间已不足,请更换其他优惠券！"); //
        }
        System.out.println("剩余时间：" + hours + "小时"); // 输出：剩余时间：2.65小时

        // 其他测试用例
        LocalTime current2 = LocalTime.of(10, 12);
        double hours2 = getHoursBetween(current2, end);
        System.out.println("剩余时间：" + hours2 + "小时"); // 输出：剩余时间：1.8小时

        String[] time1 = "16:36-18:36".split("-");
        String[] endTime = time1[1].split(":");
        LocalTime ends = LocalTime.of(Integer.parseInt(endTime[0]), Integer.parseInt(endTime[1]));

        // 2. 获取今天的日期
        LocalDate today = LocalDate.now();

        // 3. 将日期和时间组合成LocalDateTime
        LocalDateTime dateTime = LocalDateTime.of(today, ends);

        // 4. 定义目标格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // 5. 格式化并转换为字符串
        String result = dateTime.format(formatter);

        // 输出结果
        System.out.println("转换后的字符串：" + result);


    }


    /**
     * 给Calendar添加double类型的小时数（支持小数）
     * @param calendar 目标Calendar对象
     * @param hours 要添加的小时数（如2.21）
     */
    public static Calendar addDoubleHours(Calendar calendar, double hours) {
        // 处理负数情况（减少时间）
        if (hours < 0) {
            throw new IllegalArgumentException("建议使用正数小时，如需减少时间可传入负数，但需注意逻辑");
        }

        // 拆分整数小时和小数部分
        int wholeHours = (int) Math.floor(hours); // 整数部分（如2.21 → 2）
        double fractionalPart = hours - wholeHours; // 小数部分（如2.21 → 0.21）

        // 小数部分转换为分钟（四舍五入）
        int minutes = (int) Math.round(fractionalPart * 60);

        // 分别添加小时和分钟
        calendar.add(Calendar.HOUR_OF_DAY, wholeHours); // 使用HOUR_OF_DAY避免12小时制问题
        calendar.add(Calendar.MINUTE, minutes);
        return calendar;
    }

   /* public static void main(String[] args) {
        // 测试：当前时间添加2.21小时
        Calendar calendar = Calendar.getInstance();
        System.out.println("添加前时间：" + calendar.getTime());

        // 添加2.21小时（2小时13分钟）
        addDoubleHours(calendar, 2.21);
        System.out.println("添加后时间：" + calendar.getTime());
    }*/

}

