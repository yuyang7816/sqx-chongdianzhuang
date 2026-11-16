package com.sqx.modules.tearoom.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.tearoom.dao.GoodsDao;
import com.sqx.modules.tearoom.dao.GoodsTimeDao;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.GoodsTime;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.GoodsTimeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class GoodsTimeServiceImpl extends ServiceImpl<GoodsTimeDao, GoodsTime> implements GoodsTimeService {

    @Override
    public GoodsTime selectGoodsTimeByTime(String time,Long tearoomId){
        return baseMapper.selectGoodsTimeByTime(time, tearoomId);
    }


}
