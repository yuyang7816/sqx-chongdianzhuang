package com.sqx.modules.car.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.car.entity.Car;


public interface CarService extends IService<Car> {

	Result selectCarList(Integer page, Integer limit, Long userId, String userName, String phone);

}
