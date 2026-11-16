package com.sqx.modules.message.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Query;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.coupon.entity.SelfCouponUser;
import com.sqx.modules.coupon.respository.SelfCouponUserJpaRepository;
import com.sqx.modules.common.entity.CommonInfo;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.message.dao.MessageInfoDao;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import com.sqx.modules.utils.HttpClientUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息
 */
@Service
public class MessageServiceImpl extends
        ServiceImpl<MessageInfoDao, MessageInfo> implements MessageService {

    @Autowired
    private MessageInfoDao messageInfoDao;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonInfoService commonInfoService;
    @Autowired
    private SelfCouponUserJpaRepository selfCouponUserJpaRepository;


    @Override
    public Result auditMessage(Long messageId,Integer status,String auditContent){
        MessageInfo messageInfo = baseMapper.selectById(messageId);
        if("0".equals(messageInfo.getType())){
            messageInfo.setAuditContent(auditContent);
            messageInfo.setType(String.valueOf(status));
            baseMapper.updateById(messageInfo);

        }else if(StringUtils.isEmpty(messageInfo.getType())){
            messageInfo.setAuditContent(auditContent);
            messageInfo.setType(String.valueOf(status));
            baseMapper.updateById(messageInfo);
        }
        return Result.success();
    }

    @Override
    public PageUtils selectMessageLists(Map<String,Object> params){
        Long userId = (Long)params.get("userId");
        Integer state = (Integer)params.get("state");
        Integer type = (Integer)params.get("type");
        Object city = params.get("city");
        IPage<MessageInfo> page = this.page(
                new Query<MessageInfo>().getPage(params),
                new QueryWrapper<MessageInfo>()
                        .eq(userId!=null,"user_id", userId)
                        .eq(state!=null,"state", state)
                        .eq(type!=null,"type", type)
                        .eq("is_see", "0")
                        .last(state!=null && state==7 && city!=null," and by_user_id in (select user_id from artificer where city like concat('%','"+city+"','%')) order by create_at desc  ")
                        .last(state!=null && state==9 && city!=null," and user_id in (select user_id from artificer where city like concat('%','"+city+"','%')) order by create_at desc  ")
                        .orderByDesc(city==null,"create_at")
        );
        return new PageUtils(page);
    }

    @Override
    public PageUtils selectMessageList(Map<String,Object> params){
        Long userId = (Long)params.get("userId");
        Integer state = (Integer)params.get("state");
        Object type = params.get("type");
        Integer platform = (Integer)params.get("platform");
        Integer status = (Integer)params.get("status");
        Object tearoomId = params.get("tearoomId");
        Object userName = params.get("userName");
        Object phone = params.get("phone");
        Object content = params.get("content");

        IPage<MessageInfo> page = this.page(
                new Query<MessageInfo>().getPage(params),
                new QueryWrapper<MessageInfo>()
                        .eq(userId!=null,"user_id", userId)
                        .eq(state!=null,"state", state)
                        .and(state==null,Wrapper -> Wrapper.eq("state", "4").or().eq("state ","5"))
                        .eq(type!=null && !"".equals(type),"type", type)
                        .eq(status!=null,"status", status)
                        .eq(tearoomId!=null,"platform_id", tearoomId)
                        .like(userName!=null,"user_name", userName)
                        .like(phone!=null,"phone", phone)
                        .like(content!=null,"content", content)
                        .eq(platform!=null,"platform", platform).orderByDesc("create_at")
        );
        List<MessageInfo> records = page.getRecords();
        if(records.size()>0){
            for(MessageInfo messageInfo:records){
                if(messageInfo.getUserId()!=null){
                    UserEntity user = userService.getById(messageInfo.getUserId());
                    if(user!=null){
                        messageInfo.setUserName(user.getUserName());
                    }
                    messageInfo.setUserEntity(user);
                }
                if(messageInfo.getByUserId()!=null){
                    UserEntity user = userService.getById(messageInfo.getByUserId());
                    if(user!=null){
                        messageInfo.setByUserName(user.getUserName());
                    }
                }
                if("400".equals(messageInfo.getState())){
                    if(messageInfo.getPlatformId()!=null){
                        SelfCouponUser selfCouponUser = selfCouponUserJpaRepository.findById(messageInfo.getPlatformId()).orElse(null);
                        messageInfo.setSelfCouponUser(selfCouponUser);
                    }
                }
            }
        }

        return new PageUtils(page);
    }

    @Override
    public int saveBody(MessageInfo messageInfo){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        messageInfo.setStatus(0);
        messageInfo.setCreateAt(sdf.format(now));
        /*if("3".equals(messageInfo.getState())){
            UserEntity userEntity = userService.selectUserById(Long.parseLong(messageInfo.getUserId()));
            userEntity.setStatus(2);
            userService.updateById(userEntity);
        }*/
        return messageInfoDao.insert(messageInfo);
    }

    @Override
    public int update(MessageInfo messageInfo){
        return messageInfoDao.updateById(messageInfo);
    }

    @Override
    public int delete(Long id){
        return messageInfoDao.deleteById(id);
    }

    @Override
    public MessageInfo selectMessageById(Long id){
        return messageInfoDao.selectById(id);
    }

    @Override
    public int updateSendState(Long userId, Integer state) {
        return messageInfoDao.updateSendState(userId, state);
    }




}
