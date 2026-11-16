package com.sqx.modules.app.entity.dto;

import lombok.Data;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/5/8 15:10
 */
@Data
public class SearchUserDTO {
    private Long userId;
    private String userName;
    private String fansCount;
    private String userAvatar;
    /*是否关注 0未关注 1已关注*/
    private Integer followTag;
    private Integer sex;
    private Integer age;
    private String tanletName;
    private String tanletImg;
}
