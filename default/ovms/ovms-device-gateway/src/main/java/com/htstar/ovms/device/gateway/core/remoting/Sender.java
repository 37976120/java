package com.htstar.ovms.device.gateway.core.remoting;

import com.htstar.ovms.device.api.wrapper.MessageWrapper;

public interface Sender {

    void sendMessage(MessageWrapper wrapper) throws RuntimeException;

    boolean existSession(MessageWrapper wrapper) throws RuntimeException;
}
