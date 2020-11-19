package com.htstar.ovms.device.api.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class MessageWrapper implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 消息类型
     */
    private MessageType messageType;

    /**
     * 设备序列号
     */
    private String deviceSn;

    /**
     * 指令序列号（平台持久化的唯一序列号）
     *
     * 登录回包时：-1代表设备部允许接入平台
     */
    private Long seq;

    /**
     * 回包
     */
    private byte[] body;

    public MessageWrapper(MessageType messageType, String deviceSn, byte[] body) {
        this.messageType = messageType;
        this.deviceSn = deviceSn;
        this.body = body;
    }

    public MessageWrapper(String deviceSn, MessageType messageType, Long seq, byte[] body) {
        this.messageType = messageType;
        this.deviceSn = deviceSn;
        this.seq = seq;
        this.body = body;
    }

    public MessageWrapper(MessageType messageType, String deviceSn, byte[] body, long seq) {
        this.messageType = messageType;
        this.deviceSn = deviceSn;
        this.seq = seq;
        this.body = body;
        this.seq = seq;
    }

    public MessageWrapper(MessageType messageType, String deviceSn) {
        this.messageType = messageType;
        this.deviceSn = deviceSn;
    }


    public enum MessageType {
        CONNECT, CLOSE, HEART_BEAT, SEND , REPLY,COMMAND
    }

    public boolean isConnect() {
        return MessageType.CONNECT.equals(this.messageType);
    }

    public boolean isClose() {
        return MessageType.CLOSE.equals(this.messageType);
    }

    public boolean isHeartbeat() {
        return MessageType.HEART_BEAT.equals(this.messageType);
    }

    public boolean isSend() {
        return MessageType.SEND.equals(this.messageType);
    }


    public boolean isReply() {
        return MessageType.REPLY.equals(this.messageType);
    }

    public static MessageWrapper refuseLogin(String deviceSn, byte[] body){
        /**
         * -1代表设备部允许接入平台
         */
        return new MessageWrapper(MessageType.CONNECT,deviceSn,body,-1);
    }


    public static MessageWrapper login(String deviceSn, byte[] body){
        /**
         * -1代表设备部允许接入平台
         */
        return new MessageWrapper(MessageType.CONNECT,deviceSn,body);
    }

    public static MessageWrapper loginOut(String deviceSn, byte[] body){
        return new MessageWrapper(MessageType.CLOSE,deviceSn,body);
    }

    public static MessageWrapper heartBeat(String deviceSn, byte[] body){
        return new MessageWrapper(MessageType.HEART_BEAT,deviceSn,body);
    }

    /**
     * 普通下发
     * @param deviceSn
     * @param body
     * @return
     */
    public static MessageWrapper send(String deviceSn, byte[] body){
        return new MessageWrapper(MessageType.SEND,deviceSn,body);
    }


    /**
     * 普通下发
     * @param deviceSn
     * @param body
     * @return
     */
    public static MessageWrapper send(String deviceSn){
        return new MessageWrapper(MessageType.SEND,deviceSn);
    }


    /**
     * 指令回包
     * @param deviceSn
     * @param body
     * @return
     */
    public static MessageWrapper reply(String deviceSn,Long seq, byte[] body){
        return new MessageWrapper(deviceSn,MessageType.REPLY,seq,body);
    }



}
