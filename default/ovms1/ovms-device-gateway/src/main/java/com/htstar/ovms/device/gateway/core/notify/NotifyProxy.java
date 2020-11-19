package com.htstar.ovms.device.gateway.core.notify;

import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.common.core.util.ByteDataUtil;
import com.htstar.ovms.device.api.wrapper.MessageWrapper;
import com.htstar.ovms.device.gateway.core.connector.tcp.TcpConnector;
import com.htstar.ovms.device.gateway.core.constant.Constants;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * Description: 执行指令notify
 * Author: flr
 * Company: 航通星空
 */
@Slf4j
public class NotifyProxy {

    private TcpConnector tcpConnector;

    public NotifyProxy(TcpConnector tcpConnector) {
        this.tcpConnector = tcpConnector;
    }

    private final ConcurrentHashMap<Long, NotifyFuture> futureMap = new ConcurrentHashMap<Long, NotifyFuture>();

    public int notify(long seq, MessageWrapper wrapper, int timeout) throws Exception {
        try {
            NotifyFuture<Boolean> future = doSendAsync(seq, wrapper, timeout);
            if (future == null) {
                return Constants.NOTIFY_NO_SESSION;
            } else {
                return future.get(timeout, TimeUnit.MILLISECONDS) ? Constants.NOTIFY_SUCCESS : Constants.NOTIFY_FAILURE;
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /**
     * 时间单位毫秒
     * @param wrapper
     * @param timeout
     * @return
     * @throws Exception
     */
    public int notify(MessageWrapper wrapper, int timeout) throws Exception {
        try {
            NotifyFuture<Boolean> future = doSendAsync(wrapper.getSeq(), wrapper, timeout);
            if (future == null) {
                return Constants.NOTIFY_NO_SESSION;
            } else {
                return future.get(timeout, TimeUnit.MILLISECONDS) ? Constants.NOTIFY_SUCCESS : Constants.NOTIFY_FAILURE;
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void reply(MessageWrapper message) throws Exception {
        try {
            long seq = message.getSeq();
            log.info("reply seq -> " + seq + ", message -> " + ByteDataUtil.bytesToHexString(message.getBody()));
            final NotifyFuture future = this.futureMap.get(seq);
            if (future != null) {
                future.setSuccess(true);
                futureMap.remove(seq);
                log.info("reply seq -> " + seq + " success.");
            } else {
                log.info("reply seq -> " + seq + " expire.");
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private NotifyFuture doSendAsync(long seq, MessageWrapper wrapper, int timeout) throws Exception {
        if (wrapper == null) {
            throw new Exception("下发序列化指令时消息包wrapper不能为null.");
        }
        String deviceSn = wrapper.getDeviceSn();
        if (StrUtil.isBlank(deviceSn)) {
            throw new Exception("下发序列化指令时deviceSn不能为null.");
        }
        if (tcpConnector.exist(deviceSn)) {
            // start.
            final NotifyFuture future = new NotifyFuture(timeout);
            this.futureMap.put(seq, future);

            log.info("序列化化下发指令 seq -> " + seq + ", deviceSn -> " + deviceSn);
            tcpConnector.send(deviceSn, wrapper.getBody());

            future.setSentTime(System.currentTimeMillis()); // 置为已发送
            return future;
        } else {
            // tcpConnector not exist deviceSn
            return null;
        }
    }
}
