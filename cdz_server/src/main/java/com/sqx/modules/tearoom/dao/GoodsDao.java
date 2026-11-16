package com.sqx.modules.tearoom.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.tearoom.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDao extends BaseMapper<Goods> {

    IPage<Goods> selectGoodsPage(Page<Goods> page, Long tearoomId, String goodsName, Integer status,Integer admin,Integer goodsCategory);

    List<Goods> selectGoodsList(Long tearoomId, String goodsName, Integer status, Integer admin,Integer goodsCategory);

}
