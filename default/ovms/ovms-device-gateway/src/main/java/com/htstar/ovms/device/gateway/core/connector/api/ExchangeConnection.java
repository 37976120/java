package com.htstar.ovms.device.gateway.core.connector.api;

import com.htstar.ovms.device.gateway.core.connector.Connection;
import com.htstar.ovms.device.gateway.core.connector.Session;


public abstract class ExchangeConnection<T> implements Connection<T> {

    protected Session session = null;
    protected String connectionId = null;

    protected volatile boolean close = false;
    protected int connectTimeout = 60 * 60 * 1000; // ms

    public void fireError(RuntimeException e) {
        throw e;
    }

    public boolean isClosed() {
        return close;
    }

    @Override
    public void setConnectionId(String connectionId) {
        this.connectionId = connectionId;
    }

    @Override
    public String getConnectionId() {
        return connectionId;
    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public Session getSession() {
        return session;
    }

}
