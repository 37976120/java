package com.htstar.ovms.device.api.dto;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @Description:    设备协议业务模型（解包以后留下的业务数据）
* @Author:         范利瑞
* @CreateDate:     2020/3/20 15:40
*/
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProtoTransferDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 信息类型, 高字节表示主标识，低字节表示子标识
     * 主标识在0x00~0x7F的属于上行协议
     * 主标识在0x80~0xFF的属于下行协议
     * 不同的信息类型,(6)的数据不一样
     * 该字段传输使用大端字节序
     *
     *
     * 上行协议--->主动上传
     * 下行协议--->指令回包
     */
    private String protocolType;

    //协议方法名称
    protected String methodName;

    //内容之前
    protected byte[] header;
    //内容之后
    protected byte[] tail;

    //内容数据(抛弃头和尾部)
    protected byte[] data;

    //设备序列号
    protected String deviceSn;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    protected LocalDateTime revCstTime;
}
