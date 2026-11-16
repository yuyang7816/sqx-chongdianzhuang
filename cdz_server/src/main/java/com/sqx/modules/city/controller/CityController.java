package com.sqx.modules.city.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.city.entity.City;
import com.sqx.modules.city.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "城市", tags = {"城市"})
@RequestMapping(value = "/city")
public class CityController {

    @Autowired
    private CityService cityService;

    @PostMapping("/insertCity")
    @ApiOperation("添加城市")
    public Result insertCity(@RequestBody City city){
        city.setCreateTime(DateUtils.format(new Date()));
        cityService.save(city);
        return Result.success();
    }

    @PostMapping("/updateCity")
    @ApiOperation("修改城市")
    public Result updateCity(@RequestBody City city){
        city.setCreateTime(DateUtils.format(new Date()));
        cityService.updateById(city);
        return Result.success();
    }

    @PostMapping("/deleteCity")
    @ApiOperation("删除城市")
    public Result deleteCity(Long cityId){
        cityService.removeById(cityId);
        return Result.success();
    }

    @GetMapping("/selectCityList")
    @ApiOperation("查询城市列表")
    public Result selectCityList(Integer page,Integer limit,Integer status,String city){
        if(page==null || limit==null){
            return Result.success().put("data",cityService.list(
                    new QueryWrapper<City>()
                            .eq(status!=null,"status",status)
                            .eq(StringUtils.isNotEmpty(city),"city",city)));
        }
        return Result.success().put("data",new PageUtils(cityService.page(new Page<>(page,limit),
                new QueryWrapper<City>().eq(status!=null,"status",status).eq(StringUtils.isNotEmpty(city),"city",city))));
    }


}