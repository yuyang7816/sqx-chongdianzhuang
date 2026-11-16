package com.sqx.modules.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.coupon.dao.CouponCardDao;
import com.sqx.modules.coupon.entity.Coupon;
import com.sqx.modules.coupon.entity.CouponCard;
import com.sqx.modules.coupon.entity.CouponCardType;
import com.sqx.modules.coupon.entity.CouponUser;
import com.sqx.modules.coupon.service.CouponCardService;
import com.sqx.modules.coupon.service.CouponCardTypeService;
import com.sqx.modules.coupon.service.CouponService;
import com.sqx.modules.coupon.service.CouponUserService;
import com.sqx.modules.utils.excel.ExcelData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Service
public class CouponCardServiceImpl extends ServiceImpl<CouponCardDao, CouponCard> implements CouponCardService {

    @Autowired
    private UserService userService;
    @Autowired
    private CouponCardTypeService couponCardTypeService;
    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponUserService couponUserService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);

    @Async
    @Override
    public void insertCouponCard(Integer num, Long couponCardTypeId){
        String createTime = DateUtils.format(new Date());
        for(int i=0;i<num;i++){
            String couponCardNo = randomCardNo();
            CouponCard couponCard=new CouponCard();
            couponCard.setCouponCardNo(couponCardNo);
            couponCard.setCouponCardTypeId(couponCardTypeId);
            couponCard.setStatus(1);
            couponCard.setCreateTime(createTime);
            baseMapper.insert(couponCard);
        }
    }

    @Override
    public CouponCard selectCouponCard(String couponCardNo){
        return baseMapper.selectOne(new QueryWrapper<CouponCard>().eq(StringUtils.isNotBlank(couponCardNo),"coupon_card_no",couponCardNo));
    }

    @Override
    public Result selectCouponCardList(Integer page,Integer limit,Integer status,String couponCardNo,Long couponCardTypeId,Long userId){
        IPage<CouponCard> couponCardPage = baseMapper.selectPage(new Page<>(page, limit), new QueryWrapper<CouponCard>()
                .eq(status!=null && status != 0, "status", status)
                .eq(StringUtils.isNotBlank(couponCardNo), "coupon_card_no", couponCardNo)
                .eq(userId!=null && userId!=0,"user_id",userId)
                .eq(couponCardTypeId!=null && couponCardTypeId != 0, "coupon_card_type_id", couponCardTypeId));
        List<CouponCard> records = couponCardPage.getRecords();
        for(CouponCard couponCard:records){
            if(couponCard.getUserId()!=null){
                couponCard.setUser(userService.selectUserById(couponCard.getUserId()));
            }
        }
        return Result.success().put("data",new PageUtils(couponCardPage));
    }

    @Override
    public ExcelData excelCouponCardList(Integer status, String couponCardNo, Long couponCardTypeId, Long userId){
        List<CouponCard> couponCardList = baseMapper.selectList( new QueryWrapper<CouponCard>()
                .eq(status!=null && status != 0, "status", status)
                .eq(StringUtils.isNotBlank(couponCardNo), "coupon_card_no", couponCardNo)
                .eq(userId!=null && userId!=0,"user_id",userId)
                .eq(couponCardTypeId!=null && couponCardTypeId != 0, "coupon_card_type_id", couponCardTypeId));

        ExcelData data = new ExcelData();
        data.setName("提现列表");
        List<String> titles = new ArrayList();
        titles.add("编号");titles.add("卡密");titles.add("领取用户");
        titles.add("状态");titles.add("创建时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for(CouponCard couponCard:couponCardList){
            List<Object> row = new ArrayList();
            row.add(couponCard.getCouponCardId());
            row.add(couponCard.getCouponCardNo());
            if(couponCard.getUserId()!=null){
                UserEntity userEntity = userService.selectUserById(couponCard.getUserId());
                if(userEntity!=null){
                    row.add(userEntity.getUserName());
                }else{
                    row.add("用户已被删除");
                }
            }else{
                row.add("无");
            }
            if(couponCard.getStatus()==1){
                row.add("待领取");
            }else{
                row.add("已领取");
            }
            row.add(couponCard.getCreateTime());
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }

    @Override
    public Result activateCouponCard(Long userId,String couponCardNo){
        reentrantReadWriteLock.writeLock().lock();
        try{
            CouponCard couponCard = selectCouponCard(couponCardNo);
            if(couponCard==null){
                return Result.error("卡密错误！");
            }else if(couponCard.getStatus().equals(2)){
                return Result.error("卡密已被使用！");
            }
            CouponCardType couponCardType = couponCardTypeService.getById(couponCard.getCouponCardTypeId());
            if(couponCardType.getStatus().equals(2)){
                return Result.error("卡密尚未开放！");
            }
            couponCard.setUserId(userId);
            couponCard.setStatus(2);
            baseMapper.updateById(couponCard);
            String[] couponIds = couponCardType.getCouponIds().split(",");
            String createTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Coupon> couponList=new ArrayList<>();
            for(String couponId:couponIds){
                Coupon coupon = couponService.getById(Long.parseLong(couponId));
                Integer endDate = coupon.getEndDate();
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, endDate);
                calendar.add(Calendar.DATE, 1);
                Date date = calendar.getTime();
                String expirationTime = simpleDateFormat.format(date);
                CouponUser couponUser=new CouponUser();
                couponUser.setUserId(userId);
                couponUser.setCouponName(coupon.getCouponName());
                couponUser.setCouponPicture(coupon.getCouponPicture());
                couponUser.setCreateTime(createTime);
                couponUser.setExpirationTime(expirationTime);
                couponUser.setMinMoney(coupon.getMinMoney());
                couponUser.setMoney(coupon.getMoney());
                couponUser.setStatus(0);
                couponUserService.save(couponUser);
                couponList.add(coupon);
            }
            return Result.success().put("data",couponList);
        }catch (Exception e){
            log.error("激活优惠券异常："+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.error("系统繁忙，请稍后再试！");
    }




    public String randomCardNo(){
        Random random=new Random();
        while (true){
            StringBuilder str=new StringBuilder();//定义变长字符串
            //随机生成数字，并添加到字符串
            for(int j=0;j<8;j++){
                str.append(random.nextInt(10));
            }
            //将字符串转换为数字并输出
            String couponCardNo=str.toString();
            CouponCard couponCard = selectCouponCard(couponCardNo);
            if(couponCard==null){
                return couponCardNo;
            }
        }
    }


}
