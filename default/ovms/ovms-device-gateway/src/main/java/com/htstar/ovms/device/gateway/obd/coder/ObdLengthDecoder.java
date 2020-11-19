package com.htstar.ovms.device.gateway.obd.coder;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteOrder;

public class ObdLengthDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * LengthFieldBasedFrameDecoder里ByteOrder默认是大端（高位在前低位在后）：
     * @param byteOrder
     * @param maxFrameLength
     * @param lengthFieldOffset
     * @param lengthFieldLength
     * @param lengthAdjustment
     * @param initialBytesToStrip
     * @param failFast
     */
    public ObdLengthDecoder(ByteOrder byteOrder, int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip, boolean failFast) {
        super(ByteOrder.LITTLE_ENDIAN, maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip, true);
    }


    public ObdLengthDecoder() {
        super(ByteOrder.LITTLE_ENDIAN, 1024*1024, 2, 2, -4, 0, true);
    }
}
