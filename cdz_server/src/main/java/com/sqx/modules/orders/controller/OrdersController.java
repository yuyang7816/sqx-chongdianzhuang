package com.sqx.modules.orders.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.DateUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.annotation.LoginUser;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.orders.entity.Orders;
import com.sqx.modules.orders.service.OrdersService;
import com.sqx.modules.pay.dao.PayDetailsDao;
import com.sqx.modules.sys.controller.AbstractController;
import com.sqx.modules.tearoom.entity.Goods;
import com.sqx.modules.tearoom.service.GoodsService;
import com.sqx.modules.utils.excel.ExcelData;
import com.sqx.modules.utils.excel.ExportExcelUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@Api(value = "订单信息", tags = {"订单信息"})
@RequestMapping(value = "/orders")
public class OrdersController extends AbstractController {

    @Autowired
    private OrdersService ordersService;
    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PayDetailsDao payDetailsDao;


    @GetMapping("/selectOrdersList")
    @ApiOperation("查询订单列表")
    public Result selectOrdersList(Integer page,Integer limit,Long userId,String payUserName,String payPhone,
                                   String ordersNo,String goodsName,Integer status,Long tearoomId,
                                   String startTime,String endTime,Integer payWay,Integer money,Integer isShop,
                                   String dateTime,String cameraId,Integer goodsCategory,String tearoomName,
                                   Integer platformType,Long openUserId,Integer ordersType){
        return ordersService.selectOrdersList(page, limit, userId, payUserName, payPhone, ordersNo, goodsName,
                status,tearoomId,startTime,endTime,null,payWay,money,isShop,dateTime,cameraId,goodsCategory,
                tearoomName,platformType,openUserId,ordersType);
    }

    @GetMapping("/excelOrders")
    @ApiOperation("导出订单列表")
    public void excelOrders(Long userId,String payUserName,String payPhone,
                                   String ordersNo,String goodsName,Integer status,Long tearoomId,
                            String startTime,String endTime,Integer payWay,Integer money,Integer isShop,
                            String dateTime,Integer goodsCategory,String tearoomName,Integer platformType,
                            Long openUserId,Integer ordersType, HttpServletResponse response) throws Exception{
        ExcelData data = ordersService.excelOrders(userId, payUserName, payPhone, ordersNo, goodsName, status,
                tearoomId, startTime, endTime, null,payWay,money,isShop,dateTime,goodsCategory,tearoomName,
                platformType,openUserId,ordersType);
        ExportExcelUtils.exportExcel(response,"订单列表.xlsx",data);
    }

    @PostMapping("/refundOrders")
    @ApiOperation("退款订单")
    public Result refundOrders(String ordersNo){
        return ordersService.refundOrders(ordersNo);
    }

    @PostMapping("/refundOrdersMoney")
    @ApiOperation("退款订单")
    public Result refundOrdersMoney(String ordersNo,BigDecimal refundMoney,String refundRemark){
        return ordersService.refundOrdersMoney(ordersNo,refundMoney,refundRemark);
    }

    @PostMapping("/updateOrders")
    @ApiOperation("修改订单信息")
    public Result updateOrders(@RequestBody Orders orders){
        ordersService.updateById(orders);
        return Result.success();
    }


    @GetMapping("/selectOrdersDetails")
    @ApiOperation("查询订单详情")
    public Result selectOrdersDetails(String ordersNo){
        return Result.success().put("data",ordersService.selectOrdersByNo(ordersNo));
    }

    @GetMapping("/selectOrdersDetailsVip")
    @ApiOperation("查询订单详情会员")
    public Result selectOrdersDetailsVip(String ordersNo){
        return Result.success().put("data",ordersService.selectOrdersDetailsVip(ordersNo));
    }

    @GetMapping("/selectTeamUserList")
    @ApiOperation("获取团队列表")
    public Result selectTeamUserList(Long userId, Integer page, Integer limit, Integer type){
        UserEntity user = userService.selectUserById(userId);
        return ordersService.selectTeamUserList(page,limit,user.getInvitationCode(),type,user.getUserId());
    }

