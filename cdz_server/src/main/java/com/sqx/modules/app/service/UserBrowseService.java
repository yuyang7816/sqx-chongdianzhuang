package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserBrowse;
import org.springframework.web.bind.annotation.RequestAttribute;

public interface UserBrowseService extends IService<UserBrowse> {
    Result selectMyVisitor(Long userId, Long page, Long limit);

    Result selectMyBrowse(Long userId, Long page, Long limit);

    Result selectAmount(Long userId);

    Result selectAmounts(Long userId);

    Result addAmount(Long userId, Long byBrowseId, Long takingId);

    Result addVisitor(Long userId, Long byBrowseId);

    /**
     * 删除我的访客
     *
     * @param id
     * @return
     */
    Result deleteMyVisitor(Long id);

    /**
     * 删除浏览足迹
     *
     * @param id
     * @return
     */
    Result deleteMyBrowse(Long id);
}
