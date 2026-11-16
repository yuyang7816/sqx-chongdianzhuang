package com.sqx.modules.orders.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.entity.OrdersVideo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrdersVideoDao extends BaseMapper<OrdersVideo> {

    IPage<OrdersVideo> selectOrdersVideoList(Page<Orders> page,Long ordersId,Long tearoomId,Long goodsId,String goodsName,String userName,String phone,
                                             String ordersNo,String tearoomName,Long userId,Integer classify,Long shopId);

}
