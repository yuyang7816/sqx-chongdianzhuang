package com.sqx.modules.car.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.car.dao.CarDao;
import com.sqx.modules.car.entity.Car;
import com.sqx.modules.car.service.CarService;
import org.springframework.stereotype.Service;


@Service("CarService")
public class CarServiceImpl extends ServiceImpl<CarDao, Car> implements CarService {


	@Override
	public Result selectCarList(Integer page, Integer limit, Long userId, String userName, String phone){
		return Result.success().put("data",baseMapper.selectCarList(new Page<>(page,limit),userId,userName,phone));
	}





}
