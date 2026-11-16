package com.sqx.modules.apply.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.apply.dao.ApplyDao;
import com.sqx.modules.apply.entity.Apply;
import com.sqx.modules.apply.service.ApplyService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 申请
 */
@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyDao, Apply> implements ApplyService {

    @Autowired
    private UserService userService;

    @Override
    public Result insertApply(Apply apply){
        Apply oldApply = selectApplyByUserIdAndClassify(apply.getUserId());
        if(oldApply!=null){
            return Result.error("您已经提交过了！");
        }
        apply.setStatus(1);
        apply.setCreateTime(DateUtils.format(new Date()));
        baseMapper.insert(apply);
        return Result.success();
    }

    @Override
    public Result updateApply(Apply apply){
        apply.setStatus(1);
        baseMapper.updateById(apply);
        return Result.success();
    }

    @Override
    public Result deleteApply(Long applyId){
        baseMapper.deleteById(applyId);
        return Result.success();
    }


    @Override
    public Result selectApplyList(Integer page,Integer limit,String shopName,String applyName,
                                  String applyPhone,Integer status,String shopAddress,String shopAddressDetails){
        IPage<Apply> applyPage = baseMapper.selectPage(new Page<>(page, limit),
                new QueryWrapper<Apply>()
                        .like(StringUtils.isNotBlank(shopName), "shop_name", shopName)
                        .like(StringUtils.isNotBlank(applyName), "apply_name", applyName)
                        .like(StringUtils.isNotBlank(applyPhone), "apply_phone", applyPhone)
                        .like(StringUtils.isNotBlank(shopAddress), "shop_address", shopAddress)
                        .like(StringUtils.isNotBlank(shopAddressDetails), "shop_address_details", shopAddressDetails)
                        .eq(status != null && status != 0, "status", status)
                        .orderByDesc("create_time"));
        List<Apply> records = applyPage.getRecords();
        for(Apply apply:records){
            apply.setUser(userService.getById(apply.getUserId()));
        }
        return Result.success().put("data",new PageUtils(applyPage));
    }

    @Override
    public Apply selectApplyByUserIdAndClassify(Long userId){
        return baseMapper.selectOne(new QueryWrapper<Apply>().eq("user_id",userId));
    }

    @Override
    public Result auditApply(String ids,Integer status,String content){
        for(String id:ids.split(",")){
            Apply apply = baseMapper.selectById(Long.parseLong(id));
            UserEntity user = userService.getById(apply.getUserId());
            if(apply.getStatus().equals(1)){
                if(status==1){
                    apply.setStatus(2);
                    apply.setAuditContent("通过");
                    user.setIsShop(1);
                    userService.updateById(user);
                }else{
                    apply.setStatus(3);
                    apply.setAuditContent(content);
                }
                baseMapper.updateById(apply);
            }
        }
        return Result.success();
    }




}
