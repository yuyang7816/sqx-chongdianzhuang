package com.sqx.modules.tearoom.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.tearoom.entity.Room;

import java.math.BigDecimal;

public interface RoomService extends IService<Room> {

    Result insertRoom(Room room);

    Result updateRoom(Room room);

    Result removeById(Long roomId);

    Result selectGroup(String innerCode);

    JSONObject selectDevice(String deviceNo);

    JSONObject saveGroupBind(String innerCode,String isBind);

    JSONObject issueInstruct(String instructId);

    Result issueInstruct(Long roomId,Integer type);

    Result issueInstruct(Long roomId,Integer type,String tokenType,String accessToken);

    Result airConditionerControl(String clientId,Integer isOpen,String tokenType,String accessToken);

    Result closeIssueInstruct(Long roomId);

    void close(Long goodsId);

    Result lightControl(String keyCode,String channels,Integer isOpen);

    Result musicControl(String token,String id,String message);

    Result airConditionerControl(String clientId,Integer isOpen);

    Result damControl(Room room,Integer isOpen);

    String getDamToken();

    Result damMusicControl(Room room,Integer isOpen);

    BigDecimal getDamWeightControl(Room room);

}
