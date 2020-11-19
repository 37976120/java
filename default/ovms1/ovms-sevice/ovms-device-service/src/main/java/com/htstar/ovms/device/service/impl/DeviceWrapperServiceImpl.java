package com.htstar.ovms.device.service.impl;

import cn.hutool.core.date.DateUtil;
import com.htstar.ovms.common.core.util.ByteDataUtil;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.device.api.constant.CommandConstant;
import com.htstar.ovms.device.api.constant.DeviceOnlineConstant;
import com.htstar.ovms.device.api.constant.ObdMethodConstant;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.api.entity.DeviceCommand;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.handle.event.DeviceUpOriDataEvent;
import com.htstar.ovms.device.handle.event.ObdAlarmProcotoEvent;
import com.htstar.ovms.device.handle.event.ObdConditionProcotoEvent;
import com.htstar.ovms.device.handle.event.ObdGpsProcotoEvent;
import com.htstar.ovms.device.protoco.*;
import com.htstar.ovms.device.service.*;
import com.htstar.ovms.device.util.ObdConditionUtil;
import com.htstar.ovms.device.util.ObdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Description: 谁被协议解析（主要解析byte[]强业务相关的处理，如果非强业务及时性靠后，可以在事件监听中处理）
 * Author: flr
 * Date: Created in 2020/6/11
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class DeviceWrapperServiceImpl implements DeviceWrapperService {

    @Autowired
    private DeviceTripService deviceTripService;

    @Autowired
    private DeviceService deviceService;

    @Autowired
    private DeviceLastDataService deviceLastDataService;

    @Autowired
    private DeviceCommandService commandService;

    /**
     *  上下文对象
     */
    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    public MessageWrapper handlePackageWrapper(ProtoTransferDTO dto) {
        try{
            //异步数据入库
            String method = dto.getMethodName().toUpperCase();
            publisher.publishEvent(new DeviceUpOriDataEvent(this,dto));
            switch (method){
                case ObdMethodConstant.LOGING_NUM :
                    return login(dto);

                case ObdMethodConstant.LOGIN_OUT_NUM :
                    return loginOut(dto);

                case ObdMethodConstant.ALARM_NUM :
                    return alarm(dto);

                case ObdMethodConstant.FLOW_NUM :
                    return flow(dto);

                case ObdMethodConstant.GPS_NUM :
                    return gps(dto);

                case ObdMethodConstant.CONDITION_NUM :
                    return condition(dto);

                case ObdMethodConstant.HEARTBEAT_NUM:
                    return heartbeat(dto);

                case ObdMethodConstant.SET_REPLY_NUM:
                    return setReply(dto);

                case ObdMethodConstant.GSENSOR_NUM:
                    return gsensor(dto);
                default:
                    log.warn("暂未解析该种类的协议，协议编号：{}",method);
                    return MessageWrapper.send(dto.getDeviceSn());
            }
        }catch (Exception e){
            log.error("解析OBD协议异常：",e);
            return MessageWrapper.send(dto.getDeviceSn());
        }

    }

    /**
     * Description: 登出
     * Author: flr
     * Company: 航通星空
     */
    private MessageWrapper loginOut(ProtoTransferDTO dto) {
        log.info("设备：[{}]登出 CST{}",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        //更新在线状态
        deviceLastDataService.updateOnline(dto.getDeviceSn(), DeviceOnlineConstant.OFFLINE);
        return MessageWrapper.loginOut(dto.getDeviceSn(),null);
    }

    /**
     * @Description:    GSensor数据上传
     * @Author:         范利瑞
     * @CreateDate:     2020/4/10 15:44
     */
    private MessageWrapper gsensor(ProtoTransferDTO dto) {
        log.info("设备：[{}]GSensor数据 CST{}",dto.getDeviceSn(),DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        return MessageWrapper.send(dto.getDeviceSn());
    }

    /**
     * Description: 设置指令响应
     * Author: flr
     * Company: 航通星空
     */
    private MessageWrapper setReply(ProtoTransferDTO dto) {
        int startIndex = 0;// 指令序号 不处理
        byte[] cmdSeqData = ByteDataUtil.bytesFromBytes(dto.getData(), startIndex, 2);
        int protocoSeq = (int) ByteDataUtil.bytesToShortLittle(cmdSeqData);
        log.info("设备：[{}]设置指令回包 CST{}，协议内指令序列号：{}",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()),protocoSeq);

        DeviceCommand deviceCommand = commandService.getEnt(dto.getDeviceSn(),protocoSeq);
        if (null != deviceCommand){
            Integer commandConstant = deviceCommand.getCommandConstant();
            if (null == commandConstant || commandConstant.intValue() == 0){
                return MessageWrapper.send(dto.getDeviceSn());
            }
            switch (commandConstant.intValue()){
                case CommandConstant
                        .GPS_SET:
                    commandService.crackGpsSetReply(deviceCommand,dto);
                    break;
                default:
                    log.warn("系统暂未添加该类型的设置回包解析");
            }
        }

        return MessageWrapper.send(dto.getDeviceSn());
    }

    /**
     * @Description:    心跳
     * @Author:         范利瑞
     * @CreateDate:     2020/4/7 11:59
     */
    private MessageWrapper heartbeat(ProtoTransferDTO dto) {
        log.info("设备：[{}]心跳 CST{}",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        // 应答类型
        byte[] type = { (byte) 0x90, (byte) 0x03 };
        byte[] msg = ObdUtil.setResponseDate(dto.getDeviceSn(), type, null);

        return MessageWrapper.heartBeat(dto.getDeviceSn(),msg);
    }


    /**
     * @Description:    工况 上传
     * @Author:         范利瑞
     * @CreateDate:     2020/3/27 17:47
     */
    private MessageWrapper condition(ProtoTransferDTO dto) {
        log.info("设备：[{}]工况 CST{}",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        byte[] data = dto.getData();
        String deviceSn = dto.getDeviceSn();
        int index = 0;
        ObdConditionProcoto conditionProcoto = new ObdConditionProcoto();
        conditionProcoto.setDeviceSn(deviceSn);
        conditionProcoto.setRevTime(dto.getRevCstTime());

        ObdStatDataTp obdStatDataTp = ObdUtil.crackStatDataTp(data,index);
        conditionProcoto.setObdStatDataTp(obdStatDataTp);
        index = obdStatDataTp.getIndex();

        // 采集间隔
        byte[] collecteIntervalData = ByteDataUtil.bytesFromBytes(data, index, 2);
        short collecteInterval = ByteDataUtil.bytesToShortLittle(collecteIntervalData);
        conditionProcoto.setCollecteInterval((int) collecteInterval);
        index +=2;

        // 工况类型个数
        byte[] conTypeCountData = ByteDataUtil.bytesFromBytes(data, index, 1);
        int conTypeCount = (int) conTypeCountData[0];
        conditionProcoto.setConTypeCount(conTypeCount);
        index ++;

        //工况类型数组	U16[x]
        List<String> conTypeArray = new ArrayList<>();

        if (conTypeCount > 0) {
            // 循环工况类型放到数组里
            for (int i = 0; i < conTypeCount; i++) {
                byte[] conTypeData1 = ByteDataUtil.bytesFromBytes(data, index, 1);
                index ++;

                byte[] conTypeData2 = ByteDataUtil.bytesFromBytes(data, index, 1);
                String conType = ByteDataUtil.bytesToHexString(ByteDataUtil.byteMerger(conTypeData2, conTypeData1));// 小端转大端
                index ++;
                conTypeArray.add(conType);
            }
        }
        conditionProcoto.setConTypeList(conTypeArray);

        // 5.工况数据的包数
        byte[] conGroupCountData = ByteDataUtil.bytesFromBytes(data, index, 1);
        int conGroupCount = (int) conGroupCountData[0];
        index ++;
        conditionProcoto.setConGroupCount(conGroupCount);

        // 6.每包工况数据的长度
        byte[] conGroupSizeData = ByteDataUtil.bytesFromBytes(data, index, 1);
        int conGroupSize = (int) conGroupSizeData[0];
        conditionProcoto.setConGroupSize(conGroupSize);
        index++;

        int rpm = 0; // 发动机转速： 0x210c
        int speed = 0; // 车速：0x210d
        //7.con_data	工况数据	x	x	x = con_group_count * con_group_size
        byte[] conDataByte = ByteDataUtil.bytesFromBytes(data, index, conGroupCount*conGroupSize);

        List<ConditionModel> conditionModelList = new ArrayList<>();
        // 工况数据
        int pidIndex = 0;
        for (int i = 0; i < conTypeArray.size(); i++) {
            String pid = conTypeArray.get(i);
            ConditionModel obdCondition = ObdConditionUtil.getConditionModel(pid,conDataByte,pidIndex);
            pidIndex += obdCondition.getByteLenght();
            obdCondition.setPidKey(pid);

            if (null != obdCondition.getConValue()) {
                if (i + 1 == conGroupCount) {// 把数组最后的一组数据更新到 obd_condition_last 表
                    conditionProcoto.setLastCondition(obdCondition);
                }
                if (pid.equals("210c")) {
                    rpm = Integer.parseInt(obdCondition.getConValue());
                }
                if (pid.equals("210d")) {
                    speed = Integer.parseInt(obdCondition.getConValue());
                }
                conditionModelList.add(obdCondition);
            }
        }

        conditionProcoto.setSpeed(speed);
        conditionProcoto.setRpm(rpm);
        conditionProcoto.setConditionModelList(conditionModelList);
        try{
            //发布事件入库
            publisher.publishEvent(new ObdConditionProcotoEvent(this,conditionProcoto));
        }catch (Exception e){
            log.error("工况事件发布失败:",e);
        }

        return MessageWrapper.send(dto.getDeviceSn(),null);
    }

    /**
     * @Description:    GPS 上传
     * @Author:         范利瑞
     * @CreateDate:     2020/3/27 17:47
     */
    private MessageWrapper gps(ProtoTransferDTO dto) {
        log.info("设备：[{}]GPS CST{}",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        byte[] data = dto.getData();
        ObdGpsProtoco obdGpsProtoco = new ObdGpsProtoco();
        obdGpsProtoco.setDeviceSn(dto.getDeviceSn());
        int startIndex = 0;
        byte[] flag = ByteDataUtil.bytesFromBytes(data, startIndex, 1);
        int flagS = (int) flag[0];
        if (flagS == 0) {// 常规
            obdGpsProtoco.setFlag(0);
        } else {// 补传
            obdGpsProtoco.setFlag(1);
        }
        startIndex ++;

        //1	flag	GPS数据标志	U8	1	=0x00表示常规GPS数据上传
        //=0x01 表示历史GPS数据上传
        //2	stat_data	统计数据包	STAT_DATA		其中UTC_Time为采集最后一条GPS数据的时间
        //3	gpsdata	GPS数据	GPS_DATA		GPS_ITEM个数 = 终端参数中的打包个数
        //按参数设置，范围是0 ~ 30
        //无GPS模块或隐私，此条协议不需要上传
        //4	rpmdata	RPM数据	RPM_DATA		RPM_ITEM个数 = GPS_ITEM个数，范围是0 ~ 30，特别地，RPM_ITEM的值为0xFFFF表示无效转速值。

        // 获取stat_data部分数据-------------------------------------------------
        //1 last_accon_time	最近ACC点火时间	DATE_TIME	4	OBD所在车辆最近一次acc点火的时间
        ObdStatDataTp obdStatDataTp =
                ObdUtil.crackStatDataTp(data,startIndex);
        obdGpsProtoco.setObdStatDataTp(obdStatDataTp);
        startIndex = obdStatDataTp.getIndex();
        log.info("车辆点火状态"+obdGpsProtoco.getObdStatDataTp().getObdVStateTp().getFireState());
        //gpsdata	GPS数据	GPS_DATA---------------------------------------------
        ObdGpsDataTp obdGpsDataTp = ObdUtil.crackGpsDataTp(dto.getDeviceSn(),data,startIndex, dto.getRevCstTime());
        obdGpsProtoco.setObdGpsDataTp(obdGpsDataTp);
        startIndex = obdGpsDataTp.getIndex();

        if (flagS == 0 && obdGpsDataTp.getGpsCount() >0 ){
            //处理行程 TODO（也可改为异步处理）
            Integer tripId =deviceTripService.dealProcotoTrip(dto.getDeviceSn(),obdGpsDataTp,obdStatDataTp);
            if(tripId == null){
                log.error("------获取行程ID为null");
            }
            obdGpsProtoco.setTripId(tripId);//行程ID交给后续
        }

        //	rpmdata	RPM数据	RPM_DATA---------------------------------------------
        ObdRpmDataTp obdRpmDataTp = ObdUtil.crackRpmDataTp(data,startIndex);
        obdGpsProtoco.setObdRpmDataTp(obdRpmDataTp);

        try{
            //发布事件入库
            publisher.publishEvent(new ObdGpsProcotoEvent(this,obdGpsProtoco));
        }catch (Exception e){
            log.error("GPS事件发布失败:",e);
        }
        return MessageWrapper.send(dto.getDeviceSn());
    }




    /**
     * @Description:    支持的数据流类型
     * @Author:         范利瑞
     * @CreateDate:     2020/3/20 16:25
     */
    public MessageWrapper flow(ProtoTransferDTO dto){
        log.info("设备：[{}]支持的数据流类型协议 CST{}",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        return MessageWrapper.send(dto.getDeviceSn(),null);
    }


    /**
     * @Description:    警情上传
     * @Author:         范利瑞
     * @CreateDate:     2020/3/20 16:25
     */
    public MessageWrapper alarm(ProtoTransferDTO dto){
        log.info("CST：{} -->设备：[{}]警情",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        ObdAlarmProtoco obdAlarmProtoco = new ObdAlarmProtoco();
        obdAlarmProtoco.setRevTime(dto.getRevCstTime());
        obdAlarmProtoco.setDeviceSn(dto.getDeviceSn());
        byte[] data = dto.getData();
        int index = 0;
        // 警情编号
        byte[] alarmNoByte = ByteDataUtil.bytesFromBytes(data, index, 4);
        int alarmNo = ByteDataUtil.bytesToIntLittle(alarmNoByte);
        obdAlarmProtoco.setAlarmNo((long)alarmNo);
        index += 4;
        ObdStatDataTp obdStatDataTp = ObdUtil.crackStatDataTp(data,index);
        index = obdStatDataTp.getIndex();
        obdAlarmProtoco.setObdStatDataTp(obdStatDataTp);

        ObdGpsDataTp obdGpsDataTp = ObdUtil.crackGpsDataTp(dto.getDeviceSn(),data,index,dto.getRevCstTime());
        obdAlarmProtoco.setObdGpsDataTp(obdGpsDataTp);
        index = obdGpsDataTp.getIndex();

        // 警情个数
        byte[] alarmCountData = ByteDataUtil.bytesFromBytes(data, index, 1);
        int alarmCount = (int) alarmCountData[0];
        index ++;
        obdAlarmProtoco.setAlarmCount(alarmCount);

        List<AlarmDataTp> alarmDataTpList = ObdUtil.crackAlarmDataTp(alarmCount,data, index);
        obdAlarmProtoco.setAlarmDataTpList(alarmDataTpList);

        //发布警情时间监听
        try{
            publisher.publishEvent(new ObdAlarmProcotoEvent(this,obdAlarmProtoco));
        }catch (Exception e){
            log.error("发布警情的异步时间监听错误：",e);
        }

        return MessageWrapper.send(dto.getDeviceSn());
    }

    /**
     * @Description:    设备登入处理
     * @Author:         范利瑞
     * @CreateDate:     2020/3/20 16:25
     */
    public MessageWrapper login(ProtoTransferDTO dto){
        log.info("CST：{} -->设备：[{}]登入",dto.getDeviceSn(), DateUtil.formatLocalDateTime(dto.getRevCstTime()));
        // 获取StatData部分数据
        int index = 0;
        ObdStatDataTp obdStatDataTp = ObdUtil.crackStatDataTp(dto.getData(),index);

        log.debug("obdStatDataTp-->{}",obdStatDataTp);
        // IP
        byte[] ipaddress = { (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff };
        // 端口
        byte[] port = { 0x00, 0x00 };
        // 时间戳
        int t = (int) (System.currentTimeMillis() / 1000);
        byte[] time = ByteDataUtil.toPrimitives(ByteDataUtil.intToBytesLittle(t));
        // 全部内容
        byte[] content = ByteDataUtil.byteMerger(ByteDataUtil.byteMerger(ipaddress, port), time);
        // 应答类型
        byte[] type = { (byte) 0x90, (byte) 0x01 };
        byte[] msg = ObdUtil.setResponseDate(dto.getDeviceSn(), type, content);

        //检测是否是允许连接的设备
        Integer deviceId = deviceService.getAllowStatus(dto.getDeviceSn());
        if (deviceId == null){
            return MessageWrapper.refuseLogin(dto.getDeviceSn(),msg);
        }
        // 回复指令
        return MessageWrapper.login(dto.getDeviceSn(),msg);
    }

}
