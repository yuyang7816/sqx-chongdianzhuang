package com.sqx.modules.orders.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.entity.OrdersVideo;
import com.sqx.modules.utils.excel.ExcelData;

import java.util.Map;

public interface OrdersVideoService extends IService<OrdersVideo> {

    IPage<OrdersVideo> selectOrdersVideoList(Integer page,Integer limit,Long ordersId,Long tearoomId,Long goodsId,String goodsName,String userName,String phone,
                                             String ordersNo,String tearoomName,Long userId,Integer classify,Long shopId);


}
