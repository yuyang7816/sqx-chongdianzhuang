package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @description app
 * @author fang
 * @date 2023-12-04
 */
@Data
public class App implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    /**
     * app升级配置
     */
    private Long id;

    /**
     * 安卓升级地址
     */
    private String androidWgtUrl;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 描述
     */
    private String des;

    /**
     * 苹果升级版本号
     */
    private String iosVersion;

    /**
     * 苹果升级地址
     */
    private String iosWgtUrl;

    /**
     * 是否强制升级
     */
    private String method;

    /**
     * 安卓升级版本号
     */
    private String version;

    /**
     * 通用下载地址
     */
    private String wgtUrl;

    public App() {}
}
