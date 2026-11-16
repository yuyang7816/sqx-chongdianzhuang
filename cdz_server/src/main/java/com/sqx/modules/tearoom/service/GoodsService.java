package com.sqx.modules.tearoom.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.tearoom.entity.Goods;

public interface GoodsService extends IService<Goods> {

    Result selectGoodsList(Integer page, Integer limit, Long tearoomId, String goodsName, Integer status,Integer admin,Integer goodsCategory);

    Goods selectGoodsById(Long goodsId);

}
