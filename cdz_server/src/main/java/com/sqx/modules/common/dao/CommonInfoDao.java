package com.sqx.modules.common.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.common.entity.CommonInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/8
 */
@Mapper
public interface CommonInfoDao extends BaseMapper<CommonInfo> {

    List<CommonInfo> findByCondition(@Param("condition") String condition);

    CommonInfo findOne(@Param("type") int type);

    IPage<Map<String,Object>> selectTalkPage(Page<Map<String,Object>> page, @Param("status") Integer status,
                                             @Param("userId") Long userId, @Param("byUserId") Long byUserId,
                                             @Param("followUserId") Long followUserId,@Param("isDelete") Integer isDelete,
                                             @Param("city") String city,@Param("sex") Integer sex,@Param("sort") Integer sort,
                                             @Param("longitude") String longitude,@Param("latitude") String latitude,
                                             @Param("address") String address,@Param("goodUserId") Long goodUserId,
                                             @Param("userName")String userName,@Param("content")String content,
                                             @Param("isRecommend")Integer isRecommend,@Param("circleId")String circleId,
                                             @Param("circleName") String circleName);

    IPage<Map<String,Object>> selectCommentPage(Page<Map<String,Object>> page, Long userId, Long talkId,String userName,
                                                String phone,String content,Long parentId);


    List<Map<String, Object>> searchTalk(@Param("content")String content,@Param("byUserId")Long byuserId);

}