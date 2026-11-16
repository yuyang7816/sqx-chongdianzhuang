package com.sqx.modules.tearoom.service.impl;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.tearoom.dao.GoodsDao;
import com.sqx.modules.tearoom.entity.Goods;

import com.sqx.modules.tearoom.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsDao, Goods> implements GoodsService {

    @Autowired
    private OrdersService ordersService;

    @Override
    public Result selectGoodsList(Integer page, Integer limit, Long tearoomId, String goodsName, Integer status,Integer admin,Integer goodsCategory){
        if(page==null || limit==null){
            List<Goods> goodsList = baseMapper.selectGoodsList(tearoomId, goodsName, status,admin,goodsCategory);
            for(Goods goods:goodsList){
                if(goods.getGoodsStatus()==2){
                    goods.setOrders(ordersService.getById(goods.getOrdersId()));
                }
            }
            return Result.success().put("data",goodsList);
        }else{
            IPage<Goods> goodsIPage = baseMapper.selectGoodsPage(new Page<>(page, limit), tearoomId, goodsName, status,admin,goodsCategory);
            return Result.success().put("data",new PageUtils(goodsIPage));
        }

    }

    @Override
    public Goods selectGoodsById(Long goodsId){
        return baseMapper.selectById(goodsId);
    }







}
