package com.sqx.modules.orders.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.orders.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersDao extends BaseMapper<Orders> {

    IPage<Orders> selectOrdersPage(Page<Orders> page, Long userId, String userName, String phone,
                                   String ordersNo, String goodsName, Integer status,Long tearoomId,
                                   String startTime,String endTime,Integer isDelete,Integer payWay,Integer money,
                                   Integer isShop,String dateTime,String cameraId,Integer goodsCategory,String tearoomName,
                                   Integer platformType,Long openUserId,Integer ordersType);

    List<Orders> selectOrdersList(Long userId, String userName, String phone,
                                   String ordersNo, String goodsName, Integer status,Long tearoomId,
                                   String startTime,String endTime,Integer isDelete,Integer payWay,Integer money,Integer isShop,
                                  String dateTime,Integer goodsCategory,String tearoomName,Integer platformType,Long openUserId,Integer ordersType);

    int updateStartOrdersStatus();

    int updateOpenDoorOrders(String minute);

    List<Orders> selectOpenDoorOrdersList(String minute);

    int updateCloseDoorOrders(String minute);

    List<Orders> selectCloseDoorOrdersList(String minute);

    int updateEndOrdersStatus();

    List<Orders> selectEndOrdersStatusList();

    List<Orders> selectEndOrdersOpenDoorList();

    List<Orders> selectStartOrdersStatusList();

    IPage<Map<String,Object>> selectTeamUserList(Page<Map<String,Object>> page,@Param("invitationCode") String invitationCode,@Param("type") Integer type,@Param("userId") Long userId);

    Double selectOrdersMoneyCountByUserId(Long userId,Integer type);

    Integer selectUserCountByInvitationCode(String invitationCode,Integer type);

    IPage<Map<String,Object>> selectTearoomOrdersCount(Page<Map<String,Object>> page,@Param("flag") Integer flag,@Param("time") String time);

    Double selectOrdersMoneyByTearoom(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,@Param("payWay") Integer payWay);

    Integer selectOrdersCountByTearoom(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,@Param("payWay") Integer payWay);

    Double selectOrdersMoneyByGoods(@Param("flag") Integer flag,@Param("time") String time,Long tearoomId,@Param("goodsId") Long goodsId,
                                    @Param("payWay") Integer payWay,@Param("endTime") String endTime);

    Integer selectOrdersCountByGoods(@Param("flag") Integer flag,@Param("time") String time,Long tearoomId,@Param("goodsId") Long goodsId,
                                     @Param("payWay") Integer payWay,@Param("endTime") String endTime);

    Double selectOrdersMoneyByTearoomId(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,
                                     @Param("payWay") Integer payWay,@Param("endTime") String endTime,@Param("isShop") Integer isShop);

    List<Orders> selectCloseOrdersList(String time);

    List<Orders> selectOverTimeOrdersList(String time);

    Double selectOrdersSumShopMoneyByTearoomId(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,
                                        @Param("payWay") Integer payWay,@Param("endTime") String endTime,@Param("isShop") Integer isShop);

    Double selectOrdersSumPayMoneyByTearoomId(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,
                                        @Param("payWay") Integer payWay,@Param("endTime") String endTime,@Param("isShop") Integer isShop);

    Integer selectOrdersShopCountByTearoomId(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,
                                               @Param("payWay") Integer payWay,@Param("endTime") String endTime);

    IPage<Map<String,Object>> selectUserByOrdersMoneyList(Page<Map<String,Object>> page,Long tearoomId);

    Double selectTearoomPayMoneyCountByTime(@Param("flag") Integer flag,@Param("time") String time,@Param("classify") Integer classify);

    Double selectTearoomShopMoneyCountByTime(@Param("flag") Integer flag,@Param("time") String time,@Param("classify") Integer classify);

    Double selectTearoomCashPledgeCountByTime(@Param("flag") Integer flag,@Param("time") String time,@Param("classify") Integer classify);

    Double selectTearoomSumMoneyCountByTime(@Param("flag") Integer flag,@Param("time") String time,@Param("classify") Integer classify);

    IPage<Map<String,Object>> selectUserByOrdersHourList(Page<Map<String,Object>> page,Integer type,String userName,String phone,String city,
                                                         Long tearoomId,String startTime,String endTime,String tearoomName);

    Map<String,Object> selectUserByOrdersHourByUserId(Long userId,Integer type,String city,Long tearoomId);

    Integer selectOrdersHourByTearoomId(Long tearoomId,Integer type);

    Integer selectOrdersGoodsHourByTearoomId(Long goodsId,String startTime,String endTime,Integer isShop);

    Double selectOrdersGoodsMoneyByTearoomId(Long goodsId,String startTime,String endTime,Integer isShop);

    Double selectOrdersPayMoneyByTime(Integer type,String time);

    Double selectOrdersShopMoneyByTime(Integer type,String time);

    Double selectOrdersFenMoneyByTime(Integer type,String time);

    Orders selectOrdersByVideoTime(Long tearoomId,Long goodsId,String videoTime);

    IPage<Map<String,Object>> selectOrdersUserMoneyList(Page<Map<String,Object>> page,String userName,String phone,String startTime,String endTime);

    Integer selectOrdersHourByUserId(Long userId);

    Integer selectOrdersCountByNoIsShop(Integer type,String time);

    Integer selectOrdersMoneyCountByNoIsShop(Integer type,String time);

    Integer selectOrdersCouponCountByNoIsShop(Integer type,String time);

    Double selectShopOpenOrdersMoney(Long tearoomId,String time,Integer type,Integer shopWay,Long openUserId);

    Integer selectShopOpenOrdersCount(Long tearoomId,String time,Integer type,Integer shopWay,Long openUserId);

    List<Map<String,Object>> selectEmployeeUserList(String userIds,String time,Integer type);

    BigDecimal selectOrdersShopMoney(Long tearoomId,Integer shopWay,String startTime,String endTime);

    Integer selectOrdersShopCount(Long tearoomId,Integer shopWay,String startTime,String endTime);

    BigDecimal selectOrdersSmqMoney(Long tearoomId,Integer platformType,String startTime,String endTime);

    Integer selectOrdersSmqCount(Long tearoomId,Integer platformType,String startTime,String endTime);

    BigDecimal selectOrdersYhMoney(Long tearoomId,Integer platformType,Integer payWay,String startTime,String endTime);

    Integer selectOrdersYhCount(Long tearoomId,Integer platformType,Integer payWay,String startTime,String endTime);

    Double selectOrdersSumMinuteByGoodsId(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,
                                          @Param("goodsId") Long goodsId,@Param("payWay") Integer payWay,@Param("endTime") String endTime);

    Double selectOrdersSumMoneyByGoodsId(@Param("flag") Integer flag,@Param("time") String time,@Param("tearoomId") Long tearoomId,
                                         @Param("goodsId") Long goodsId,@Param("payWay") Integer payWay,@Param("endTime") String endTime);

    Double selectOrdersMoneyByStartTimeAndEndTime(Long tearoomId,Integer flag,String startTime,String endTime,Integer platformType);

    Integer selectOrdersCountByStartTimeAndEndTime(Long tearoomId,Integer flag,String startTime,String endTime,Integer platformType);


}
