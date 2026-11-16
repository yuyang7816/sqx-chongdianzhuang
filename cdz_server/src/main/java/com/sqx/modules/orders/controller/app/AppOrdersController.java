package com.sqx.modules.orders.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.entity.UserMoney;
import com.sqx.modules.app.entity.UserMoneyDetails;
import com.sqx.modules.app.service.UserMoneyDetailsService;
import com.sqx.modules.app.service.UserMoneyService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.entity.Tearoom;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.tearoom.service.TearoomService;
import com.sqx.modules.utils.excel.ExcelData;
import com.sqx.modules.utils.excel.ExportExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@RestController
@Api(value = "订单信息", tags = {"订单信息"})
@RequestMapping(value = "/app/orders")
public class AppOrdersController extends AbstractController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private TearoomService tearoomService;
    @Autowired
    private UserMoneyService userMoneyService;
    @Autowired
    private UserMoneyDetailsService userMoneyDetailsService;


    @Login
    @GetMapping("/selectOrdersMoney")
    @ApiOperation("查询订单预计金额")
    public Result selectOrdersMoney(@RequestAttribute Long userId,Long goodsId,Integer hour){
        return ordersService.selectOrdersMoney(userId, goodsId, hour);
    }

    @Login
    @PostMapping("/insertOrders")
    @ApiOperation("生成订单")
    public Result insertOrders(@RequestAttribute Long userId,@RequestBody Orders orders){
        orders.setUserId(userId);
        return ordersService.insertOrders(orders);
    }

    @Login
    @PostMapping("/payOrders")
    @ApiOperation("支付订单")
    public Result payOrders(String ordersNo){
        return ordersService.payOrders(ordersNo);
    }

    @Login
    @GetMapping("/selectOrdersByUserId")
    @ApiOperation("查询自己正在进行的订单")
    public Result selectOrdersByUserId(@RequestAttribute Long userId){
        return ordersService.selectOrdersByUserId(userId);
    }

    @Login
    @GetMapping("/selectOrdersList")
    @ApiOperation("查询订单列表")
    public Result selectOrdersList(Integer page,Integer limit,@RequestAttribute Long userId,String userName,String phone,
                                   String ordersNo,String goodsName,Integer status,Long tearoomId,String startTime,String endTime,
                                   Integer isShop,String dateTime,String cameraId,Integer goodsCategory,String tearoomName,
                                   Integer platformType,Long openUserId,Integer ordersType){
        return ordersService.selectOrdersList(page, limit, userId, userName, phone, ordersNo, goodsName,
                status,tearoomId,startTime,endTime,1,null,null,isShop,dateTime,cameraId,
                goodsCategory,tearoomName,platformType,openUserId,ordersType);
    }

    @Login
    @GetMapping("/selectOrdersLists")
    @ApiOperation("查询订单列表")
    public Result selectOrdersLists(Integer page,Integer limit,Long userId,String userName,String phone,
                                   String ordersNo,String goodsName,Integer status,Long tearoomId,String startTime,String endTime,
                                    Integer isShop,String dateTime,String cameraId,Integer goodsCategory,String tearoomName,
                                    Integer platformType,Long openUserId,Integer ordersType){
        return ordersService.selectOrdersList(page, limit, userId, userName, phone, ordersNo, goodsName,
                status,tearoomId,startTime,endTime,1,null,null,isShop,dateTime,
                cameraId,goodsCategory,tearoomName,platformType,openUserId,ordersType);
    }

    @Login
    @PostMapping("/cancelOrders")
    @ApiOperation("取消订单")
    public Result cancelOrders(String ordersNo){
        return ordersService.cancelOrders(ordersNo);
    }

    @Login
    @GetMapping("/selectOrdersDetails")
    @ApiOperation("查询订单详情")
    public Result selectOrdersDetails(String ordersNo){
        return Result.success().put("data",ordersService.selectOrdersByNo(ordersNo));
    }

    @Login
    @GetMapping("/selectOrdersDetailsVip")
    @ApiOperation("查询订单详情会员")
    public Result selectOrdersDetailsVip(String ordersNo){
        return Result.success().put("data",ordersService.selectOrdersDetailsVip(ordersNo));
    }

    @Login
    @PostMapping("/removeOrders")
    @ApiOperation("删除订单")
    public Result removeOrders(Long ordersId){
        return ordersService.removeOrders(ordersId);
    }

    @Login
    @GetMapping("/selectTeamUserList")
    @ApiOperation("获取团队列表")
    public Result selectTeamUserList(@LoginUser UserEntity user, Integer page, Integer limit, Integer type){
        return ordersService.selectTeamUserList(page,limit,user.getInvitationCode(),type,user.getUserId());
    }

    @Login
    @GetMapping("/selectTeamStatistics")
    @ApiOperation("团队统计")
    public Result selectTeamStatistics(@LoginUser UserEntity user){
        Double oneTeamMoney = ordersService.selectOrdersMoneyCountByUserId(user.getUserId(), 1);
        Integer oneTeamCount = ordersService.selectUserCountByInvitationCode(user.getInvitationCode(), 1);
        Double twoTeamMoney = ordersService.selectOrdersMoneyCountByUserId(user.getUserId(), 2);
        Integer twoTeamCount = ordersService.selectUserCountByInvitationCode(user.getInvitationCode(), 2);
        Integer teamCount=oneTeamCount+twoTeamCount;
        BigDecimal teamMoney=BigDecimal.valueOf(oneTeamMoney).add(BigDecimal.valueOf(twoTeamMoney));
        Map<String,Object> result=new HashMap<>();
        result.put("teamMoney",teamMoney);
        result.put("teamCount",teamCount);
        result.put("oneTeamMoney",oneTeamMoney);
        result.put("oneTeamCount",oneTeamCount);
        result.put("twoTeamMoney",twoTeamMoney);
        result.put("twoTeamCount",twoTeamCount);
        return Result.success().put("data",result);
    }


    @Login
    @PostMapping("/updateOrders")
    @ApiOperation("修改订单信息")
    public Result updateOrders(@RequestBody Orders orders){
        if(orders.getStatus()!=null && orders.getStatus()==4){
            Orders orders1 = ordersService.getById(orders.getOrdersId());
            orders.setStatus(4);
            Tearoom tearoom = tearoomService.getById(orders.getTearoomId());
            if(tearoom.getUserId()!=null){
                //计算商家收入 需要计算用户补钱和支付的钱
                BigDecimal shopMoney = orders.getPayMoney().multiply(tearoom.getRate()).setScale(2, RoundingMode.DOWN);
                userMoneyService.updateShopMoney(1,tearoom.getUserId(),shopMoney.doubleValue());
                UserMoneyDetails userMoneyDetails=new UserMoneyDetails();
                userMoneyDetails.setMoney(shopMoney);
                userMoneyDetails.setShopUserId(tearoom.getUserId());
                userMoneyDetails.setContent("到账收益："+shopMoney);
                userMoneyDetails.setTitle("订单收益："+orders.getOrdersNo());
                userMoneyDetails.setType(1);
                userMoneyDetails.setClassify(5);
                userMoneyDetails.setCreateTime(DateUtils.format(new Date()));
                userMoneyDetailsService.save(userMoneyDetails);
                orders.setShopMoney(shopMoney);
            }
        }
        ordersService.updateById(orders);
        return Result.success();
    }

    @Login
    @PostMapping("/roomChange")
    @ApiOperation("更换房间")
    public Result roomChange(Long ordersId,Long goodsId){
        return ordersService.roomChange(ordersId, goodsId);
    }

    @Login
    @PostMapping("/closeOrders")
    @ApiOperation("结束订单")
    public Result closeOrders(Long ordersId){
        return ordersService.closeOrders(ordersId);
    }

    @Login
    @PostMapping("/refundOrdersMoney")
    @ApiOperation("退款订单")
    public Result refundOrdersMoney(String ordersNo,BigDecimal refundMoney,String refundRemark){
        return ordersService.refundOrdersMoney(ordersNo,refundMoney,refundRemark);
    }

    @Login
    @GetMapping("/selectOrdersMoneyByTearoomId")
    @ApiOperation("查询店铺总收益")
    public Result selectOrdersMoneyByTearoomId(Long tearoomId){
        Double sumMoney = ordersService.selectOrdersMoneyByTearoom(null, null, tearoomId,null);
        Integer sumCount = ordersService.selectOrdersCountByTearoom(null, null, tearoomId,null);
        int goodsCount = goodsService.count(new QueryWrapper<Goods>().eq("tearoom_id", tearoomId));
        Tearoom tearoom = tearoomService.getById(tearoomId);
        UserMoney userMoney = userMoneyService.selectUserMoneyByUserId(tearoom.getUserId());
        Double nowMoney = ordersService.selectOrdersMoneyByTearoomId(1, DateUtils.format(new Date()), tearoomId,null,null,null);
        Map<String,Object> result=new HashMap<>();
        result.put("nowMoney", nowMoney==null?0.00:nowMoney);
        result.put("sumShopMoney", userMoney.getSumShopMoney());
        result.put("shopMoney", userMoney.getShopMoney());
        result.put("sumMoney", sumMoney);
        result.put("sumCount", sumCount);
        result.put("goodsCount", goodsCount);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectOrdersStatusListByTearoomId")
    @ApiOperation("查询订单的状态数量")
    public Result selectOrdersStatusListByTearoomId(Long tearoomId){
        //状态 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价
        int dzfCount = ordersService.count(new QueryWrapper<Orders>().eq("tearoom_id", tearoomId).eq("status", 1));
        int dsyCount = ordersService.count(new QueryWrapper<Orders>().eq("tearoom_id", tearoomId).eq("status", 2));
        int syzCount = ordersService.count(new QueryWrapper<Orders>().eq("tearoom_id", tearoomId).eq("status", 3));
        int ywcCount = ordersService.count(new QueryWrapper<Orders>().eq("tearoom_id", tearoomId).in("status", 4,6));
        Map<String,Object> result=new HashMap<>();
        result.put("dzfCount", dzfCount);
        result.put("dsyCount", dsyCount);
        result.put("syzCount", syzCount);
        result.put("ywcCount", ywcCount);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectOrdersStatusCountByUserId")
    @ApiOperation("查询订单的状态数量")
    public Result selectOrdersStatusCountByUserId(@RequestAttribute Long userId){
        //状态 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价
        int dzfCount = ordersService.count(new QueryWrapper<Orders>().eq("user_id", userId).eq("status", 1));
        int dsyCount = ordersService.count(new QueryWrapper<Orders>().eq("user_id", userId).eq("status", 2));
        int syzCount = ordersService.count(new QueryWrapper<Orders>().eq("user_id", userId).eq("status", 3));
        Map<String,Object> result=new HashMap<>();
        result.put("dzfCount", dzfCount);
        result.put("dsyCount", dsyCount);
        result.put("syzCount", syzCount);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectTearoomOrdersByGoodsIdAndTime")
    @ApiOperation("查询包间收益统计")
    public Result selectTearoomOrdersByGoodsIdAndTime(Integer flag,String time,Long tearoomId,Long goodsId,String endTime){
        Double money = ordersService.selectOrdersMoneyByGoods(flag, time,tearoomId, goodsId,null,endTime);
        Integer count = ordersService.selectOrdersCountByGoods(flag, time,tearoomId, goodsId,null,endTime);
        Map<String,Object> result=new HashMap<>();
        result.put("money", money);
        result.put("count", count);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectTearoomOrdersByTearoomIddAndTime")
    @ApiOperation("查询包间收益统计")
    public Result selectTearoomOrdersByTearoomIddAndTime(Integer flag,String time,Long tearoomId,String endTime){
        Double money = ordersService.selectOrdersMoneyByTearoomId(flag, time, tearoomId,null,endTime,1);
        Double sjMoney = ordersService.selectOrdersMoneyByTearoomId(flag, time, tearoomId,null,endTime,2);
        Map<String,Object> result=new HashMap<>();
        result.put("money", money);
        result.put("sjMoney", sjMoney);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectTearoomOrdersByTearoomId")
    @ApiOperation("充电桩的收益")
    public Result selectTearoomOrdersByTearoomId(Long tearoomId){
        //获取充电桩总收益
        BigDecimal sumMoney=BigDecimal.ZERO;
        //待结算收益
        Double djsMoney = ordersService.selectOrdersSumPayMoneyByTearoomId(null, null, tearoomId,null,null,1);
        //已结算收益
        Double yjsMoney = ordersService.selectOrdersSumShopMoneyByTearoomId(null, null, tearoomId,null,null,1);
        //待结算收益
        Double sjDjsMoney = ordersService.selectOrdersSumPayMoneyByTearoomId(null, null, tearoomId,null,null,2);
        //已结算收益
        Double sjYjsMoney = ordersService.selectOrdersSumShopMoneyByTearoomId(null, null, tearoomId,null,null,2);

        sumMoney=BigDecimal.valueOf(djsMoney==null?0.00:djsMoney)
                .add(BigDecimal.valueOf(yjsMoney==null?0.00:yjsMoney)
                        .add(BigDecimal.valueOf(sjDjsMoney==null?0.00:sjDjsMoney)
                                .add(BigDecimal.valueOf(sjYjsMoney==null?0.00:sjYjsMoney))));
        //订单管理
        //待使用
        int dsyCount = ordersService.count(new QueryWrapper<Orders>().eq("tearoom_id",tearoomId).eq("status", 2));
        //使用中
        int syzCount = ordersService.count(new QueryWrapper<Orders>().eq("tearoom_id",tearoomId).eq("status", 3));
        //已完成
        int ywcCount = ordersService.count(new QueryWrapper<Orders>().eq("tearoom_id",tearoomId).eq("status", 4));
        //桌台管理
        int goodsCount = goodsService.count(new QueryWrapper<Goods>().eq("tearoom_id", tearoomId));
        //桌台收益
        Double ztMoney = ordersService.selectOrdersSumShopMoneyByTearoomId(1, DateUtils.format(new Date()), tearoomId,null,null,null);
        Map<String,Object> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("djsMoney",djsMoney);
        result.put("yjsMoney",yjsMoney);
        result.put("sjDjsMoney",sjDjsMoney);
        result.put("sjYjsMoney",sjYjsMoney);
        result.put("dsyCount",dsyCount);
        result.put("syzCount",syzCount);
        result.put("ywcCount",ywcCount);
        result.put("goodsCount",goodsCount);
        result.put("ztMoney",ztMoney);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectTearoomOrdersByTearoomIdAndTime")
    @ApiOperation("充电桩的收益")
    public Result selectTearoomOrdersByTearoomIdAndTime(Long tearoomId,String time,Integer dayType){
        //1天2周3月4年
        Integer flag=0;
        String endTime=null;
        if(dayType==1){
            flag=1;
        }else if(dayType==2){
            flag=4;
            Calendar calendar = Calendar.getInstance();
            Date date = DateUtils.stringToDate(time, DateUtils.DATE_PATTERN);
            calendar.setTime(date);
            // 获取周一
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            time=DateUtils.format(calendar.getTime());
            // 获取周日
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            endTime=DateUtils.format(calendar.getTime());
        }else if(dayType==3){
            flag=2;
        }else{
            flag=3;
        }
        Double money = ordersService.selectOrdersSumShopMoneyByTearoomId(flag, time, tearoomId,null,endTime,null);
        return Result.success().put("data",money);
    }

    @Login
    @GetMapping("/selectTearoomOrdersCountByTearoomIdAndTime")
    @ApiOperation("充电桩的收益")
    public Result selectTearoomOrdersCountByTearoomIdAndTime(Long tearoomId,String time,Integer dayType){
        //1天2周3月4年
        Integer flag=0;
        String endTime=null;
        if(dayType==1){
            flag=1;
        }else if(dayType==2){
            flag=4;
            Calendar calendar = Calendar.getInstance();
            Date date = DateUtils.stringToDate(time, DateUtils.DATE_PATTERN);
            calendar.setTime(date);
            // 获取周一
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            time=DateUtils.format(calendar.getTime());
            // 获取周日
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            endTime=DateUtils.format(calendar.getTime());
        }else if(dayType==3){
            flag=2;
        }else{
            flag=3;
        }
        Double money = ordersService.selectOrdersSumShopMoneyByTearoomId(flag, time, tearoomId,null,endTime,null);
        Integer count = ordersService.selectOrdersShopCountByTearoomId(flag, time, tearoomId,null,endTime);
        Map<String,Object> result=new HashMap<>();
        result.put("money",money);
        result.put("count",count);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectUserByOrdersMoneyList")
    @ApiOperation("消费排行榜")
    public Result selectUserByOrdersMoney(Integer page,Integer limit,Long tearoomId){
        return ordersService.selectUserByOrdersMoneyList(page,limit,tearoomId);
    }

    
    @GetMapping("/selectUserByOrdersHourList")
    @ApiOperation("开台时长排行榜")
    public Result selectUserByOrdersHourList(Integer page,Integer limit,Integer type,String userName,String phone,String city,Long tearoomId,
                                             String startTime,String endTime,String tearoomName){
        Map<String,Object> result=new HashMap<>();
        IPage<Map<String, Object>> mapIPage = ordersService.selectUserByOrdersHourList(page, limit, type, userName, phone, city, tearoomId, startTime, endTime, tearoomName);
        result.put("data",mapIPage);
        result.put("dateTime",DateUtils.format(new Date(),DateUtils.DATE_TIME_PATTERN));
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectUserByOrdersHourByUserId")
    @ApiOperation("我的开台时长")
    public Result selectUserByOrdersHourByUserId(@RequestAttribute Long userId,Integer type,String city,Long tearoomId){
        Map<String, Object> map = ordersService.selectUserByOrdersHourByUserId(userId, type, city, tearoomId);
        return Result.success().put("data",map);
    }

    @Login
    @GetMapping("/selectOrdersHourByTearoomId")
    @ApiOperation("门店开台时长统计")
    public Result selectOrdersHourByTearoomId(Long tearoomId){
        //今日时长
        Integer daySecond=ordersService.selectOrdersHourByTearoomId(tearoomId,1);
        //本月时长
        Integer monthSecond=ordersService.selectOrdersHourByTearoomId(tearoomId,1);
        //总时长
        Integer sumSecond=ordersService.selectOrdersHourByTearoomId(tearoomId,1);
        Map<String,Integer> result=new HashMap<>();
        result.put("daySecond",daySecond);
        result.put("monthSecond",monthSecond);
        result.put("sumSecond",sumSecond);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectOrdersGoodsHourByTearoomId")
    @ApiOperation("门店开台时长统计")
    public Result selectOrdersGoodsHourByTearoomId(Long tearoomId,String startTime,String endTime){
        List<Goods> goodsList = goodsService.list(new QueryWrapper<Goods>().eq("status", 1).eq("tearoom_id", tearoomId));
        List<Map<String,Object>> mapList=new ArrayList<>();
        for (Goods goods:goodsList){
            Map<String,Object> map=new HashMap<>();
            map.put("goodsName",goods.getGoodsName());
            map.put("goodsCategory",goods.getGoodsCategory());
            map.put("goodsCoverImage",goods.getGoodsCoverImage());
            //总时长
            Integer sumSecond = ordersService.selectOrdersGoodsHourByTearoomId(goods.getGoodsId(), startTime, endTime,null);
            //用户开台时长
            Integer userSecond = ordersService.selectOrdersGoodsHourByTearoomId(goods.getGoodsId(), startTime, endTime,1);
            //商家
            Integer shopSecond = ordersService.selectOrdersGoodsHourByTearoomId(goods.getGoodsId(), startTime, endTime,2);
            //总收益
            Double sumMoney = ordersService.selectOrdersGoodsMoneyByTearoomId(goods.getGoodsId(), startTime, endTime,null);
            //用户
            Double userMoney = ordersService.selectOrdersGoodsMoneyByTearoomId(goods.getGoodsId(), startTime, endTime,1);
            //商家
            Double shopMoney = ordersService.selectOrdersGoodsMoneyByTearoomId(goods.getGoodsId(), startTime, endTime,2);
            map.put("sumSecond",sumSecond);
            map.put("userSecond",userSecond);
            map.put("shopSecond",shopSecond);
            map.put("sumMoney",sumMoney);
            map.put("userMoney",userMoney);
            map.put("shopMoney",shopMoney);

            mapList.add(map);
        }
        return Result.success().put("data",mapList);
    }

    @Login
    @GetMapping("/selectOrdersMoneyByTearoomIdAndDate")
    @ApiOperation("账单收益查询")
    public Result selectOrdersMoneyByTearoomIdAndDate(Integer page,Integer limit,Long tearoomId,Integer way){
        List<String> dateTimeList=new ArrayList<>();
        //1日 2月
        if(way==1){
            dateTimeList=DateUtils.getDatesByPage(page,limit);
        }else{
            dateTimeList=DateUtils.getMonthsByPage(page,limit);
        }
        List<Double> ordersMoneyList=new ArrayList<>();
        for (String dateTime:dateTimeList){
            if(way==2){
                Double money = ordersService.selectOrdersMoneyByTearoomId(way, dateTime+"-01", tearoomId, null, null,1);
                ordersMoneyList.add(money==null?0.00:money);
            }else{
                Double money = ordersService.selectOrdersMoneyByTearoomId(way, dateTime, tearoomId, null, null,1);
                ordersMoneyList.add(money==null?0.00:money);
            }
        }
        Map<String,Object> result=new HashMap<>();
        result.put("dateTimeList",dateTimeList);
        result.put("ordersMoneyList",ordersMoneyList);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectShopOpenOrdersCount")
    @ApiOperation("商家开台收益统计")
    public Result selectShopOpenOrdersCount(Long tearoomId,String time,Integer type){
        //商家开台支付方式 1微信 2现金  3门店会员 4美团核销 5快手核销 6抖音核销
        //总收益
        Double sumMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, null, null);
        Integer sumCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, null, null);
        //1微信
        Double wxMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 1, null);
        Integer wxCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 1, null);
        //2现金
        Double xjMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 2, null);
        Integer xjCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 2, null);
        //3门店会员
        Double mdMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 3, null);
        Integer mdCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 3, null);
        //4美团核销
        Double mtMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 4, null);
        Integer mtCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 4, null);
        //5快手核销
        Double ksMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 5, null);
        Integer ksCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 5, null);
        //6抖音核销
        Double dyMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 6, null);
        Integer dyCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 6, null);
        Map<String,Object> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("wxMoney",wxMoney);
        result.put("xjMoney",xjMoney);
        result.put("mdMoney",mdMoney);
        result.put("mtMoney",mtMoney);
        result.put("ksMoney",ksMoney);
        result.put("dyMoney",dyMoney);
        result.put("sumCount",sumCount);
        result.put("wxCount",wxCount);
        result.put("xjCount",xjCount);
        result.put("mdCount",mdCount);
        result.put("mtCount",mtCount);
        result.put("ksCount",ksCount);
        result.put("dyCount",dyCount);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectYgOpenOrdersCount")
    @ApiOperation("员工开台收益统计")
    public Result selectYgOpenOrdersCount(Long userId,Long tearoomId,String time,Integer type){
        //商家开台支付方式 1微信 2现金  3门店会员 4美团核销 5快手核销 6抖音核销
        //总收益
        Double sumMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, null, userId);
        Integer sumCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, null, userId);
        //1微信
        Double wxMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 1, userId);
        Integer wxCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 1, userId);
        //2现金
        Double xjMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 2, userId);
        Integer xjCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 2, userId);
        //3门店会员
        Double mdMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 3, userId);
        Integer mdCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 3, userId);
        //4美团核销
        Double mtMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 4, userId);
        Integer mtCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 4, userId);
        //5快手核销
        Double ksMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 5, userId);
        Integer ksCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 5, userId);
        //6抖音核销
        Double dyMoney = ordersService.selectShopOpenOrdersMoney(tearoomId, time, type, 6, userId);
        Integer dyCount = ordersService.selectShopOpenOrdersCount(tearoomId, time, type, 6, userId);
        Map<String,Object> result=new HashMap<>();
        result.put("sumMoney",sumMoney);
        result.put("wxMoney",wxMoney);
        result.put("xjMoney",xjMoney);
        result.put("mdMoney",mdMoney);
        result.put("mtMoney",mtMoney);
        result.put("ksMoney",ksMoney);
        result.put("dyMoney",dyMoney);
        result.put("sumCount",sumCount);
        result.put("wxCount",wxCount);
        result.put("xjCount",xjCount);
        result.put("mdCount",mdCount);
        result.put("mtCount",mtCount);
        result.put("ksCount",ksCount);
        result.put("dyCount",dyCount);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectOrdersNowSumMinuteByGoodsId")
    @ApiOperation("店铺时长统计")
    public Result selectOrdersNowSumMinuteByGoodsId(Long tearoomId){
        String time = DateUtils.format(new Date());
        Double dayMinute = ordersService.selectOrdersSumMinuteByGoodsId(1, time, tearoomId,null,null,null);
        Double monthMinute = ordersService.selectOrdersSumMinuteByGoodsId(2, time, tearoomId,null,null,null);
        Double yearMinute = ordersService.selectOrdersSumMinuteByGoodsId(3, time, tearoomId,null,null,null);
        Map<String,Object> result=new HashMap<>();
        result.put("dayMinute", dayMinute);
        result.put("monthMinute", monthMinute);
        result.put("yearMinute", yearMinute);
        return Result.success().put("data",result);
    }

    @Login
    @GetMapping("/selectOrdersNowSumMinuteByGoodsIdAndTime")
    @ApiOperation("车位时长和收益统计")
    public Result selectOrdersNowSumMinuteByGoodsId(Long tearoomId,String startTime,String endTime){
        List<Goods> goodsList = goodsService.list(new QueryWrapper<Goods>().eq("tearoom_id", tearoomId));
        for (Goods goods:goodsList){
            Double ordersMinute = ordersService.selectOrdersSumMinuteByGoodsId(4, startTime, null,goods.getGoodsId(),null,endTime);
            Double ordersMoney = ordersService.selectOrdersSumMoneyByGoodsId(4, startTime, null,goods.getGoodsId(),null,endTime);
            goods.setOrdersMinute(ordersMinute);
            goods.setOrdersMoney(ordersMoney);
        }
        return Result.success().put("data",goodsList);
    }

    @Login
    @GetMapping("/selectOrdersCountByDate")
    @ApiOperation("订单统计")
    public Result selectOrdersCountByDate(Long tearoomId,String startTime,String endTime,Integer type){
        return ordersService.selectOrdersCountByDate(tearoomId, startTime, endTime, type);
    }

    @Login
    @GetMapping("/selectOrdersDateByTearoomId")
    @ApiOperation("订单统计")
    public Result selectOrdersDateByTearoomId(Long tearoomId){
        return ordersService.selectOrdersDateByTearoomId(tearoomId);
    }

}
