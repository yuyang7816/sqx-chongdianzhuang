package com.sqx.modules.app.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.Risk;


public interface RiskService extends IService<Risk> {

    Result selectRiskList(Integer page,Integer limit, String userName, String phone, Integer riskType);

    Result selectRiskGroupList(Integer page,Integer limit, String userName, String phone, Integer riskSort);

}
