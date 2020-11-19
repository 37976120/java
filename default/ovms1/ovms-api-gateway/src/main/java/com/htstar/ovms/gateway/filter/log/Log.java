package com.htstar.ovms.gateway.filter.log;

import com.htstar.ovms.common.core.util.R;
import com.htstar.ovms.gateway.util.IpUtils;
import lombok.Data;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * 日志实体类，方便后续接入ELK
 */
@Data
public class Log {
    private Log.TYPE logType;
    private Log.LEVEL level;
    private String ip;
    private Long handleTime;
    private String timeStamp;
    private String requestUrl;
    private String requestBody;
    private R r;
    private String requestId;
    private String requestMethod;
    private Integer status;
    private String serverIp;
    private String sessionId;

    public Log() {
        this(Log.TYPE.REQUEST);
    }

    public Log(Log.TYPE logType) {
        this.logType = logType;
        this.timeStamp = ZonedDateTime.now(ZoneOffset.of("+08:00")).toString();
        this.serverIp = IpUtils.getLocalIp();
    }

    /**
     * 日志级别枚举类
     */
    public static enum LEVEL {
        OFF,
        ERROR,
        WARN,
        INFO,
        DEBUG,
        TRACE,
        ALL;

        private LEVEL() {
        }
    }

    /**
     * 日志类型枚举
     */
    public static enum TYPE {
        REQUEST,
        RESPONSE,
        OUT;

        private TYPE() {
        }
    }
}
