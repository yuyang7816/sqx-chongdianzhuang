package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserFollow;
import com.sqx.modules.app.response.UserFollowResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserFollowDao extends BaseMapper<UserFollow> {

    IPage<Map<String, Object>> selectMyFollow(IPage iPage, Long userId);

    IPage<Map<String, Object>> selectFans(IPage iPage, Long userId);

    List<UserFollowResponse> selectMyFollow1(Long userId);

    List<UserFollowResponse> selectFans1(Long userId);

    int queryCount(@Param("userId") Long userId,@Param("followUserId")Long followUserId);

}
