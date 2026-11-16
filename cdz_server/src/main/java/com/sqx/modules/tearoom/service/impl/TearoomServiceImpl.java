package com.sqx.modules.tearoom.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.chats.utils.DateUtil;
import com.sqx.modules.tearoom.dao.TearoomDao;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Room;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.tearoom.service.TearoomService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TearoomServiceImpl extends ServiceImpl<TearoomDao, Tearoom> implements TearoomService {

    @Autowired
    private RoomService roomService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private UserService userService;

    @Override
    public Result selectTearoomList(Integer page, Integer limit, String city, Integer status, String tearoomName,
                                    String longitude, String latitude, Integer sort,Long userId,String userName,Long tearoomUserId,
                                    String tearoomPhone,Integer shopType,Integer isVipPrivilege,Integer tearoomType,Long byUserId){
        if(page==null || limit==null){
            List<Tearoom> tearooms = baseMapper.selectTearoomList(city, status, tearoomName, longitude, latitude, sort,
                    userId, userName, tearoomUserId,tearoomPhone,shopType,isVipPrivilege,tearoomType);
            return Result.success().put("data",tearooms);
        }
        IPage<Tearoom> tearoomIPage = baseMapper.selectTearoomPage(new Page<>(page, limit), city, status,
                tearoomName, longitude, latitude, sort, userId, userName, tearoomUserId,tearoomPhone,shopType,isVipPrivilege,tearoomType,byUserId);
        return Result.success().put("data",new PageUtils(tearoomIPage));
    }

    @Override
    public Tearoom selectTearoomById(Long tearoomId,String longitude, String latitude){
        return baseMapper.selectTearoomById(tearoomId, longitude, latitude);
    }

    @Override
    public Integer queryTearoomCount(Integer type,String date){
        return baseMapper.queryTearoomCount(type, date);
    }

    @Override
    public Result insertGoodsGate(Long tearoomId,String roomNo,String roomName,String innerCode){
        Tearoom tearoom = baseMapper.selectById(tearoomId);
        tearoom.setRoomNo(roomNo);
        tearoom.setRoomName(roomName);
        tearoom.setInnerCode(innerCode);
        baseMapper.updateById(tearoom);
        List<Goods> goodsList = goodsService.list(new QueryWrapper<Goods>().eq("tearoom_id", tearoomId));
        for (Goods goods:goodsList){
            //先查询有没有关联大门 如果有 则取消掉 重新关联
            roomService.remove(new QueryWrapper<Room>().eq("goods_id",goods.getGoodsId()).eq("room_type",2));
            Room room=new Room();
            room.setGoodsId(goods.getGoodsId());
            room.setRoomNo(roomNo);
            room.setRoomName(roomName);
            room.setRoomType("2");
            room.setCreateTime(DateUtil.createTime());
            roomService.save(room);
        }
        return Result.success();
    }

    @Override
    public Tearoom selectTearoomByDistance(String longitude,String latitude){
        return baseMapper.selectTearoomByDistance(longitude,latitude);
    }

    @Override
    public IPage<Tearoom> selectTearoomByOrdersUserId(Integer page,Integer limit,String longitude, String latitude, Long userId,Integer tearoomType){
        return baseMapper.selectTearoomByOrdersUserId(new Page<>(page,limit), longitude, latitude, userId,tearoomType);
    }



}
