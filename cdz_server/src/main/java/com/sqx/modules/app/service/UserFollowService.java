package com.sqx.modules.app.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserFollow;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author liyuan
 * @description user_follow
 * @date 2021-08-12
 */
public interface UserFollowService extends IService<UserFollow> {

    /**
     * 关注 / 取消关注
     */
    Result insert(Long userId, Long followUserId);

    /**
     * 查看我的关注
     */
    Result selectMyFollow(Long userId,Long page,Long limit);

    /**
     * 查看我的粉丝
     */
    Result selectFans(Long userId,Long page,Long limit);

    Result selectFollowUser( Long userId, Long followUserId);

    int queryCount(Long userId,Long followUserId);

}
