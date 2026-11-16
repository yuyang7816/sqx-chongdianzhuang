package com.sqx.modules.tearoom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.GoodsTime;

public interface GoodsTimeService extends IService<GoodsTime> {

    GoodsTime selectGoodsTimeByTime(String time,Long tearoomId);

}
