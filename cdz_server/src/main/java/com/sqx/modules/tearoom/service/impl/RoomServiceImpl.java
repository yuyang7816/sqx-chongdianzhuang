package com.sqx.modules.tearoom.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.Result;
import com.sqx.modules.chats.utils.DateUtil;
import com.sqx.modules.common.service.CommonInfoService;
import com.sqx.modules.file.utils.Md5Utils;
import com.sqx.modules.tearoom.dao.RoomDao;
import com.sqx.modules.tearoom.entity.Room;
import com.sqx.modules.tearoom.service.RoomService;
import com.sqx.modules.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Slf4j
@Service
public class RoomServiceImpl extends ServiceImpl<RoomDao, Room> implements RoomService {

    @Autowired
    private CommonInfoService commonInfoService;
    private ReentrantReadWriteLock reentrantReadWriteLock=new ReentrantReadWriteLock(true);


    @Override
    public Result insertRoom(Room room){
        room.setCreateTime(DateUtil.createTime());
        baseMapper.insert(room);
//        saveGroupBind(room.getRoomNo(),"1");
        return Result.success();
    }

    @Override
    public Result updateRoom(Room room){
        baseMapper.updateById(room);
//        saveGroupBind(room.getRoomNo(),"0");
        return Result.success();
    }

    @Override
    public Result removeById(Long roomId){
        Room room = baseMapper.selectById(roomId);
        baseMapper.deleteById(roomId);
//        saveGroupBind(room.getRoomNo(),"0");
        return Result.success();
    }

    /**
     * 查询设备组
     * @param innerCode 可以为空
     * @return
     */
    @Override
    public Result selectGroup(String innerCode){
        String token = getToken();
        if(StringUtils.isEmpty(token)){
            return Result.error("获取设备信息失败！");
        }
        Map<String,String> param=new HashMap<>();
        param.put("innerCode",innerCode);
        String resultStr = HttpClientUtil.doGetToken(commonInfoService.findOne(807).getValue() + "/openapi/device/selectgroup", param,token);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        return Result.success().put("data",resultJson);
    }

    /**
     * 查询设备组下设备详情
     * @param deviceNo 不可为空
     * @return
     */
    @Override
    public JSONObject selectDevice(String deviceNo){
        String token = getToken();
        Map<String,String> param=new HashMap<>();
        param.put("deviceNo",deviceNo);
        String resultStr = HttpClientUtil.doGetToken(commonInfoService.findOne(807).getValue() + "/openapi/device/selectDevice", param,token);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        return resultJson;
    }

    /**
     * 绑定设备组
     * @param innerCode  机组编号
     * @param isBind   0是不绑定1是绑定   不可为空
     * @return
     */
    @Override
    public JSONObject saveGroupBind(String innerCode,String isBind){
        String token = getToken();
        Map<String,String> param=new HashMap<>();
        param.put("innerCode",innerCode);
        param.put("isBind",isBind);
        String resultStr = HttpClientUtil.doGetToken(commonInfoService.findOne(807).getValue() + "/openapi/device/bind", param,token);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        return resultJson;
    }

    /**
     * 操作设备
     * @param instructId 不可为空
     * @return
     */
    @Override
    public JSONObject issueInstruct(String instructId){
        String token = getToken();
        Map<String,String> param=new HashMap<>();
        param.put("instructId",instructId);
        String resultStr = HttpClientUtil.doGetToken(commonInfoService.findOne(807).getValue() + "/openapi/device/issueInstruct", param,token);
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        return resultJson;
    }


    public String getToken(){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("username",commonInfoService.findOne(808).getValue());
        jsonObject.put("password",commonInfoService.findOne(809).getValue());
        String resultStr = HttpClientUtil.doPostJson(commonInfoService.findOne(807).getValue() + "/applogin", jsonObject.toJSONString());
        JSONObject resultJson = JSONObject.parseObject(resultStr);
        String code = resultJson.getString("code");
        if("200".equals(code)){
             return resultJson.getString("token");
        }else{
            String msg = resultJson.getString("msg");
            log.error("获取设备token失败！"+msg);
        }
        return null;
    }


