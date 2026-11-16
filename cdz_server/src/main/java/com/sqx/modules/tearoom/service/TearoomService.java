package com.sqx.modules.tearoom.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.tearoom.entity.Tearoom;

public interface TearoomService extends IService<Tearoom> {

    Result selectTearoomList(Integer page, Integer limit, String city, Integer status, String tearoomName,
                             String longitude, String latitude, Integer sort,Long userId,String userName,Long tearoomUserId,
                             String tearoomPhone,Integer shopType,Integer isVipPrivilege,Integer tearoomType,Long byUserId);

    Tearoom selectTearoomById(Long tearoomId,String longitude, String latitude);

    Integer queryTearoomCount(Integer type,String date);

    Result insertGoodsGate(Long tearoomId,String roomNo,String roomName,String innerCode);

    Tearoom selectTearoomByDistance(String longitude,String latitude);

    IPage<Tearoom> selectTearoomByOrdersUserId(Integer page,Integer limit, String longitude, String latitude, Long userId,Integer tearoomType);

}
