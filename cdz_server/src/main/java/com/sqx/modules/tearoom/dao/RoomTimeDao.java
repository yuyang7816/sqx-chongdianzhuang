package com.sqx.modules.tearoom.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sqx.modules.tearoom.entity.RoomTime;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface RoomTimeDao extends BaseMapper<RoomTime> {

    /**
     * 批量查询指定商品、日期、多个时间点的占用记录
     * @param goodsId 商品ID
     * @param roomDate 日期（如 2025-09-15）
     * @param roomTimeList 时间列表（如 ["00:00", "00:30", ..., "23:30"]）
     * @return 已占用的RoomTime列表
     */
    List<RoomTime> selectByGoodsIdDateAndTimes(
            @Param("goodsId") Long goodsId,
            @Param("roomDate") String roomDate,
            @Param("roomTimeList") List<String> roomTimeList
    );

}
