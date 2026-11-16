package com.sqx.modules.tearoom.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.GoodsTime;
import com.sqx.modules.tearoom.entity.PriceResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface GoodsTimeDao extends BaseMapper<GoodsTime> {

    GoodsTime selectGoodsTimeByTime(String time,Long tearoomId);

    GoodsTime selectGoodsTimeByRuleType(String time,Long tearoomId);

}
