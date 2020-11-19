package com.htstar.ovms.device.util;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.htstar.ovms.common.core.util.ByteDataUtil;
import com.htstar.ovms.common.core.util.ByteDataUtil;
import com.htstar.ovms.common.core.util.LitteByteUtil;
import com.htstar.ovms.device.api.req.ObdSetGpsReq;
import com.htstar.ovms.device.protoco.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ObdUtil {

    public static byte[] setResponseDate(String deviceSn, byte[] type, byte[] content) {
        // 1
        byte[] protocol_head = { 0x40, 0x40 };// 协议头2个字节
        // 3
        byte[] protocol_version = { 0x03 };// 协议版本 1个字节
        // 4
        byte[] obd_id = ByteDataUtil.completionByte(20, deviceSn);
        // 5
        byte[] protocol_type = type;// 信息类型 2个字节,使用大端字节
        // 2
        int totalSize = 31;
        if (null != content) {
            totalSize += content.length;// 总协议长度
        }
        byte[] protocol_length = ByteDataUtil.short2byte(totalSize);// 将总协议长度转为2个字节数组

        // 7//校验码 2个字节 校验和, 计算校验和时包括(1),(2),(3),(4),(5),(6)
        byte[] crcMessage = getTotalByte(protocol_head, protocol_length, protocol_version, obd_id, protocol_type,
                content);
        int crc = Integer.parseInt(CrCUtil.crc16x25(crcMessage), 16); // 生成crc校验码
        byte[] crcBytes = ByteDataUtil.shortToBytesLittle((short) crc);// 转bytes小端
        // 8
        byte[] protocol_tail = { 0x0D, 0x0A };// 协议尾标志2个字节
        byte[] byteMerger = ByteDataUtil.byteMerger(ByteDataUtil.byteMerger(crcMessage, crcBytes), protocol_tail);
        return byteMerger;
    }

    /**
     * crc和校验后返回校验码数组
     *
     * @param protocol_head    协议头2个字节
     * @param protocol_length  协议字节总长度
     * @param protocol_version 协议版本 1个字节
     * @param obd_id           obd编号 20个字节
     * @param protocol_type    信息类型 2个字节,使用大端字节
     * @param content          协议内容
     * @return
     */
    public static byte[] getTotalByte(byte[] protocol_head, byte[] protocol_length, byte[] protocol_version,
                                      byte[] obd_id, byte[] protocol_type, byte[] content) {
        byte[] data1 = ByteDataUtil.byteMerger(protocol_head, protocol_length);
        byte[] data2 = ByteDataUtil.byteMerger(protocol_version, obd_id);

        if (null != content) {
            byte[] data3 = ByteDataUtil.byteMerger(protocol_type, content);
            byte[] data5 = ByteDataUtil.byteMerger(data1, data2);
            return ByteDataUtil.byteMerger(data5, data3);
        } else {
            byte[] data5 = ByteDataUtil.byteMerger(data1, data2);
            return ByteDataUtil.byteMerger(data5, protocol_type);
        }
    }

    /**
    * @Description:    通用设置指令
    * @Author:         范利瑞
    * @CreateDate:     2020/4/8 11:24
    */
    public static byte[] setTlv(String deviceSn, Map<String, String> param, long typeSn) {
        //查询代码  0x01,0x34:1;0x02,0x34:000000; 用;分割，由于是小端在填写参数的的时候要颠倒协议,":"为键和值分离
        String set_key_value_str = param.get("setCode");
        if (StrUtil.isBlank(set_key_value_str))return null;
        /**
         * 每个字符串数组代表要设置的键值对
         */
        String[] set_key_value_strArray = set_key_value_str.replace("0x","")//去掉0x
                .split(";");//拆为键值对
        short query_count = (short)set_key_value_strArray.length;
        //1. 信息类型 2001
        byte[] type_byte = { (byte) 0x20, (byte) 0x01 };
        //2. 指令序号（用于判断当前查询的唯一标识）U16 2字节无符号整型 范围0 ~ 65535
        byte[] cmd_seq_byte = ByteDataUtil.longToBytesLittle(typeSn);
        //3.tlv_count	tlv数据包个数	U8	1	(3)的数据包组数 范围 1~255
        byte[] tlv_count = {(byte)(query_count)};
        //4.tlv_array	tlv数据组	TLV[x]		x = tlv_count 设置的TLV数据组
        List<byte[]> tag_array_list = new ArrayList<>();
        for(int i=0;i<query_count;i++){//循环TLV的数量
            String[] keyValueStrArr = set_key_value_strArray[i].split(":");
            String[] keyStrArr = keyValueStrArr[0].split(",");
            String valueStr = keyValueStrArr[1];
            byte[] tag_array = new byte[2];
            tag_array[0] = (byte) Integer.parseInt(keyStrArr[0], 16);
            tag_array[1] = (byte) Integer.parseInt(keyStrArr[1], 16);
            //设置wifi指令也要判断类型
            byte[] valueContent = ObdTlvUtil.getByte(keyValueStrArr[0].replace(",",""),valueStr);
            // length 占2个字节
            short length = (short) (valueContent.length);
            byte[] lengthBytes = ByteDataUtil.shortToBytesLittle(length);
            tag_array_list.add(ByteDataUtil.byteMergerMore(tag_array,lengthBytes,valueContent));
        }
        byte[] allTag = ByteDataUtil.byteMergerList(tag_array_list);
        byte[] byteData = ByteDataUtil.byteMergerMore(cmd_seq_byte,tlv_count,allTag);
        byte[] resultData = setResponseDate(deviceSn, type_byte, byteData);
        log.debug("通用设置指令接口:" + ByteDataUtil.bytesToHexString(resultData, "[", "]", null, false));
        return resultData;
    }

    /**
     * 转化OBD设备UTC时间to北京时间
     * @param data
     * @param startIndex
     * @return
     */
    public static LocalDateTime castUtcToCst(byte[] data, int startIndex) {
        byte[] bytes = ByteDataUtil.bytesFromBytes(data, startIndex, 4);
        int intTime = ByteDataUtil.bytesToIntLittle(bytes);
        LocalDateTime time = LocalDateTime.ofEpochSecond(intTime,0, ZoneOffset.ofHours(8));
        return time;
    }

    /**
     * Description: 解析stat_data数据
     * Author: flr
     * Company: 航通星空
     */
    public static ObdStatDataTp crackStatDataTp(byte[] data,int startIndex) {
        ObdStatDataTp obdStatDataTp = new ObdStatDataTp();
        LocalDateTime last_accon_time = ObdUtil.castUtcToCst(data, startIndex);
        startIndex +=4;
        obdStatDataTp.setLastAcconTime(last_accon_time);
        //2	UTC_Time	设备时间	DATE_TIME	4	设备当前时间
        LocalDateTime deviceTime = ObdUtil.castUtcToCst(data,startIndex);
        startIndex +=4;
        obdStatDataTp.setDeviceTime(deviceTime);
        //3	total_trip_mileage	累计里程	U32	4	OBD上电后到(1)的累计里程 单位:米(M)
        byte[] btotalMileage = ByteDataUtil.bytesFromBytes(data, startIndex, 4);
        int totalMileage = ByteDataUtil.bytesToIntLittle(btotalMileage);
        obdStatDataTp.setTotalTripMileage((long) totalMileage);
        startIndex +=4;
        //4	current_trip_mileage	当前里程	U32	4	从(1)到当前的里程 单位:米(M)
        byte[] bcurrentMileage = ByteDataUtil.bytesFromBytes(data, startIndex, 4);
        int currentMileage = ByteDataUtil.bytesToIntLittle(bcurrentMileage);
        obdStatDataTp.setCurrentTripMileage(currentMileage);
        startIndex +=4;
        //5	total_fuel	累计油耗	U32	4	OBD上电后到(1)的累计油耗 单位: 0.01升(L)
        byte[] btotalFuel = ByteDataUtil.bytesFromBytes(data, startIndex, 4);
        int totalFuel = ByteDataUtil.bytesToIntLittle(btotalFuel);
        obdStatDataTp.setTotalFuel(totalFuel * 0.01);
        startIndex +=4;
        //6	current_fuel	当前油耗	U16	2	从(1)到当前的油耗
        //单位:0.01升(L)
        byte[] bCurrentFuel = ByteDataUtil.bytesFromBytes(data, startIndex, 2);
        short currentFuel = ByteDataUtil.bytesToShortLittle(bCurrentFuel);
        obdStatDataTp.setCurrentFuel(currentFuel * 0.01);
        startIndex +=2;


        //7	vstate	状态包	VSTATE	len(VSTATE)	当前汽车状态 U8[4]

        //这里读取第3个byte的
        ObdVStateTp obdVStateTp = crackObdVStateTp(data,startIndex);
        obdStatDataTp.setObdVStateTp(obdVStateTp);
        startIndex = obdVStateTp.getIndex();

        //8.reserve	预留	U8[8]	8
        startIndex += 8;

        obdStatDataTp.setIndex(startIndex);
        return obdStatDataTp;
    }

    /**
     * Description: 解析 VSTATE数据
     * Author: flr
     * Company: 航通星空
     */
    public static ObdVStateTp crackObdVStateTp(byte[] data,int index) {
        ObdVStateTp obdVStateTp = new ObdVStateTp();
        String s0 = ByteDataUtil.byteToBit(ByteDataUtil.bytesFromBytes(data, index, 1)[0]);
        obdVStateTp.setS0(s0);
        index ++;

        String s1 = ByteDataUtil.byteToBit(ByteDataUtil.bytesFromBytes(data, index, 1)[0]);
        obdVStateTp.setS1(s1);
        index ++;

        byte s2Byte = ByteDataUtil.bytesFromBytes(data, index, 1)[0];
        String s2 = ByteDataUtil.byteToBit(s2Byte);
        obdVStateTp.setS2(s2);
        //从s2中分离出点火状态
        short fireState = (short) ((s2Byte >> 2) & 0x1);
        obdVStateTp.setFireState(fireState);

        index ++;

        String s3 = ByteDataUtil.byteToBit(ByteDataUtil.bytesFromBytes(data, index, 1)[0]);
        obdVStateTp.setS3(s3);
        index ++;

        obdVStateTp.setIndex(index);
        return obdVStateTp;

    }

    /**
     * Description: 解析GPD_DATA数据
     * Author: flr
     * 时间为北京时间
     */
    public static ObdGpsDataTp crackGpsDataTp(String deviceSn, byte[] data, int startIndex,LocalDateTime rcvTime) {
        ObdGpsDataTp obdGpsDataTp = new ObdGpsDataTp();
        //1	gps_count	GPS个数	U8	1	GPS数组个数
        byte[] gpsCountData = ByteDataUtil.bytesFromBytes(data, startIndex, 1);
        int gpsCount = (int) gpsCountData[0];
        startIndex ++;

        obdGpsDataTp.setGpsCount(gpsCount);
        if (gpsCount <= 0){
            obdGpsDataTp.setIndex(startIndex);
            return obdGpsDataTp;
        }
        ObdGpsDataTp halfOgdt =  crackGpsItemTp(deviceSn,data,startIndex,gpsCount,rcvTime);
        obdGpsDataTp.setGpsItemTpList(halfOgdt.getGpsItemTpList());
        obdGpsDataTp.setLastGpsItemTp(halfOgdt.getLastGpsItemTp());
        startIndex = startIndex + (19 * gpsCount);
        obdGpsDataTp.setIndex(startIndex);
        return obdGpsDataTp;
    }

    /**
     * Description: 转化OBD GpsItem
     * Author: flr
     */
    private static ObdGpsDataTp crackGpsItemTp(String deviceSn, byte[] data, int startIndex,int gpsCount,LocalDateTime rcvTime) {
        ObdGpsDataTp obdGpsDataTp = new ObdGpsDataTp();
        long timeLog = 0;
        GpsItemTp lastGpsItemTp = null;

        List<GpsItemTp> gpsItemTpList = new ArrayList<>();
        for (int i = 0; i < gpsCount; i++) {
            GpsItemTp gpsItemTp = new GpsItemTp();
            gpsItemTp.setDeviceSn(deviceSn);
            gpsItemTp.setRcvTime(rcvTime);

            // GPS时间
            byte[] bGPSTime = ByteDataUtil.bytesFromBytes(data, startIndex, 6);

            log.debug("int year{}, int month{}, int dayOfMonth{}, int hour{}, int minute{}, int second{}",
                    bGPSTime[2] + 2000, bGPSTime[1], bGPSTime[0], bGPSTime[3],
                    bGPSTime[4], bGPSTime[5]
                    );
            LocalDateTime utcGpsTime = LocalDateTime.of(bGPSTime[2] + 2000, bGPSTime[1], bGPSTime[0], bGPSTime[3],
                    bGPSTime[4], bGPSTime[5]);

            gpsItemTp.setGpsTime(utcGpsTime.plusHours(8));

            // latitude
            startIndex += 6;

            byte[] bLatitude = ByteDataUtil.bytesFromBytes(data, startIndex, 4);
            int latitude = ByteDataUtil.bytesToIntLittle(bLatitude);
            gpsItemTp.setLat(ByteDataUtil.doubleNDots((double) latitude / 3600000, 7));
            startIndex += 4;

            // longitude
            byte[] bLongitude = ByteDataUtil.bytesFromBytes(data, startIndex, 4);
            int longitude = ByteDataUtil.bytesToIntLittle(bLongitude);
            gpsItemTp.setLng(ByteDataUtil.doubleNDots((double) longitude / 3600000, 7));
            startIndex += 4;

            // speed
            byte[] bSpeed = ByteDataUtil.bytesFromBytes(data, startIndex, 2);
            short speed = ByteDataUtil.bytesToShortLittle(bSpeed);
            gpsItemTp.setSpeed(ByteDataUtil.floatNDots((float) speed / 1000 * 36, 2));
            startIndex += 2;

            // direction
            byte[] bDirection = ByteDataUtil.bytesFromBytes(data, startIndex, 2);
            short direction = ByteDataUtil.bytesToShortLittle(bDirection);
            gpsItemTp.setDirection(ByteDataUtil.floatNDots((float) direction / 10, 2));
            startIndex += 2;


            // 定位标志
            byte[] valflagByte = ByteDataUtil.bytesFromBytes(data, startIndex, 1);
            byte satellite = (byte) (valflagByte[0] >> 4);
            gpsItemTp.setPositionStarNum(-satellite);

            startIndex ++;

            // gps flag
            byte bit0 = (byte) ((valflagByte[0] >> 0) & 0x1);//Bit0     1：东经E(+)，  0：西经W(-)
            byte bit1 = (byte) ((valflagByte[0] >> 1) & 0x1);//Bit1     1：北纬N(+)，  0：南纬S(-)

            byte bit2 = (byte) ((valflagByte[0] >> 2) & 0x1);
            byte bit3 = (byte) ((valflagByte[0] >> 3) & 0x1);

            // 0：东经；1：西经
            Integer longitude_way = 0;
            if (bit0==1) {
                longitude_way = 0;
            }
            if (bit0==0) {
                longitude_way = 1;
            }
            // 0：北纬；1：南纬
            Integer latitude_way = 0;
            if (bit1==1) {
                latitude_way = 0;
            }
            if (bit1==0) {
                latitude_way = 1;
            }
            gpsItemTp.setLongitudeWay(longitude_way);
            gpsItemTp.setLatitudeWay(latitude_way);
            //00---未定位 01---2D定位 11---3D定位
            //定位标志：0=未定位；1=2D定位；2=3D定位；
            if (bit2 == 0 && bit3 == 1) {
                gpsItemTp.setGpsFlag(1);
            } else if (bit2 == 1 && bit3 == 1) {
                gpsItemTp.setGpsFlag(2);
            }  else {
                gpsItemTp.setGpsFlag(0);
            }
            if (gpsItemTp.getLongitudeWay() != null && gpsItemTp.getLongitudeWay() == 1) {
                // 西经
                gpsItemTp.setLng(-gpsItemTp.getLng());
            }
            if (gpsItemTp.getLatitudeWay() != null && gpsItemTp.getLatitudeWay() == 1) {
                // 南纬
                gpsItemTp.setLat(-gpsItemTp.getLat());
            }
            gpsItemTpList.add(gpsItemTp);

            if(utcGpsTime.getSecond() > timeLog){
                timeLog = utcGpsTime.getSecond();
                lastGpsItemTp = gpsItemTp;
            }
        }

        obdGpsDataTp.setGpsItemTpList(gpsItemTpList);

        if (null == lastGpsItemTp){
            //排序取得时间最大的
            gpsItemTpList = gpsItemTpList.stream().sorted(Comparator.comparing(GpsItemTp::getGpsTime).reversed()).collect(Collectors.toList());
            obdGpsDataTp.setLastGpsItemTp(gpsItemTpList.get(0));
        }else {
            obdGpsDataTp.setLastGpsItemTp(lastGpsItemTp);
        }
        return obdGpsDataTp;
    }

    /**
     * Description: 逆地理位置解析
     * @param mockStatus  是否开启测试模式，默认否
     * Author: flr
     * Company: 航通星空
     */
    public static String getLocation(boolean mockStatus, String amapApi, String convertApi, String apiKey, Double lat, Double lng) {
        if (mockStatus){
            return "当前环境为测试环境，为了避免高德地图封号。获取的到的地址是固定地址";
        }

        //经度在前，纬度在后，经纬度间以“,”分割，经纬度小数点后不要超过 6 位
        String locations = NumberUtil.decimalFormat("###.######",lng)  + "," + NumberUtil.decimalFormat("###.######",lat);
        String res = HttpUtil.post(convertApi, "locations=" + locations + "&key=" + apiKey+ "&coordsys=gps");
        JSONObject objApi = JSON.parseObject(res);
        //坐标转换
        if (objApi.containsKey("infocode") && objApi.get("infocode").toString().equals("10000")) {

            String addr = HttpUtil.post(amapApi, "location=" + objApi.get("locations").toString() + "&key=" + apiKey);
            JSONObject jsonObject = JSON.parseObject(addr);
            if (jsonObject.containsKey("infocode") && jsonObject.get("infocode").toString().equals("10000")) {
                JSONObject regeocode = (JSONObject) jsonObject.get("regeocode");
                JSONObject addressComponent = (JSONObject) regeocode.get("addressComponent");

                log.info(
                        "citycode:{},province:{},city:{},district:{},"
                        ,addressComponent.get("citycode").toString()
                        ,addressComponent.get("province").toString()
                        ,addressComponent.get("city").toString()
                        ,addressComponent.get("district").toString()
                );
                return regeocode.get("formatted_address").toString();
            } else {
                return "解析地里位置失败";
            }

        }else{
            return "解析地里位置失败";
        }
    }

    /**
     * Description: RPM数据	RPM_DATA
     * Author: flr
     * Company: 航通星空
     */
    public static ObdRpmDataTp crackRpmDataTp(byte[] data, int startIndex) {
        ObdRpmDataTp obdRpmDataTp = new ObdRpmDataTp();
        byte[] count = ByteDataUtil.bytesFromBytes(data, startIndex, 1);
        int rpmCount = (int) count[0];
        startIndex ++;

        obdRpmDataTp.setRpmCount(rpmCount);


        List<Integer> rpmList = new ArrayList<>();
        for (int i = 0; i < rpmCount; i++) {
            byte[] rpmByte = ByteDataUtil.bytesFromBytes(data, startIndex, 2);
            int rpm = (int) ByteDataUtil.bytesToShortLittle(rpmByte);
            startIndex +=2;
            rpmList.add(rpm);
        }

        obdRpmDataTp.setRpm(rpmList.get(0));

        return obdRpmDataTp;
    }

    /**
     * Description: 获取设置OBD-GPS的指令byte[]
     * Author: flr
     */
    public static byte[] getObdGpsCommandByte(ObdSetGpsReq req,long protocoSeq) {

        //1. 信息类型 2001
        byte[] type_byte = { (byte) 0x20, (byte) 0x01 };


        //1. 指令序号
        byte[] seq = ByteDataUtil.shortToBytesLittle((short) protocoSeq);

        //2.tlv_count	tlv数据包个数	U8
        short tlv_count = 0;


        //3.tlv_array	tlv数据组	TLV[x]		x = tlv_count 设置的TLV数据组
        List<byte[]> tag_array_list = new ArrayList<>();

        if (req.getUpPackageNum() != null){
            //GPS上传包数
            //(0x1103)	1	采集多少包GPS数据才上传一次
            //类型：U8
            //单位：包
            //范围：1~30，默认值1
            byte[] upPackageNumByte = ObdTlvUtil.getByte("0x03,0x11",req.getUpPackageNum().toString());

            // length 占2个字节
            short length = (short) (upPackageNumByte.length);
            byte[] lengthBytes = ByteDataUtil.shortToBytesLittle(length);

            //键
            byte[] tag_array = new byte[2];
            tag_array[0] = (byte) Integer.parseInt("03", 16);
            tag_array[1] = (byte) Integer.parseInt("11", 16);

            tag_array_list.add(ByteDataUtil.byteMergerMore(tag_array,lengthBytes,upPackageNumByte));
            tlv_count ++;
        }

        if (req.getGpsSwitch() != null){
            //GPS开关
            //(0x1101)	1	类型：U8
            //=0 开启隐私功能
            //=1 需要上传，默认开启
            byte[] gpsSwitchByte = ObdTlvUtil.getByte("0x01,0x11",req.getGpsSwitch().toString());

            // length 占2个字节
            short length = (short) (gpsSwitchByte.length);
            byte[] lengthBytes = ByteDataUtil.shortToBytesLittle(length);

            //键
            byte[] tag_array = new byte[2];
            tag_array[0] = (byte) Integer.parseInt("01", 16);
            tag_array[1] = (byte) Integer.parseInt("11", 16);

            tag_array_list.add(ByteDataUtil.byteMergerMore(tag_array,lengthBytes,gpsSwitchByte));
            tlv_count ++;
        }

        if (req.getUpTimeInterval() != null){
            //GPS采集间隔
            //(0x1102)	2	多长时间采集一次GPS数据
            //类型：U16
            //单位：秒
            //范围：2 ~ 600，默认值120s
            byte[] upTimeIntervalByte = ObdTlvUtil.getByte("0x02,0x11",req.getUpTimeInterval().toString());


            // length 占2个字节
            short length = (short) (upTimeIntervalByte.length);
            byte[] lengthBytes = ByteDataUtil.shortToBytesLittle(length);

            //键
            byte[] tag_array = new byte[2];
            tag_array[0] = (byte) Integer.parseInt("02", 16);
            tag_array[1] = (byte) Integer.parseInt("11", 16);

            tag_array_list.add(ByteDataUtil.byteMergerMore(tag_array,lengthBytes,upTimeIntervalByte));
            tlv_count ++;
        }

        byte[] tlv_count_byte = {(byte)(tlv_count)};

        byte[] allTag = ByteDataUtil.byteMergerList(tag_array_list);
        byte[] byteData = ByteDataUtil.byteMergerMore(seq,tlv_count_byte,allTag);
        byte[] resultData = setResponseDate(req.getDeviceSn(), type_byte, byteData);
        log.info("设置OBD-GPS相关共{}个原数据:{}" ,tlv_count, ByteDataUtil.bytesToHexString(resultData, "[", "]", null, false));

        return resultData;
    }

    /**
     * Description: 解析警情 ALARM_DATA
     * Author: flr
     * Company: 航通星空
     */
    public static List<AlarmDataTp> crackAlarmDataTp(int alarmCount, byte[] data, int index) {

        if (alarmCount <= 0){
            return null;
        }
        List<AlarmDataTp> alarmDataTpList = new ArrayList<>();

        for (int i = 0; i < alarmCount; i++) {
            AlarmDataTp alarmDataTp = new AlarmDataTp();
            // 警情标志
            byte[] newAlarmFlagData = ByteDataUtil.bytesFromBytes(data, index, 1);
            int newAlarmFlag = (int) newAlarmFlagData[0];
            alarmDataTp.setNewAlarmFlag(newAlarmFlag);
            index ++;


            // 警情类型
            byte[] alarmTypeData = ByteDataUtil.bytesFromBytes(data, index, 1);
            int alarmType = (int) alarmTypeData[0];
            alarmDataTp.setAlarmType(alarmType);
            index ++;


            // 当前值
            byte[] alarmDescData = ByteDataUtil.bytesFromBytes(data, index, 2);
            short crrentData = ByteDataUtil.bytesToShortLittle(alarmDescData);
            alarmDataTp.setAlarmDesc(crrentData);
            index += 2;

            // 警情阈值
            byte[] alarmThresholdData = ByteDataUtil.bytesFromBytes(data, index, 2);
            short alarmThreshold = ByteDataUtil.bytesToShortLittle(alarmThresholdData);
            alarmDataTp.setAlarmThreshold(alarmThreshold);

            index += 2;

            alarmDataTpList.add(alarmDataTp);
        }


        return alarmDataTpList;
    }

    /**
     * 解析OBD GPS设置（如果该字段为null，则说明设置失败）
     * @param setNum
     * @param data
     * @return
     */
    public static ObdSetGpsReq crackReplyGpsSet(int setNum, byte[] data) {
        ObdSetGpsReq req = new ObdSetGpsReq();
        int index = 2;// 跳过指令序号


        // 成功的tag数据包个数	U8	1	(3)中的成功TLV标识个数 范围 0~255
        byte[] success_tag_count = ByteDataUtil.bytesFromBytes(data, index, 1);
        short respCount = (short) success_tag_count[0];
        log.info("2.成功的tag数据包个数:{}",respCount);
        index += 1;

        // tlv数组
        for(int i=0;i<respCount;i++){
            //1	tag	标识	U16	2	TLV数据的标识，可以根据此标识判断后续数据的类型
            byte[] tagData = ByteDataUtil.bytesFromBytes(data, index, 2);
            index += 2;
            String tag = ByteDataUtil.bytesToHexString(tagData);
            log.info("TLV数据的标识{}",i,tag);

            switch (tag){
                case "0111" ://GPS开关  0x00：GPS功能关闭 0x01：GPS功能开启
                    req.setGpsSwitch(0);
                    log.info("设置GPS开关成功");
                    continue;
                case "0311" :
                    //GPS上传包数
                    //(0x1103)	1	采集多少包GPS数据才上传一次
                    //类型：U8
                    //单位：包
                    //范围：1~30，默认值1
                    req.setUpPackageNum(0);
                    log.info("设置GPS上传包数成功");
                    continue;

                case "0211" ://GPS采集间隔
                        req.setUpTimeInterval(0);
                    log.info("设置GPS采集间隔成功");
                    continue;

                default: continue;
            }
        }

        return req;
    }
}
