package com.sqx.modules.tearoom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.tearoom.dao.RoomTimeDao;
import com.sqx.modules.tearoom.entity.RoomTime;
import com.sqx.modules.tearoom.service.RoomTimeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomTimeServiceImpl extends ServiceImpl<RoomTimeDao, RoomTime> implements RoomTimeService {


    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * 查询在指定时间范围内未被预约的房间ID
     * @param goodsIds 待查询的房间ID集合
     * @param startDateTime 开始时间（格式：yyyy-MM-dd HH:mm）
     * @param endDateTime 结束时间（格式：yyyy-MM-dd HH:mm）
     * @return 未被预约的房间ID集合
     */
    @Override
    public List<Long> findAvailableGoodsIds(List<Long> goodsIds,
                                               String startDateTime,
                                               String endDateTime) {
        // 解析开始和结束时间
        LocalDateTime start = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime end = LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        String startDate = start.toLocalDate().toString();
        String endDate = end.toLocalDate().toString();
        String startTime = start.format(TIME_FORMATTER);
        String endTime = end.format(TIME_FORMATTER);

        // 查询在时间范围内已被预约的房间ID
        QueryWrapper<RoomTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("goods_id", goodsIds);

        // 处理跨天情况
        if (startDate.equals(endDate)) {
            // 同一天：日期相同且时间有重叠
            queryWrapper.eq("room_date", startDate)
                    .and(qw -> qw.le("room_time", endTime)
                            .ge("room_time", startTime));
        } else {
            // 跨天：包含开始日期的后半段和结束日期的前半段
            queryWrapper.and(qw -> qw.eq("room_date", startDate)
                            .ge("room_time", startTime))
                    .or()
                    .eq("room_date", endDate)
                    .le("room_time", endTime);
        }

        // 获取已被预约的房间ID
        List<Long> bookedGoodsIds = baseMapper.selectList(queryWrapper)
                .stream()
                .map(RoomTime::getGoodsId)
                .distinct()
                .collect(Collectors.toList());

        // 计算未被预约的房间ID（原始集合减去已预约集合）
        return goodsIds.stream()
                .filter(goodsId -> !bookedGoodsIds.contains(goodsId))
                .collect(Collectors.toList());
    }


    @Override
    public List<RoomTime> selectRoomTimeList(Long goodsId,String date){
        return baseMapper.selectList(new QueryWrapper<RoomTime>().eq("goods_id",goodsId).eq("room_date",date));
    }

    @Override
    public List<RoomTime> selectRoomTimeListByDate(Long goodsId, String date) {
        // 生成当天所有“半小时粒度”的时间字符串（共48个：00:00、00:30、01:00...23:30）
        List<String> timeList = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            // 补前导0（如 2→02）
            String hourStr = StringUtils.leftPad(String.valueOf(hour), 2, '0');
            // 添加“整点”（如 00:00）
            timeList.add(hourStr + ":00");
            // 添加“半点”（如 00:30）
            timeList.add(hourStr + ":30");
        }
        // 直接返回批量查询结果
        return baseMapper.selectByGoodsIdDateAndTimes(goodsId, date, timeList);
    }

    @Override
    public RoomTime selectRoomTimeByDateTime(Long goodsId,String date,String time){
        return baseMapper.selectOne(new QueryWrapper<RoomTime>().eq("goods_id",goodsId).eq("room_date",date).eq("room_time",time));
    }

    @Override
    public void deleteRoomTimeByOrdersNo(String ordersNo){
        baseMapper.delete(new QueryWrapper<RoomTime>().eq("orders_no",ordersNo));
    }

    @Override
    public Result updateRoomTimeByDate(String date, Long goodsId, String times){
        List<RoomTime> roomTimes = selectRoomTimeList(goodsId, date);
        List<String> dbTimes = roomTimes.stream()
                .map(RoomTime::getRoomTime)
                .collect(Collectors.toList());
        String[] split = times.split(",");
        List<String> frontTimes = new ArrayList<>(Arrays.asList(split));
        // 找出需要新增的时间段（前端有，数据库没有）
        List<String> toAdd = frontTimes.stream()
                .filter(time -> !dbTimes.contains(time))
                .collect(Collectors.toList());
        // 找出需要删除的时间段（数据库有，前端没有）
        List<Long> toRemoveIds = roomTimes.stream()
                .filter(roomTime -> !frontTimes.contains(roomTime.getRoomTime()))
                .map(RoomTime::getRoomTimeId)  // 获取需要删除的ID
                .collect(Collectors.toList());
        if(!toRemoveIds.isEmpty()){
            baseMapper.deleteBatchIds(toRemoveIds);
        }
        String createTime = DateUtils.format(new Date());
        for (String timeStr:toAdd){
            RoomTime roomTime=new RoomTime();
            roomTime.setRoomDate(date);
            roomTime.setRoomTime(timeStr);
            roomTime.setGoodsId(goodsId);
            roomTime.setCreateTime(createTime);
            baseMapper.insert(roomTime);
        }
        return Result.success();
    }





}
