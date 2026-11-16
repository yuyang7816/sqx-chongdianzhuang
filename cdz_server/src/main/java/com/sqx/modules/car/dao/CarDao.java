package com.sqx.modules.car.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.car.entity.Car;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface CarDao extends BaseMapper<Car> {

    IPage<Car> selectCarList(Page<Car> page, Long userId, String userName, String phone);


}