    @GetMapping("/selectTearoomOrdersCount")
    @ApiOperation("查询充电桩收益分析")
    public Result selectTearoomOrdersCount(Integer page,Integer limit,Integer flag,String time){
        return ordersService.selectTearoomOrdersCount(page, limit, flag, time);
    }

    @GetMapping("/selectTearoomOrdersIncome")
    @ApiOperation("查询充电桩收益统计")
    public Result selectTearoomOrdersIncome(Integer flag,String time,Long tearoomId){
        //总收入
        Double sumMoney = ordersService.selectOrdersMoneyByTearoom(null, null, tearoomId,null);
        //当前收入
        Double money = ordersService.selectOrdersMoneyByTearoom(flag, time, tearoomId,null);
        //微信收入
        Double wxMoney = ordersService.selectOrdersMoneyByTearoom(flag, time, tearoomId,1);
        //支付宝收入
        Double zfbMoney = ordersService.selectOrdersMoneyByTearoom(flag, time, tearoomId,2);
        //零钱收入
        Double lqMoney = ordersService.selectOrdersMoneyByTearoom(flag, time, tearoomId,3);
        //订单年成交量
        Integer yearCount = ordersService.selectOrdersCountByTearoom(3, time, tearoomId,null);
        //订单月成交量
        Integer monthCount = ordersService.selectOrdersCountByTearoom(2, time, tearoomId,null);
        //订单日成交量
        Integer dayCount = ordersService.selectOrdersCountByTearoom(1, time, tearoomId,null);
        Map<String,Object> result=new HashMap<>();
        result.put("sumMoney",sumMoney);result.put("money",money);
        result.put("wxMoney",wxMoney);result.put("zfbMoney",zfbMoney);
        result.put("lqMoney",lqMoney);result.put("yearCount",yearCount);
        result.put("monthCount",monthCount);result.put("dayCount",dayCount);
        return Result.success().put("data",result);
    }

    @PostMapping("/roomChange")
    @ApiOperation("更换房间")
    public Result roomChange(Long ordersId,Long goodsId){
        return ordersService.roomChange(ordersId, goodsId);
    }

    @PostMapping("/closeOrders")
    @ApiOperation("提前结束订单")
    public Result closeOrders(Long ordersId){
        return ordersService.closeOrders(ordersId);
    }

    @GetMapping("/selectOrdersMoneyByTearoomId")
    @ApiOperation("查询店铺总收益")
    public Result selectOrdersMoneyByTearoomId(Long tearoomId){
        Double sumMoney = ordersService.selectOrdersMoneyByTearoom(null, null, tearoomId,null);
        Integer sumCount = ordersService.selectOrdersCountByTearoom(null, null, tearoomId,null);
        int goodsCount = goodsService.count(new QueryWrapper<Goods>().eq("tearoom_id", tearoomId));
        Map<String,Object> result=new HashMap<>();
        result.put("sumMoney", sumMoney);
        result.put("sumCount", sumCount);
        result.put("goodsCount", goodsCount);
        return Result.success().put("data",result);
    }

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


