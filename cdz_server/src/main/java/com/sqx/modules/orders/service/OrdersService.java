package com.sqx.modules.orders.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.utils.excel.ExcelData;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface OrdersService extends IService<Orders> {

    Result selectOrdersByUserId(Long userId);

    Result selectOrdersMoney(Long userId,Long goodsId,Integer hour);

    Result insertOrders(Orders orders);

    Result payOrders(String ordersNo);

    Orders selectOrdersByNo(String ordersNo);

    Orders selectOrdersDetailsVip(String ordersNo);

    void sendPayOrdersMsg(Orders orders);

    Result selectOrdersList(Integer page,Integer limit,Long userId,String userName,String phone,
                            String ordersNo,String goodsName,Integer status,Long tearoomId,
                            String startTime,String endTime,Integer isDelete,Integer payWay,
                            Integer money,Integer isShop,String dateTime,String cameraId,
                            Integer goodsCategory,String tearoomName,Integer platformType,Long openUserId,Integer ordersType);

    ExcelData excelOrders(Long userId, String userName, String phone,
                          String ordersNo, String goodsName, Integer status, Long tearoomId,
                          String startTime, String endTime, Integer isDelete,Integer payWay,Integer money,
                          Integer isShop,String dateTime,Integer goodsCategory,String tearoomName,
                          Integer platformType,Long openUserId,Integer ordersType);

    Result cancelOrders(String ordersNo);

    Result refundOrders(String ordersNo);

    Result refundOrdersMoney(String ordersNo,BigDecimal refundMoney,String refundRemark);

    Result removeOrders(Long ordersId);

    Result selectTeamUserList(Integer page, Integer limit, String invitationCode,Integer type,Long userId);

    Double selectOrdersMoneyCountByUserId(Long userId,Integer type);

    Integer selectUserCountByInvitationCode(String invitationCode,Integer type);

    Result selectTearoomOrdersCount(Integer page,Integer limit,Integer flag,String time);

    Double selectOrdersMoneyByTearoom(Integer flag,String time,Long tearoomId,Integer payWay);

    Integer selectOrdersCountByTearoom(Integer flag,String time,Long tearoomId,Integer payWay);

    Double selectOrdersMoneyByGoods(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime);

    Integer selectOrdersCountByGoods(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime);

    Result roomChange(Long ordersId,Long goodsId);

    Result closeOrders(Long ordersId);

    Double selectOrdersMoneyByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime,Integer isShop);

    Double selectOrdersSumShopMoneyByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime,Integer isShop);

    Double selectOrdersSumPayMoneyByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime,Integer isShop);

    Integer selectOrdersShopCountByTearoomId(Integer flag,String time,Long tearoomId,Integer payWay,String endTime);

    Result selectUserByOrdersMoneyList(Integer page,Integer limit,Long tearoomId);

    Double selectTearoomPayMoneyCountByTime(Integer flag,String time,Integer classify);

    Double selectTearoomShopMoneyCountByTime(Integer flag,String time,Integer classify);

    Double selectTearoomCashPledgeCountByTime(Integer flag,String time,Integer classify);

    Double selectTearoomSumMoneyCountByTime(Integer flag,String time,Integer classify);

    IPage<Map<String,Object>> selectUserByOrdersHourList(Integer page, Integer limit, Integer type, String userName, String phone, String city,
                                                         Long tearoomId,String startTime,String endTime,String tearoomName);

    IPage<Map<String,Object>> selectOrdersUserMoneyList(Integer page,Integer limit,String userName,String phone,String startTime,String endTime);
    
    Map<String,Object> selectUserByOrdersHourByUserId(Long userId,Integer type,String city,Long tearoomId);

    Integer selectOrdersHourByTearoomId(Long tearoomId,Integer type);

    Integer selectOrdersGoodsHourByTearoomId(Long goodsId,String startTime,String endTime,Integer isShop);

    Double selectOrdersGoodsMoneyByTearoomId(Long goodsId,String startTime,String endTime,Integer isShop);

    Double selectOrdersPayMoneyByTime(Integer type,String time);

    Double selectOrdersShopMoneyByTime(Integer type,String time);

    Double selectOrdersFenMoneyByTime(Integer type,String time);

    Integer selectOrdersCountByNoIsShop(Integer type,String time);

    Integer selectOrdersMoneyCountByNoIsShop(Integer type,String time);

    Integer selectOrdersCouponCountByNoIsShop(Integer type,String time);

    Double selectShopOpenOrdersMoney(Long tearoomId,String time,Integer type,Integer shopWay,Long openUserId);

    Integer selectShopOpenOrdersCount(Long tearoomId,String time,Integer type,Integer shopWay,Long openUserId);

    Result shopUpdateOrdersStatus(Long ordersId,Long ordersGoodsId);

    Double selectOrdersSumMinuteByGoodsId(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime);

    Double selectOrdersSumMoneyByGoodsId(Integer flag,String time,Long tearoomId,Long goodsId,Integer payWay,String endTime);

    Double selectOrdersMoneyByStartTimeAndEndTime(Long tearoomId,Integer flag,String startTime,String endTime,Integer platformType);

    Integer selectOrdersCountByStartTimeAndEndTime(Long tearoomId,Integer flag,String startTime,String endTime,Integer platformType);

    Result selectOrdersCountByDate(Long tearoomId, String startTime, String endTime, Integer type);

    Result selectOrdersDateByTearoomId(Long tearoomId);

}
