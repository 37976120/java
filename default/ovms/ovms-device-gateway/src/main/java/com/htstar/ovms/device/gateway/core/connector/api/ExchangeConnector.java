package com.htstar.ovms.device.gateway.core.connector.api;

import com.htstar.ovms.device.gateway.core.connector.Connector;
import com.htstar.ovms.device.gateway.core.connector.Session;
import com.htstar.ovms.device.gateway.core.connector.SessionManager;
import com.htstar.ovms.device.gateway.core.exception.DispatchException;
import com.htstar.ovms.device.gateway.core.exception.PushException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ExchangeConnector<T> implements Connector<T> {

    public void send(SessionManager sessionManager, String deviceSn, T message) throws Exception {
        Session session = sessionManager.getSession(deviceSn);
        if (session == null) {
            throw new Exception(String.format("session %s no exist.", deviceSn));
        }
        try {
            session.getConnection().send(message);
            session.access();
        } catch (PushException e) {
            log.error("ExchangeConnector send occur PushException.", e);
            session.close();
            throw new DispatchException(e);
        } catch (Exception e) {
            log.error("ExchangeConnector send occur Exception.", e);
            session.close();
            throw new DispatchException(e);
        }
    }
}
