package com.sqx.modules.app.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.dao.BlacklistDao;
import com.sqx.modules.app.entity.Blacklist;
import com.sqx.modules.app.entity.UserFollow;
import com.sqx.modules.app.service.BlacklistService;
import com.sqx.modules.app.service.UserFollowService;
import com.sqx.modules.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class BlacklistServiceImpl extends ServiceImpl<BlacklistDao, Blacklist> implements BlacklistService {

    @Autowired
    private UserService userService;
    @Autowired
    private UserFollowService userFollowService;

    @Override
    public Result insertBlacklist(Long userId,Long byUserId){
        Blacklist blacklist = baseMapper.selectOne(new QueryWrapper<Blacklist>().eq("user_id", userId).eq("by_user_id", byUserId));
        if(blacklist!=null){
            baseMapper.deleteById(blacklist.getBlacklistId());
            return Result.success();
        }
        blacklist=new Blacklist();
        blacklist.setUserId(userId);
        blacklist.setByUserId(byUserId);
        blacklist.setCreateTime(DateUtils.format(new Date()));
        baseMapper.insert(blacklist);
        //查询关注 如果我关注了 他 则自动取关 也自动取关
        QueryWrapper<UserFollow> queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("follow_user_id", byUserId);
        UserFollow userFollow = userFollowService.getOne(queryWrapper);
        if(userFollow!=null){
            userFollowService.removeById(userFollow.getFollowId());
        }
        queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_id", byUserId);
        queryWrapper.eq("follow_user_id", userId);
        userFollow = userFollowService.getOne(queryWrapper);
        if(userFollow!=null){
            userFollowService.removeById(userFollow.getFollowId());
        }
        return Result.success();
    }

    @Override
    public Result selectBlacklist(Integer page,Integer limit,Long userId){
        IPage<Blacklist> blacklistPage = baseMapper.selectPage(new Page<>(page, limit), new QueryWrapper<Blacklist>().eq("user_id", userId));
        List<Blacklist> records = blacklistPage.getRecords();
        for(Blacklist blacklist:records){
            blacklist.setUser(userService.selectUserById(blacklist.getByUserId()));
        }
        return Result.success().put("data",blacklistPage);
    }

    @Override
    public Blacklist selectBlacklistByUserId(Long userId,Long byUserId){
        Blacklist blacklist = baseMapper.selectOne(new QueryWrapper<Blacklist>().eq("user_id", userId).eq("by_user_id", byUserId));
        return blacklist;
    }


}
