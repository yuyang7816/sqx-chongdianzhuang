package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.modules.app.entity.UserBrowse;
import com.sqx.modules.app.response.UserFollowResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserBrowseDao extends BaseMapper<UserBrowse> {

    IPage<Map<String, Object>> selectMyBrowse(IPage iPage, Long userId);

    List<UserFollowResponse> selectMyVisitor1(Long userId);

    List<UserFollowResponse> selectMyBrowse1(Long userId);

}
