package com.sqx.modules.message.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.Result;
import com.sqx.modules.message.entity.MessageInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @author fang
 * @date 2020/7/9
 */
@Mapper
public interface MessageInfoDao extends BaseMapper<MessageInfo> {

    int updateSendState(@Param("userId") Long userId,@Param("state") Integer state);


}
