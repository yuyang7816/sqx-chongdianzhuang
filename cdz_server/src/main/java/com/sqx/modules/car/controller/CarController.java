package com.sqx.modules.car.controller;


import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.car.entity.Car;
import com.sqx.modules.car.service.CarService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 用户车辆信息
 */
@RestController
@RequestMapping("/car")
@Api(value = "用户车辆信息", tags = {"用户车辆信息"})
public class CarController {

    @Autowired
    private CarService carService;

    @PostMapping("/insertCar")
    @ApiOperation("添加车辆信息")
    public Result insertCar(@RequestBody Car car){
        car.setCreateTime(DateUtils.format(new Date()));
        carService.save(car);
        return Result.success();
    }

    @PostMapping("/updateCar")
    @ApiOperation("修改车辆信息")
    public Result updateCar(@RequestBody Car car){
        carService.updateById(car);
        return Result.success();
    }

    @PostMapping("/deleteCar")
    @ApiOperation("删除车辆信息")
    public Result deleteCar(Long carId){
        carService.removeById(carId);
        return Result.success();
    }

    @GetMapping("/selectCarList")
    @ApiOperation("查询车辆信息")
    public Result selectCarList(Integer page,Integer limit,Long userId,String userName,String phone){
        return carService.selectCarList(page, limit, userId, userName, phone);
    }


}
