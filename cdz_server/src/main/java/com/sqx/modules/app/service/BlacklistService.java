package com.sqx.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.Blacklist;

public interface BlacklistService extends IService<Blacklist> {

    Result insertBlacklist(Long userId,Long byUserId);

    Result selectBlacklist(Integer page,Integer limit,Long userId);

    Blacklist selectBlacklistByUserId(Long userId,Long byUserId);
}