    @Override
    public Result issueInstruct(Long roomId,Integer type){
        Room room = baseMapper.selectById(roomId);
        if("2".equals(room.getRoomType())){
            return airConditionerControl(room.getRoomNo(),type);
        }else if("3".equals(room.getRoomType())){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (type==1) {
                return damControl(room,type);
            }
        }else if("4".equals(room.getRoomType())){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return damMusicControl(room,type);
        }else if("5".equals(room.getRoomType())){
            //称重设备 不做开启或关闭
            return Result.success();
        }
        JSONObject jsonObject = selectDevice(room.getRoomNo());
        log.error("获取设备1："+jsonObject.toJSONString());
        String code = jsonObject.getString("code");
        if(!"200".equals(code)){
            return Result.error(jsonObject.getString("msg"));
        }
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray vmsInstructs = result.getJSONArray("vmsInstructs");
        for (int i=0;i<vmsInstructs.size();i++){
            JSONObject jsonObject1 = vmsInstructs.getJSONObject(i);
            String switchShow = jsonObject1.getString("switchShow");
            String id=null;
            if(type!=null && type==1){
                String instructCode = jsonObject1.getString("instructCode");
                if(instructCode.contains("OPEN")){
                    id = jsonObject1.getString("id");
                    JSONObject jsonObject2 = issueInstruct(id);
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        log.error("操作灯失败，打开灯："+jsonObject2.toJSONString());
                    }
                }
            }else{
                String instructCode = jsonObject1.getString("instructCode");
                log.error("灯："+instructCode);
                if(instructCode.contains("CLOSE")){
                    id = jsonObject1.getString("id");
                    JSONObject jsonObject2 = issueInstruct(id);
                    log.error("关闭灯："+jsonObject2.toJSONString());
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        log.error("操作灯失败，关闭灯："+jsonObject2.toJSONString());
                    }
                }
            }
            if(type==null){
                if(StringUtils.isNotEmpty(id)){
                    //操作指令
                    JSONObject jsonObject2 = issueInstruct(id);
                    log.error("操作："+jsonObject2.toJSONString());
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        return Result.error(jsonObject2.getString("msg"));
                    }
                }
            }

        }
        if("3".equals(room.getRoomType())){
            if(room.getIsOpen()==1){
                room.setIsOpen(2);
            }else{
                room.setIsOpen(1);
            }
            baseMapper.updateById(room);
        }
        return Result.success();
    }

    @Override
    public Result issueInstruct(Long roomId,Integer type,String tokenType,String accessToken){
        Room room = baseMapper.selectById(roomId);
        if("2".equals(room.getRoomType())){
            return airConditionerControl(room.getRoomNo(),type,tokenType,accessToken);
        }
        JSONObject jsonObject = selectDevice(room.getRoomNo());
        log.error("获取设备1："+jsonObject.toJSONString());
        String code = jsonObject.getString("code");
        if(!"200".equals(code)){
            return Result.error(jsonObject.getString("msg"));
        }
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray vmsInstructs = result.getJSONArray("vmsInstructs");
        for (int i=0;i<vmsInstructs.size();i++){
            JSONObject jsonObject1 = vmsInstructs.getJSONObject(i);
            String switchShow = jsonObject1.getString("switchShow");
            String id=null;
            if(type!=null && type==1){
                String instructCode = jsonObject1.getString("instructCode");
                if(instructCode.contains("OPEN")){
                    id = jsonObject1.getString("id");
                    JSONObject jsonObject2 = issueInstruct(id);
                    log.error("打开灯："+jsonObject2.toJSONString());
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        log.error("操作灯失败，打开灯："+jsonObject2.toJSONString());
                    }
                }
            }else{
                String instructCode = jsonObject1.getString("instructCode");
                log.error("灯："+instructCode);
                if(instructCode.contains("CLOSE")){
                    id = jsonObject1.getString("id");
                    JSONObject jsonObject2 = issueInstruct(id);
                    log.error("关闭灯："+jsonObject2.toJSONString());
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        log.error("操作灯失败，关闭灯："+jsonObject2.toJSONString());
                    }
                }
            }
            if(type==null){
                if(StringUtils.isNotEmpty(id)){
                    //操作指令
                    JSONObject jsonObject2 = issueInstruct(id);
                    log.error("操作："+jsonObject2.toJSONString());
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        return Result.error(jsonObject2.getString("msg"));
                    }
                }
            }

        }
        if("3".equals(room.getRoomType())){
            if(room.getIsOpen()==1){
                room.setIsOpen(2);
            }else{
                room.setIsOpen(1);
            }
            baseMapper.updateById(room);
        }
        return Result.success();
    }




    @Override
    public Result closeIssueInstruct(Long roomId){
        Room room = baseMapper.selectById(roomId);
        JSONObject jsonObject = selectDevice(room.getRoomNo());
        String code = jsonObject.getString("code");
        if(!"200".equals(code)){
            return Result.error(jsonObject.getString("msg"));
        }
        JSONObject result = jsonObject.getJSONObject("result");
        JSONArray vmsInstructs = result.getJSONArray("vmsInstructs");
        for (int i=0;i<vmsInstructs.size();i++){
            JSONObject jsonObject1 = vmsInstructs.getJSONObject(i);
            String switchShow = jsonObject1.getString("switchShow");
            String id=null;
            if("3".equals(room.getRoomType())){
                if("1".equals(switchShow)){
                    String switchDisplay = jsonObject1.getString("switchDisplay");
                    if(switchDisplay.contains("关")){
                        id = jsonObject1.getString("id");
                    }
                }
            }else if("4".equals(room.getRoomType())){
                String instructCode = jsonObject1.getString("instructCode");
                //包厢时间到 关闭红灯
                if(instructCode.contains("HONG") && instructCode.contains("CLOSE")){
                    id = jsonObject1.getString("id");
                    JSONObject jsonObject2 = issueInstruct(id);
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        log.error("操作灯失败，关闭红灯："+jsonObject2.toJSONString());
                    }
                }
                //包厢时间到 关闭红灯
                if(instructCode.contains("LV") && instructCode.contains("CLOSE")){
                    id = jsonObject1.getString("id");
                    JSONObject jsonObject2 = issueInstruct(id);
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        log.error("操作灯失败，关闭绿灯："+jsonObject2.toJSONString());
                    }
                }
            }
            if(!"4".equals(room.getRoomType())){
                if(StringUtils.isNotEmpty(id)){
                    //操作指令
                    JSONObject jsonObject2 = issueInstruct(id);
                    String code1 = jsonObject2.getString("code");
                    if(!"200".equals(code1)){
                        return Result.error(jsonObject2.getString("msg"));
                    }
                }
            }

        }
        if("3".equals(room.getRoomType())){
            room.setIsOpen(2);
            baseMapper.updateById(room);
        }
        return Result.success();
    }


    @Override
    public void close(Long goodsId){
        List<Room> roomList = baseMapper.selectList(
                new QueryWrapper<Room>()
                        .eq(goodsId != null, "goods_id", goodsId));
        for (Room room:roomList){
            try {
                closeIssueInstruct(room.getRoomId());
            }catch (Exception e){
                e.printStackTrace();
                log.error("关闭所有设备出错："+e.getMessage(),e);
            }
        }
    }


    /**
     * 灯同时控制锁球器
     */
    @Override
    public Result lightControl(String keyCode,String channels,Integer status){
        reentrantReadWriteLock.writeLock().lock();
        try {
            String url="https://www.zsdcloud.cn:8443/openApi/getAccessToken.action";
            Map<String,String> param=new HashMap<>();
            String accessId = commonInfoService.findOne(830).getValue();
            param.put("accessId",accessId);
            String s = HttpClientUtil.doPost(url, param);
            log.info("获取灯token:"+s);
            JSONObject jsonObject = JSONObject.parseObject(s);
            String accessToken = jsonObject.getString("result");
            String urls="https://www.zsdcloud.cn:8443/openApi/equipment/control.action";
            param.put("token",accessToken);
            //值1为开灯，0为关灯
            param.put("setInfo","{\"Status\":"+status+"}");
            param.put("keyCode",keyCode);
            param.put("childDeviceKey","1");
            //1-8灯标识 只能是1-8
            param.put("channels",channels);
            String s1 = HttpClientUtil.doPost(urls, param);
            log.info("执行灯操作:"+s1);
            JSONObject jsonObject1 = JSONObject.parseObject(s1);
            String statusCode = jsonObject1.getString("statusCode");
            if(!"200".equals(statusCode)){
                return Result.error(jsonObject1.getString("message"));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("执行灯操作出错:"+e.getMessage(),e);
        }finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
        return Result.success();
    }

    /**
     * 语音音响
     */
    @Override
    public Result musicControl(String token,String id,String message){
        String url="https://speaker.17laimai.cn/notify.php";
        Map<String,String> param=new HashMap<>();
        param.put("token",token);
        param.put("id",id);
        param.put("version","3");
        param.put("message",message);
        String s = HttpClientUtil.doPost(url, param);
        log.info("音响执行返回参数:"+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        String errcode = jsonObject1.getString("errcode");
        if(!"0".equals(errcode)){
            return Result.error(jsonObject1.getString("errmsg"));
        }
        return Result.success();
    }

    public static void main(String[] args) {
        /*JSONObject jsonObject=new JSONObject();
        jsonObject.put("serveCode","SZB000000");
        jsonObject.put("equipmentCode","ECZ000000");
        jsonObject.put("deviceId","1");
        String s = HttpClientUtil.doPostJsonSXT("https://eqm.nxchiplink.com"+"/openApi/WeighingTransmitter/v1.0.0/getWeighInfo",
                jsonObject.toJSONString(),getDamTokens());
        log.info("调用dam称重返回值："+s);*/
        /*JSONObject jsonObject=new JSONObject();
        jsonObject.put("select",1);
        jsonObject.put("action",true);
        jsonObject.put("serveCode","SZB000000");
        jsonObject.put("equipmentCode","ECZ000000");
        String s = HttpClientUtil.doPostJsonSXT("https://eqm.nxchiplink.com"+"/openApi/DAM/v1.0.0/sendQuickController",
                jsonObject.toJSONString(),getDamTokens());
        log.info("调用dam闪开闪关返回值："+s);*/
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("select","1");
        jsonObject.put("serveCode","SZB000000");
        jsonObject.put("equipmentCode","ECZ000000");

        String s = HttpClientUtil.doPostJsonSXT("https://eqm.nxchiplink.com"+"/openApi/voicePlayer/v1.0.0/playerSelect",
                jsonObject.toJSONString(),getDamTokens());
        log.info("调用dam音响返回值："+s);
    }

    /**
     * 操作空调
     */
    @Override
    public Result airConditionerControl(String clientId,Integer isOpen){
        String url="http://manage.iot.smart-bird.cn/api/geek-iot/login";
        JSONObject jsonObject=new JSONObject();
        String username = commonInfoService.findOne(833).getValue();
        String password = commonInfoService.findOne(834).getValue();
        jsonObject.put("username",username);
        jsonObject.put("password",password);
        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
        log.info("空调获取accessToken:"+s);
        JSONObject jsonObject1=JSONObject.parseObject(s);
        String code = jsonObject1.getString("code");
        if(!"0".equals(code)){
            jsonObject1.getString("message");
        }
        JSONObject data = jsonObject1.getJSONObject("data");
        String accessToken = data.getString("access_token");
        String tokenType = data.getString("token_type");
        String urls="http://manage.iot.smart-bird.cn/api/geek-iot/device/pub";
        JSONObject jsonObjects=new JSONObject();
        JSONObject cmd=new JSONObject();
        //0关  1是开
        String key= "1";
        if(isOpen==1){
            key="1";
        }else{
            key="0";
        }
        cmd.put("type","event");
        cmd.put("key",key);
        jsonObjects.put("cmd",cmd.toJSONString());
        jsonObjects.put("clientId",clientId);
        String s1 = HttpClientUtil.doPostJsonKongTiao(urls, jsonObjects.toJSONString(),tokenType+" "+accessToken);
        log.info("执行空调操作:"+s1);
        JSONObject jsonObject2 = JSONObject.parseObject(s1);
        String code1 = jsonObject2.getString("code");
        if(!"0".equals(code1)){
            return Result.error(jsonObject2.getString("message"));
        }
        return Result.success();
    }

    @Override
    public Result damControl(Room room,Integer isOpen){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("select",room.getRoomSelect());
        if(isOpen==1){
            jsonObject.put("action",true);
        }else{
            jsonObject.put("action",false);
        }
        jsonObject.put("serveCode",room.getServeCode());
        jsonObject.put("equipmentCode",room.getEquipmentCode());
        String url = commonInfoService.findOne(1109).getValue();

        String s = HttpClientUtil.doPostJsonSXT(url+"/openApi/DAM/v1.0.0/sendQuickController",
                jsonObject.toJSONString(),getDamToken());
        log.info("调用dam闪开闪关返回值："+s);
        JSONObject jsonObject2 = JSONObject.parseObject(s);
        String code1 = jsonObject2.getString("code");
        if(!"200".equals(code1)){
            return Result.error(jsonObject2.getString("msg"));
        }
        return Result.success();
    }

    @Override
    public Result damMusicControl(Room room,Integer isOpen){
        JSONObject jsonObject=new JSONObject();
        if(isOpen==1){
            jsonObject.put("select",room.getMusicSelectStart());
        }else{
            jsonObject.put("select",room.getMusicSelectEnd());
        }
        jsonObject.put("serveCode",room.getServeCode());
        jsonObject.put("equipmentCode",room.getEquipmentCode());
        String url = commonInfoService.findOne(1109).getValue();

        String s = HttpClientUtil.doPostJsonSXT(url+"/openApi/voicePlayer/v1.0.0/playerSelect",
                jsonObject.toJSONString(),getDamToken());
        log.info("调用dam音响返回值："+s);
        JSONObject jsonObject2 = JSONObject.parseObject(s);
        String code1 = jsonObject2.getString("code");
        if(!"200".equals(code1)){
            return Result.error(jsonObject2.getString("msg"));
        }
        return Result.success();
    }

    @Override
    public BigDecimal getDamWeightControl(Room room){
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("deviceId",room.getDeviceId());
        jsonObject.put("serveCode",room.getServeCode());
        jsonObject.put("equipmentCode",room.getEquipmentCode());
        String url = commonInfoService.findOne(1109).getValue();

        String s = HttpClientUtil.doPostJsonSXT(url+"/openApi/WeighingTransmitter/v1.0.0/getWeighInfo",
                jsonObject.toJSONString(),getDamToken());
        log.info("调用dam称重返回值："+s);
        JSONObject jsonObject2 = JSONObject.parseObject(s);
        String code1 = jsonObject2.getString("code");
        if(!"200".equals(code1)){
            return BigDecimal.ZERO;
        }
        return jsonObject2.getBigDecimal("data");
    }

    @Override
    public Result airConditionerControl(String clientId,Integer isOpen,String tokenType,String accessToken){
        String urls="http://manage.iot.smart-bird.cn/api/geek-iot/device/pub";
        JSONObject jsonObjects=new JSONObject();
        JSONObject cmd=new JSONObject();
        //0关  1是开
        String key= "1";
        if(isOpen==1){
            key="1";
        }else{
            key="0";
        }
        cmd.put("type","event");
        cmd.put("key",key);
        jsonObjects.put("cmd",cmd.toJSONString());
        jsonObjects.put("clientId",clientId);
        String s1 = HttpClientUtil.doPostJsonKongTiao(urls, jsonObjects.toJSONString(),tokenType+" "+accessToken);
        log.info("执行空调操作:"+s1);
        JSONObject jsonObject2 = JSONObject.parseObject(s1);
        String code1 = jsonObject2.getString("code");
        if(!"0".equals(code1)){
            return Result.error(jsonObject2.getString("message"));
        }
        return Result.success();
    }

    /*public static void main(String[] args) {
        String url="http://manage.iot.smart-bird.cn/api/geek-iot/login";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("username","17868807816");
        jsonObject.put("password","yang7816");
        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
        log.info("空调获取accessToken:"+s);
        JSONObject jsonObject1=JSONObject.parseObject(s);
        String code = jsonObject1.getString("code");
        if(!"0".equals(code)){
            jsonObject1.getString("message");
        }
        JSONObject data = jsonObject1.getJSONObject("data");
        String accessToken = data.getString("access_token");
        String tokenType = data.getString("token_type");
        String urls="http://manage.iot.smart-bird.cn/api/geek-iot/device/pub";
        JSONObject jsonObjects=new JSONObject();
        JSONObject cmd=new JSONObject();
        //0关  1是开
        String key="1";
        cmd.put("type","event");
        cmd.put("key",key);
        jsonObjects.put("cmd",cmd.toJSONString());
        jsonObjects.put("clientId","8cce4e51e597");
        String s1 = HttpClientUtil.doPostJsonKongTiao(urls, jsonObjects.toJSONString(),tokenType+" "+accessToken);
        log.info("执行空调操作:"+s1);
        JSONObject jsonObject2 = JSONObject.parseObject(s1);
        String code1 = jsonObject2.getString("code");
        if(!"0".equals(code1)){
            jsonObject1.getString("message");
        }
    }*/

    @Override
    public String getDamToken() {
        // key是秘钥
        String key = commonInfoService.findOne(1119).getValue();
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式，例如：20250520
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 格式化当前日期为字符串
        String dateStr = currentDate.format(date);
        String stringMd5 = Md5Utils.md5s(key + dateStr);
        String url=commonInfoService.findOne(1109).getValue()+"/openApi/token/v1.0.0/getToken";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("openId",commonInfoService.findOne(1108).getValue());
        jsonObject.put("sign",stringMd5);
        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
        log.info("人脸识别获取token:"+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        return jsonObject1.getString("data");
    }

    public static String getDamTokens() {
        // key是秘钥
        String key = "91E560CC1990232FC86C53CEFFC4679268CBE603D342E99C8B0D0272C97699557694F0471714B488DF7F833F1D52027A";
        // 获取当前日期
        LocalDate currentDate = LocalDate.now();

        // 定义日期格式，例如：20250520
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyyMMdd");

        // 格式化当前日期为字符串
        String dateStr = currentDate.format(date);
        String stringMd5 = Md5Utils.md5s(key + dateStr);
        String url="https://eqm.nxchiplink.com"+"/openApi/token/v1.0.0/getToken";
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("openId","0eef9faad87a4397");
        jsonObject.put("sign",stringMd5);
        String s = HttpClientUtil.doPostJson(url, jsonObject.toJSONString());
        log.info("人脸识别获取token:"+s);
        JSONObject jsonObject1 = JSONObject.parseObject(s);
        return jsonObject1.getString("data");
    }

}
