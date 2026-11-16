package com.sqx.modules.apply.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.Result;
import com.sqx.modules.apply.entity.Apply;

public interface ApplyService extends IService<Apply> {

    Result insertApply(Apply apply);

    Result updateApply(Apply apply);

    Result deleteApply(Long applyId);

    Result selectApplyList(Integer page,Integer limit,String shopName,String applyName,
                                  String applyPhone,Integer status,String shopAddress,String shopAddressDetails);

    Apply selectApplyByUserIdAndClassify(Long userId);

    Result auditApply(String ids,Integer status,String content);


}
