package com.sqx.modules.city.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description city
 * @author fang
 * @date 2023-09-18
 */
@Data
public class City implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 城市id
     */
    private Long cityId;

    /**
     * 城市
     */
    private String city;

    /**
     * 状态 1开启 2关闭
     */
    private Integer status;

    /**
     * 创建时间
     */
    private String createTime;

    public City() {}
}
