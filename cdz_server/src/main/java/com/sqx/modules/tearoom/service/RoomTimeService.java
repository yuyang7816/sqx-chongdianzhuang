package com.sqx.modules.tearoom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.tearoom.entity.RoomTime;

import java.util.List;

public interface RoomTimeService extends IService<RoomTime> {

    List<Long> findAvailableGoodsIds(List<Long> goodsIds,
                                     String startDateTime,
                                     String endDateTime);

    List<RoomTime> selectRoomTimeList(Long goodsId, String date);

    List<RoomTime> selectRoomTimeListByDate(Long goodsId,String date);

    RoomTime selectRoomTimeByDateTime(Long goodsId,String date,String time);

    void deleteRoomTimeByOrdersNo(String ordersNo);

    Result updateRoomTimeByDate(String date, Long goodsId, String times);

}
