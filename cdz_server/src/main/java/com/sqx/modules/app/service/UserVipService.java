package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserVip;
import org.springframework.web.bind.annotation.RequestAttribute;

public interface UserVipService extends IService<UserVip> {

    UserVip selectUserVipByUserId(Long userId);

    Result isUserVip(Long userId);

}
