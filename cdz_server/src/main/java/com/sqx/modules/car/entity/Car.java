package com.sqx.modules.car.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description car
 * @author fang
 * @date 2022-07-25
 */
@Data
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 车辆id
     */
    @TableId(type = IdType.AUTO)
    private Long carId;

    /**
     * 车辆型号
     */
    private String carType;

    /**
     * 车辆类型
     */
    private String carClassify;

    /**
     * 车辆号码
     */
    private String carNo;

    /**
     * 车辆颜色
     */
    private String carColor;

    /**
     * 车主名称
     */
    private String carName;

    /**
     * 车主电话
     */
    private String carPhone;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private String createTime;

    private String carLogo;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String phone;

    public Car() {}
}
