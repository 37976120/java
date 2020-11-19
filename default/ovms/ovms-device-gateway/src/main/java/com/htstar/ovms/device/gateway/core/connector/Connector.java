package com.htstar.ovms.device.gateway.core.connector;

public interface Connector<T> {

    void send(String deviceSn, T message) throws Exception;

    boolean exist(String deviceSn) throws Exception;

}
