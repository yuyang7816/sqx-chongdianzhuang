package com.sqx.modules.city.controller.app;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.city.entity.City;
import com.sqx.modules.city.service.CityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author fang
 * @date 2020/7/9
 */
@Slf4j
@RestController
@Api(value = "城市", tags = {"城市"})
@RequestMapping(value = "/app/city")
public class AppCityController {

    @Autowired
    private CityService cityService;

    @GetMapping("/selectCityList")
    @ApiOperation("查询城市列表")
    public Result selectCityList(Integer status,String city){
        return Result.success().put("data",cityService.list(
                new QueryWrapper<City>().eq(status!=null,"status",status).eq(StringUtils.isNotEmpty(city),"city",city)));
    }


}