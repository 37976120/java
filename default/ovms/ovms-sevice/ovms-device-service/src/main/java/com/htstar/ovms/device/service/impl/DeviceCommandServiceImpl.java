package com.htstar.ovms.device.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.common.security.util.SecurityUtils;
import com.htstar.ovms.common.sequence.sequence.Sequence;
import com.htstar.ovms.device.api.constant.CommandConstant;
import com.htstar.ovms.device.api.constant.RocketMQConstant;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import com.htstar.ovms.device.api.entity.DeviceCommand;
import com.htstar.ovms.device.api.req.ObdSetGpsReq;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.mapper.DeviceCommandMapper;
import com.htstar.ovms.device.service.DeviceCommandService;
import com.htstar.ovms.device.service.RocketMqSenderService;
import com.htstar.ovms.device.util.ObdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description: 指令下发
 * Author: flr
 * Date: Created in 2020/6/17
 * Company: 航通星空
 * Modified By:
 */
@Service
@Slf4j
public class DeviceCommandServiceImpl extends ServiceImpl<DeviceCommandMapper, DeviceCommand> implements DeviceCommandService {

    /**
     * 分布式发号器（全局ID）
     */
    @Autowired
    private Sequence sequence;

    @Autowired
    RocketMqSenderService rocketMqSenderService;

    /**
     * Description:设置GPS eg
     * Author: flr
     */
    @Override
    public R setObdGps(ObdSetGpsReq req) {
        Integer userId = SecurityUtils.getUser().getId();
        String deviceSn = req.getDeviceSn();

        int tlv_count = 0;

        if (req.getUpPackageNum() != null){
            tlv_count ++;
        }

        if (req.getGpsSwitch() != null){
            tlv_count ++;
        }

        if (req.getUpTimeInterval() != null){
            tlv_count ++;
        }

        //默认的及时下发
        DeviceCommand deviceCommand = new DeviceCommand();
        deviceCommand.setUserId(userId);
        deviceCommand.setDeviceSn(deviceSn);
        //构造一个指令序号
        deviceCommand.setProtocoSeq(getProtocoSeq(deviceSn));
        deviceCommand.setCommandConstant(CommandConstant.GPS_SET);
        deviceCommand.setSetNum(tlv_count);
        deviceCommand.setRemark("测试设置OBD GPS");
        this.save(deviceCommand);

        MessageWrapper messageWrapper = new MessageWrapper();
        messageWrapper.setDeviceSn(deviceSn);
        messageWrapper.setSeq(deviceCommand.getId());//表的自增ID作为指令唯一
        messageWrapper.setMessageType(MessageWrapper.MessageType.COMMAND);
        byte[] body = ObdUtil.getObdGpsCommandByte(req,deviceCommand.getProtocoSeq());
        messageWrapper.setBody(body);
        rocketMqSenderService.sendWithTags(messageWrapper, RocketMQConstant.TAG.OBD_COMMAND_TAG);
        return R.ok();

    }

    @Override
    public Integer getGatewayStatusById(Long id) {
        return this.baseMapper.getGatewayStatusById(id);
    }

    @Override
    public DeviceCommand getEnt(String deviceSn, int protocoSeq) {
        return this.baseMapper.getEnt(deviceSn,protocoSeq);
    }

    /**
     * 判断OBD设置GPS相关设置是否成功
     * @param deviceCommand
     * @param dto
     * @return
     */
    @Override
    public boolean crackGpsSetReply(DeviceCommand deviceCommand, ProtoTransferDTO dto) {
        //设置数量
        int setNum = deviceCommand.getSetNum();
        ObdSetGpsReq req = ObdUtil.crackReplyGpsSet(setNum,dto.getData());
        int tlv_count = 0;

        if (req.getUpPackageNum() != null){
            tlv_count ++;
        }

        if (req.getGpsSwitch() != null){
            tlv_count ++;
        }

        if (req.getUpTimeInterval() != null){
            tlv_count ++;
        }
        if (tlv_count == setNum){
            //更改指令状态
            deviceCommand.setExecuteTime(OvmDateUtil.getCstNow());
            deviceCommand.setGatewayStatus(2);
            deviceCommand.setCommandStatus(2);
            this.updateById(deviceCommand);
            log.info("设置指令回包成功：{}",deviceCommand);
            return true;
        }else {
            //更改指令状态
            deviceCommand.setExecuteTime(OvmDateUtil.getCstNow());
            deviceCommand.setGatewayStatus(2);
            deviceCommand.setCommandStatus(3);
            this.updateById(deviceCommand);
            log.info("设置指令回包失败：{}",deviceCommand);
            return false;
        }

    }

    /**
     * Description: 获取一个可用的协议指令序号
     * Author: flr
     * Company: 航通星空
     */
    private Long getProtocoSeq(String deviceSn) {

        Long exMaxProtocoSeq = this.baseMapper.getMaxProtocoSeq(deviceSn);
        if (null == exMaxProtocoSeq){
            return new Long(1);
        }else {
            return ++exMaxProtocoSeq;
        }
    }
}
