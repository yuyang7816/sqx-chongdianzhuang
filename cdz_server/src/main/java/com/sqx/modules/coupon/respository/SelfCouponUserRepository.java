package com.sqx.modules.coupon.respository;


import com.sqx.modules.coupon.entity.SelfCouponUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface SelfCouponUserRepository extends Repository<SelfCouponUser, Long> {

    @Query(value = "select count(*) from self_coupon_user where `type`=:type and user_id=:userId and status=1",nativeQuery = true)
    Integer selectSelfCouponUserCount(@Param("type") Integer type,@Param("userId") Long userId);

    @Query(value = "select count(*) from self_coupon_user where user_id=:userId and status=1",nativeQuery = true)
    Integer selectSelfCouponUserCount(@Param("userId") Long userId);

    @Query(value = "select count(*) from self_coupon_user where user_id=:userId and platform=2 ",nativeQuery = true)
    Integer selectInviteCouponUserCount(@Param("userId") Long userId);

    @Query(value = "select count(*) from self_coupon_user where user_id=:userId and platform=3 ",nativeQuery = true)
    Integer selectInviteCouponUserCount3(@Param("userId") Long userId);

    @Query(value = "select count(*) from self_coupon_user where platform=:platform and create_time like concat('%',:time,'%') ",nativeQuery = true)
    Integer selectCouponCountByTimeAndPlatform(@Param("time") String time,@Param("platform") Integer platform);


    @Query(value = "select count(*) from self_coupon_user where platform_type=:platformType and create_time like concat('%',:time,'%') " +
            "and if(:tearoomId!='',shop_tearoom_id=:tearoomId,1=1) ",nativeQuery = true)
    Integer selectCouponCountByTimeAndPlatformType(@Param("time") String time,@Param("platformType") Integer platformType,@Param("tearoomId") String tearoomId);

    @Query(value = "select ifnull(sum(less_money),0) from self_coupon_user where platform_type=:platformType and create_time like concat('%',:time,'%') " +
            "and if(:tearoomId!='',shop_tearoom_id=:tearoomId,1=1) ",nativeQuery = true)
    BigDecimal selectCouponMoneyByTimeAndPlatformType(@Param("time") String time, @Param("platformType") Integer platformType,@Param("tearoomId") String tearoomId);

    @Query(value = "select ifnull(sum(less_money),0) from self_coupon_user where platform_type=:platformType ",nativeQuery = true)
    BigDecimal selectCouponMoneyByPlatformType(@Param("platformType") Integer platformType);

    @Query(value = "select count(*) from self_coupon_user where platform in (1,2,3,6) and create_time like concat('%',:time,'%') " +
            "and if(:tearoomId!='',shop_tearoom_id=:tearoomId,1=1) and status=2 ",nativeQuery = true)
    Integer selectCouponCountByTimeAndPlatforms(@Param("time") String time,@Param("tearoomId") String tearoomId);

    @Query(value = "select ifnull(sum(less_money),0) from self_coupon_user where platform in (1,2,3,6) and create_time like concat('%',:time,'%') " +
            "and if(:tearoomId!='',shop_tearoom_id=:tearoomId,1=1) and status=2 ",nativeQuery = true)
    BigDecimal selectCouponMoneyByTimeAndPlatforms(@Param("time") String time, @Param("tearoomId") String tearoomId);

    @Query(value = "select count(*) from self_coupon_user where platform=4 and create_time like concat('%',:time,'%') " +
            "and if(:tearoomId!='',shop_tearoom_id=:tearoomId,1=1) ",nativeQuery = true)
    Integer selectCouponCountByTimeAndPlatformsPay(@Param("time") String time,@Param("tearoomId") String tearoomId);

    @Query(value = "select ifnull(sum(less_money),0) from self_coupon_user where platform=4 and create_time like concat('%',:time,'%') " +
            "and if(:tearoomId!='',shop_tearoom_id=:tearoomId,1=1) ",nativeQuery = true)
    BigDecimal selectCouponMoneyByTimeAndPlatformsPay(@Param("time") String time, @Param("tearoomId") String tearoomId);

    @Query(value = "select ifnull(sum(less_money),0) from self_coupon_user where create_time like concat('%',:time,'%') " +
            "and status = 2 and platform_type=:platformType and tearoom_id=:tearoomId",nativeQuery = true)
    Double selectCouponMoneyByTimeAndPlatformType(@Param("time") String time,@Param("platformType") Integer platformType,@Param("tearoomId") Long tearoomId);

    @Query(value = "select count(*) from self_coupon_user where create_time like concat('%',:time,'%') " +
            "and status = 2 and platform_type=:platformType and tearoom_id=:tearoomId",nativeQuery = true)
    Integer selectCouponCountByTimeAndPlatformType(@Param("time") String time,@Param("platformType") Integer platformType,@Param("tearoomId") Long tearoomId);

    @Query(value = "select ifnull(sum(less_money),0) from self_coupon_user where 1=1 " +
            "and status = 2 and platform_type=:platformType and tearoom_id=:tearoomId",nativeQuery = true)
    Double selectCouponMoneyByTimeAndPlatformType2(@Param("platformType") Integer platformType,@Param("tearoomId") Long tearoomId);


}