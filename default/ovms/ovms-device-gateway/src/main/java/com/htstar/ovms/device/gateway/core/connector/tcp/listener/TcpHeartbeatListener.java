package com.htstar.ovms.device.gateway.core.connector.tcp.listener;

import cn.hutool.core.util.StrUtil;
import com.htstar.ovms.common.core.constant.SecurityConstants;
import com.htstar.ovms.device.api.constant.DeviceOnlineConstant;
import com.htstar.ovms.device.api.feign.DeviceLastDataFeign;
import com.htstar.ovms.device.gateway.core.connector.Session;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionEvent;
import com.htstar.ovms.device.gateway.core.connector.api.listener.SessionListener;
import com.htstar.ovms.device.gateway.core.connector.tcp.TcpSessionManager;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description: 心跳监听器
 * Author: flr
 * Company: 航通星空
 * Modified By:
 */
@Slf4j
public class TcpHeartbeatListener implements Runnable, SessionListener {

    private DeviceLastDataFeign deviceLastDataFeign;

    private TcpSessionManager tcpSessionManager = null;

    private ReentrantLock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();

    private int checkPeriod = 30 * 1000;
    private volatile boolean stop = false;

    public TcpHeartbeatListener(TcpSessionManager tcpSessionManager,DeviceLastDataFeign deviceLastDataFeign) {
        this.tcpSessionManager = tcpSessionManager;
        this.deviceLastDataFeign = deviceLastDataFeign;
    }

    @Override
    public void run() {
        while (!stop) {
            if (isEmpty()) {
                awaitQueue();
            }
            log.debug("心跳监听器监听到在线设备数: " + tcpSessionManager.getSessionCount());
            // sleep period
            try {
                Thread.sleep(checkPeriod);
            } catch (InterruptedException e) {
                log.error("心跳监听器运行时异常!", e);
            }
            // is stop
            if (stop) {
                break;
            }
            // 检测在线用户，多久没有发送心跳，超过规定时间的删除掉
            checkHeartBeat();
        }
    }

    public void checkHeartBeat() {
        Session[] sessions = tcpSessionManager.getSessions();
        for (Session session : sessions) {
            if (session.expire()) {
                session.close();
                if (StrUtil.isNotBlank(session.getDeviceSn())){
                    deviceLastDataFeign.heartBeatOnline(session.getDeviceSn(), DeviceOnlineConstant.OFFLINE, SecurityConstants.FROM_IN);
                }
                log.info("[{}]心跳超时，关闭设备连接:" , session.getDeviceSn());
            }
        }
    }

    private boolean isEmpty() {
        return tcpSessionManager.getSessionCount() == 0;
    }

    private void awaitQueue() {
        boolean flag = lock.tryLock();
        if (flag) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
                log.error("TcpHeartbeatListener awaitQueue occur InterruptedException!", e);
            } catch (Exception e) {
                log.error("await Thread Queue error!", e);
            } finally {
                lock.unlock();
            }
        }
    }

    private void signalQueue() {
        boolean flag = false;
        try {
            flag = lock.tryLock(100, TimeUnit.MILLISECONDS);
            if (flag){
                notEmpty.signalAll();
            }

        } catch (InterruptedException e) {
            log.error("TcpHeartbeatListener signalQueue occur InterruptedException!", e);
        } catch (Exception e) {
            log.error("signal Thread Queue error!", e);
        } finally {
            if (flag){
                lock.unlock();
            }

        }
    }

    public void stop() {
        this.stop = true;
    }

    @Override
    public void sessionCreated(SessionEvent se) {
        signalQueue();
    }

    @Override
    public void sessionDestroyed(SessionEvent se) {
    }

}
