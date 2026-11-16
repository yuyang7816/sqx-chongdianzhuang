package com.sqx.modules.car.controller.app;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.car.entity.Car;
import com.sqx.modules.car.service.CarService;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.utils.HttpClientUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 用户车辆信息
 */
@RestController
@RequestMapping("/app/car")
@Api(value = "用户车辆信息", tags = {"用户车辆信息"})
public class AppCarController {

    @Autowired
    private CarService carService;
    @Autowired
    private CommonInfoService commonInfoService;

    @PostMapping("/insertCar")
    @ApiOperation("添加车辆信息")
    @Login
    public Result insertCar(@RequestBody Car car,@RequestAttribute("userId") Long userId){
        car.setUserId(userId);
        car.setCreateTime(DateUtils.format(new Date()));
        carService.save(car);
        return Result.success();
    }

    @PostMapping("/updateCar")
    @ApiOperation("修改车辆信息")
    @Login
    public Result updateCar(@RequestBody Car car){
        carService.updateById(car);
        return Result.success();
    }

    @PostMapping("/deleteCar")
    @ApiOperation("删除车辆信息")
    @Login
    public Result deleteCar(Long carId){
        carService.removeById(carId);
        return Result.success();
    }

    @GetMapping("/selectCarList")
    @ApiOperation("查询车辆信息")
    @Login
    public Result selectCarList(Integer page,Integer limit,@RequestAttribute("userId") Long userId){
        return carService.selectCarList(page, limit, userId, null, null);
    }

    @GetMapping("/selectCarDetails")
    @ApiOperation("查询车辆详细信息")
    @Login
    public Result selectCarDetails(Long carId){
        return Result.success().put("data",carService.getById(carId));
    }

    /**
     * 储存车辆品牌
     */
    private static List<Map<String,Object>> carList;


    @Login
    @GetMapping("/selectCarApi")
    @ApiOperation("查询汽车品牌和车系")
    public Result selectCarApi(){
        if(carList!=null){
            return Result.success().put("data",carList);
        }
        String url="http://apis.juhe.cn/cxdq/brand";
        Map<String,String> param=new HashMap<>();
        param.put("key",commonInfoService.findOne(1165).getValue());
        String data = HttpClientUtil.doGet(url, param);
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONArray result = jsonObject.getJSONArray("result");
        List<Map<String,Object>> carList=new ArrayList<>();
        Map<String,Object> map=new HashMap<>();
        JSONArray jsonArray=new JSONArray();
        String oldL="A";
        for(int i=0;i<result.size();i++){
            JSONObject jsonObject1 = result.getJSONObject(i);
            String l = jsonObject1.getString("first_letter");
            if(oldL.equals(l)){
                jsonArray.add(jsonObject1);
            }else{
                map.put("letter",oldL);
                map.put("carList",jsonArray);
                oldL=l;
                carList.add(map);
                map=new HashMap<>();
                jsonArray=new JSONArray();
                jsonArray.add(jsonObject1);
            }
        }
        map.put("letter",oldL);
        map.put("carList",jsonArray);
        carList.add(map);
        AppCarController.carList=carList;
        return Result.success().put("data",carList);

    }


}
