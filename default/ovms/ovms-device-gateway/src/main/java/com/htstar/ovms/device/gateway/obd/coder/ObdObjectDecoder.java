package com.htstar.ovms.device.gateway.obd.coder;

import com.htstar.ovms.common.core.util.ByteDataUtil;
import com.htstar.ovms.common.core.util.OvmDateUtil;
import com.htstar.ovms.device.api.dto.ProtoTransferDTO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @Description: 解码器
 * 解码器属于比较核心的部分，自定义解码协议、粘包、拆包等都在里面实现，继承自ByteToMessageDecoder，
 * 其实ByteToMessageDecoder的内部已经帮我们处理好了拆包/粘包的问题，只需要按照它的设计原则去实现decode方法即可
* @Author:         范利瑞
* @CreateDate:     2020/3/20 11:00
*/
@Slf4j
public class ObdObjectDecoder extends ByteToMessageDecoder {
    //https://blog.csdn.net/qq_24874939/article/details/86475285

    //https://blog.csdn.net/qq_41884976/article/details/91917025?depth_1-utm_source=distribute.pc_relevant.none-task&utm_source=distribute.pc_relevant.none-task

    //最小的数据长度：开头标准位1字节
    private static int MIN_DATA_LEN=6;
    //数据解码协议的开始标志
    private static byte[] PROTOCOL_HEADER = {0x40,0x40};
    //数据解码协议的结束标志
    private static byte[] PROTOCOL_TAIL= {0x0d,0x0a};


    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        if (in.readableBytes()>MIN_DATA_LEN){
            //读取开头
            byte[] header= ByteBufUtil.getBytes(in,0,2);
            if (Arrays.equals(header,PROTOCOL_HEADER)){
                //读取字节数据的长度
                byte[] lenByteArr = ByteBufUtil.getBytes(in,2,2);
                int len = ByteDataUtil.bytesToShortLittle(lenByteArr);
                byte [] pkgData= ByteBufUtil.getBytes(in,4,len - 6);//读取核心的数据
                byte[] tail = ByteBufUtil.getBytes(in,len - 2,2);//获取尾

                byte[] allheard = ByteBufUtil.getBytes(in,0,27);
                if (Arrays.equals(tail,PROTOCOL_TAIL)){
//                    log.info("数据解码成功");
                    // 获取设备号
                    int startIndex = 1;//前面还有一个版本号直接跳过
                    byte[] bDeviceSnName = ByteDataUtil.bytesFromBytes(pkgData, startIndex, 20);
                    String deviceSn = ByteDataUtil.parseStr(bDeviceSnName);

                    // 具体指令类型
                    startIndex += bDeviceSnName.length;
                    byte[] bMethodName = ByteDataUtil.bytesFromBytes(pkgData, startIndex, 2);
                    String method = ByteDataUtil.bytesToHexString(bMethodName);

                    // 报文内容(bao包含校验码)
                    startIndex += bMethodName.length;
                    byte[] data = ByteDataUtil.bytesFromBytes(pkgData, startIndex, pkgData.length - startIndex);
                    out.add(ProtoTransferDTO.builder()
                            .deviceSn(deviceSn)
                            .data(data)
                            .methodName(method)
                            .revCstTime(OvmDateUtil.getCstNow())
                            .header(allheard)
                            .tail(tail)
                            .build());
                    in.skipBytes(in.readableBytes());
                    return;
                }else {
                    log.debug("数据解码协议结束标志位:{} [错误!]，期待的结束标志位是：{}",tail,PROTOCOL_TAIL);
                    return;
                }
            }else {
                log.debug("开头不对，可能不是期待的客服端发送的数，将自动略过这一个字节");
            }
        }else {
            log.debug("数据长度不符合要求，期待最小长度是："+MIN_DATA_LEN+" 字节");
            return;
        }

    }
}
