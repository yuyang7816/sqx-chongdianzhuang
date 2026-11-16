package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

/**
 * @description blacklist
 * @author fang
 * @date 2022-09-21
 */
@Data
public class Blacklist implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * 拉黑id
     */
    private Long blacklistId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 拉黑用户id
     */
    private Long byUserId;
    @TableField(exist = false)
    private UserEntity user;


    /**
     * 时间
     */
    private String createTime;

    public Blacklist() {}
}