    @GetMapping("/selectTearoomOrdersByGoodsIdAndTime")
    @ApiOperation("查询包间收益统计")
    public Result selectTearoomOrdersByGoodsIdAndTime(Integer flag,String time,Long tearoomId,Long goodsId,String endTime){
        Double money = ordersService.selectOrdersMoneyByGoods(flag, time, tearoomId,goodsId,null,endTime);
        Integer count = ordersService.selectOrdersCountByGoods(flag, time, tearoomId,goodsId,null,endTime);
        Map<String,Object> result=new HashMap<>();
        result.put("money", money);
        result.put("count", count);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectTearoomCount")
    @ApiOperation("台球馆订单收益统计")
    public Result selectTearoomCount(Integer flag,String time,Integer classify){
        if(classify==null){
            classify=1;
        }
        String dateTime=null;
        if(1==flag){
            dateTime=time;
        }else if(2==flag){
            dateTime=time.substring(0,7);
        }else{
            dateTime=time.substring(0,4);
        }
        int dzfCount = ordersService.count(new QueryWrapper<Orders>().like("create_time",dateTime).eq("status", 1)
                .in(classify==1,"goods_category",1,2,3).in(classify==2,"goods_category",4));
        int dsyCount = ordersService.count(new QueryWrapper<Orders>().like("create_time",dateTime).eq("status", 2)
                .in(classify==1,"goods_category",1,2,3).in(classify==2,"goods_category",4));
        int syzCount = ordersService.count(new QueryWrapper<Orders>().like("create_time",dateTime).eq("status", 3)
                .in(classify==1,"goods_category",1,2,3).in(classify==2,"goods_category",4));
        int ywcCount = ordersService.count(new QueryWrapper<Orders>().like("create_time",dateTime).in("status", 4,6)
                .in(classify==1,"goods_category",1,2,3).in(classify==2,"goods_category",4));
        //统计 支付金额，结算金额，押金，待缴费
        Double payMoney = ordersService.selectTearoomPayMoneyCountByTime(flag, time,classify);
        Double shopMoney = ordersService.selectTearoomShopMoneyCountByTime(flag, time,classify);
        Double cashPledge = ordersService.selectTearoomCashPledgeCountByTime(flag, time,classify);
        Double sumMoney = ordersService.selectTearoomSumMoneyCountByTime(flag, time,classify);

        Map<String,Object> result=new HashMap<>();
        result.put("dzfCount", dzfCount);
        result.put("dsyCount", dsyCount);
        result.put("syzCount", syzCount);
        result.put("ywcCount", ywcCount);
        result.put("payMoney", payMoney);
        result.put("shopMoney", shopMoney);
        result.put("cashPledge", cashPledge);
        result.put("sumMoney", sumMoney);
        return Result.success().put("data",result);
    }


    @GetMapping("/selectUserByOrdersHourList")
    @ApiOperation("开台时长排行榜")
    public Result selectUserByOrdersHourList(Integer page,Integer limit,Integer type,String userName,String phone,String city,Long tearoomId,
                                             String startTime,String endTime,String tearoomName){
        return Result.success().put("data",ordersService.selectUserByOrdersHourList(page, limit, type, userName, phone, city, tearoomId,startTime,endTime,tearoomName));
    }

    @GetMapping("/selectOrdersUserMoneyList")
    @ApiOperation("消费排行榜")
    public Result selectOrdersUserMoneyList(Integer page,Integer limit,String userName,String phone,String startTime,String endTime){
        return Result.success().put("data",ordersService.selectOrdersUserMoneyList(page, limit, userName, phone,startTime,endTime));
    }


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

    @GetMapping("/selectOrdersCountStatisticsByYear")
    @ApiOperation("订单数量统计")
    public Result selectOrdersCountStatisticsByYear(String startTime, String endTime){
        //状态 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价
        List<Integer> ordersCountList=new ArrayList<>();
        List<Integer> ordersDzfCountList=new ArrayList<>();
        List<Integer> ordersDsyCountList=new ArrayList<>();
        List<Integer> ordersSyzCountList=new ArrayList<>();
        List<Integer> ordersYwcCountList=new ArrayList<>();
        List<Integer> ordersYqxCountList=new ArrayList<>();
        List<Integer> ordersDpjCountList=new ArrayList<>();
        List<String> year=new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar=Calendar.getInstance();
        Date parse = null;
        try {
            parse = simpleDateFormat.parse(startTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(parse);
        while (true){
            String dateTime = simpleDateFormat.format(calendar.getTime());
            //状态 总数量 1待支付 2待使用 3使用中 4已完成 5已取消 6待评价
            //总数量
            int ordersCount = ordersService.count(new QueryWrapper<Orders>().like("create_time", dateTime));
            ordersCountList.add(ordersCount);
            //1待支付
            int ordersDzfCount = ordersService.count(new QueryWrapper<Orders>().eq("status",1).like("create_time", dateTime));
            ordersDzfCountList.add(ordersDzfCount);
            //2待使用
            int ordersDsyCount = ordersService.count(new QueryWrapper<Orders>().eq("status",2).like("create_time", dateTime));
            ordersDsyCountList.add(ordersDsyCount);
            //3使用中
            int ordersSyzCount = ordersService.count(new QueryWrapper<Orders>().eq("status",3).like("create_time", dateTime));
            ordersSyzCountList.add(ordersSyzCount);
            //4已完成
            int ordersYwcCount = ordersService.count(new QueryWrapper<Orders>().eq("status",4).like("create_time", dateTime));
            ordersYwcCountList.add(ordersYwcCount);
            //5已取消
            int ordersYqxCount = ordersService.count(new QueryWrapper<Orders>().eq("status",5).like("create_time", dateTime));
            ordersYqxCountList.add(ordersYqxCount);
            //6待评价
            int ordersDpjCount = ordersService.count(new QueryWrapper<Orders>().eq("status",6).like("create_time", dateTime));
            ordersDpjCountList.add(ordersDpjCount);
            year.add(dateTime);
            if(dateTime.equals(endTime)){
                break;
            }
            calendar.add(Calendar.DATE,1);
        }
        Map<String,Object> result=new HashMap<>();
        result.put("ordersCountList",ordersCountList);
        result.put("ordersDzfCountList",ordersDzfCountList);
        result.put("ordersDsyCountList",ordersDsyCountList);
        result.put("ordersSyzCountList",ordersSyzCountList);
        result.put("ordersYwcCountList",ordersYwcCountList);
        result.put("ordersYqxCountList",ordersYqxCountList);
        result.put("ordersDpjCountList",ordersDpjCountList);
        result.put("year",year);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectPayMoneyData")
    @ApiOperation("充值收益统计")
    public Result selectPayMoneyData(Integer type,String time){
        //订单支付金额
        Double ordersPayMoney = ordersService.selectOrdersPayMoneyByTime(type, time);
        //商家收益
        Double ordersShopMoney = ordersService.selectOrdersShopMoneyByTime(type, time);
        //分销金额
        Double ordersFenMoney = ordersService.selectOrdersFenMoneyByTime(type, time);
        //平台收益
        BigDecimal ordersPingMoney=BigDecimal.valueOf(ordersPayMoney).subtract(BigDecimal.valueOf(ordersShopMoney)).subtract(BigDecimal.valueOf(ordersFenMoney));

        Map<String,Object> result=new HashMap<>();
        result.put("ordersPayMoney",ordersPayMoney);
        result.put("ordersShopMoney",ordersShopMoney);
        result.put("ordersFenMoney",ordersFenMoney);
        result.put("ordersPingMoney",ordersPingMoney);
        return Result.success().put("data",result);
    }

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
                Double money = ordersService.selectOrdersMoneyByTearoomId(way, dateTime+"-01", tearoomId, null, null,null);
                ordersMoneyList.add(money==null?0.00:money);
            }else{
                Double money = ordersService.selectOrdersMoneyByTearoomId(way, dateTime, tearoomId, null, null,null);
                ordersMoneyList.add(money==null?0.00:money);
            }
        }
        Map<String,Object> result=new HashMap<>();
        result.put("dateTimeList",dateTimeList);
        result.put("ordersMoneyList",ordersMoneyList);
        return Result.success().put("data",result);
    }

    @GetMapping("/selectLoveOrdersData")
    @ApiOperation("爱心订单统计")
    public Result selectLoveOrdersData(Integer type,String time){
        //累计订单
        Integer ordersCount = ordersService.selectOrdersCountByNoIsShop(type, time);
        //金额订单
        Integer ordersMoneyCount = ordersService.selectOrdersMoneyCountByNoIsShop(type, time);
        //优惠券订单
        Integer ordersCouponCount = ordersService.selectOrdersCouponCountByNoIsShop(type, time);
        Map<String,Object> result=new HashMap<>();
        result.put("ordersCount",ordersCount);
        result.put("ordersMoneyCount",ordersMoneyCount);
        result.put("ordersCouponCount",ordersCouponCount);
        return Result.success().put("data",result);
    }

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


    @PostMapping("/shopUpdateOrdersStatus")
    @ApiOperation("商家挂账订单")
    public Result shopUpdateOrdersStatus(Long ordersId,Long ordersGoodsId){
        return ordersService.shopUpdateOrdersStatus(ordersId, ordersGoodsId);
    }

}
