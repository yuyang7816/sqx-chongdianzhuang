package com.sqx.modules.integral.dao;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.integral.entity.UserIntegralDetails;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;

@Mapper
public interface UserIntegralDetailsDao extends BaseMapper<UserIntegralDetails> {

    UserIntegralDetails selectUserIntegralDetailsByUserId(@Param("userId") Long userId, @Param("time") Date time);

    IPage<Map<String,Object>> selectUserIntegralDetailsCount(Page<Map<String,Object>> page,@Param("flag") Integer flag,@Param("time") String time);

    IPage<UserIntegralDetails> selectIntegralDetailsList(Page<UserIntegralDetails> page,String content,String userName,String phone,Long userId,
                                                         String startTime,String endTime);


}
