package com.sqx.modules.app.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.dto.SearchUserDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 用户
 */
@Mapper
public interface UserDao extends BaseMapper<UserEntity> {


    IPage<UserEntity> selectUserPage(Page<UserEntity> page,@Param("search") String search,
                                     @Param("sex") Integer sex,@Param("platform") String platform,
                                     @Param("sysPhone")String sysPhone,@Param("status") Integer status,
                                     @Param("member")Integer member,@Param("startTime")String startTime,
                                     @Param("endTime")String endTime,@Param("userName")String userName,
                                     @Param("inviterCode")String inviterCode,@Param("isAuthentication") Integer isAuthentication,
                                     @Param("flag") Integer flag,@Param("date") String date,@Param("tearoomId") Long tearoomId,
                                     @Param("isRate") Integer isRate,@Param("isVip") Integer isVip,@Param("vipIndex") Integer vipIndex,
                                     @Param("moneyUser") Integer moneyUser,@Param("vipNo") String vipNo,
                                     @Param("isBirthday") Integer isBirthday,@Param("birthdayMonth") String birthdayMonth,
                                     @Param("integralNum") Integer integralNum,
                                     @Param("ordersStartTime") String ordersStartTime,@Param("ordersEndTime") String ordersEndTime,
                                     @Param("userVipDetails") Integer userVipDetails,@Param("tearoomUser") Long tearoomUser);

    int queryInviterCount(@Param("inviterCode") String inviterCode);

    int queryBirthdayCount(@Param("birthdayMonth") String birthdayMonth);

    int queryUserCount(@Param("type") int type, @Param("date") String date);

    int queryUserByPhoneCount(@Param("type") int type, @Param("date") String date);

    int queryUserByNullPhoneCount(@Param("type") int type, @Param("date") String date);

    int queryUserTearoomCount(@Param("type") int type, @Param("date") String date);

    Double queryPayMoney(@Param("type") int type, @Param("date") String date);

    IPage<Map<String, Object>> queryCourseOrder(Page iPage, @Param("type") int type, @Param("date") String date);

    int userMessage(String date, int type);

    int insertUser(UserEntity userEntity);

    IPage<Map<String, Object>> takingOrdersMessage(Page<Map<String, Object>> iPage, @Param("type") Long type, @Param("date") String date);

    UserEntity selectUserByUserId(@Param("userId") Long userId,@Param("byUserId") Long byUserId);

    int updateUserConsortiaIsNullByUserId(Long userId);

    int updateUserConsortiaIsNullByConsortiaId(Long consortiaId);

    int updateUserClientIdIsNull(String clientid);

    IPage<UserEntity> selectUserByOrdersCount(Page<UserEntity> page);

    int userPlatform(String platform,String date,Integer type);

    int userVipCount(Integer vipIndex,String date,Integer type);

    IPage<UserEntity> selectUserByGood(Page<UserEntity> page,Long userId);

    IPage<UserEntity> selectUserByActivityUserId(Page<UserEntity> page,Long userId);

    IPage<Map<String,Object>> selectUserVipExp(Page<Map<String,Object>> page,Long userId,String userName,String phone,
                                               String startTime,String endTime,String ordersNo);

    Double selectMoneyUserVipExp(Long userId,String startTime,String endTime);

    List<SearchUserDTO> queryByContent(@Param("content")String content);

}
