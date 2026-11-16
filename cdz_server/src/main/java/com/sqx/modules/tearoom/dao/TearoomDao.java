package com.sqx.modules.tearoom.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.tearoom.entity.Tearoom;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface TearoomDao extends BaseMapper<Tearoom> {

    IPage<Tearoom> selectTearoomPage(Page<Tearoom> page, String city, Integer status, String tearoomName,
                                     String longitude, String latitude, Integer sort,Long userId,String userName,Long tearoomUserId,
                                     String tearoomPhone,Integer shopType,Integer isVipPrivilege,Integer tearoomType,Long byUserId);

    List<Tearoom> selectTearoomList(String city, Integer status, String tearoomName,
                                    String longitude, String latitude, Integer sort, Long userId,String userName,Long tearoomUserId,
                                    String tearoomPhone,Integer shopType,Integer isVipPrivilege,Integer tearoomType);

    Tearoom selectTearoomById(Long tearoomId,String longitude, String latitude);

    int queryTearoomCount(@Param("type") Integer type, @Param("date") String date);

    Tearoom selectTearoomByDistance(String longitude,String latitude);

    IPage<Tearoom> selectTearoomByOrdersUserId(Page<Tearoom> page,String longitude, String latitude, Long userId,Integer tearoomType);

}
