package com.htstar.ovms.common.security.exception;

/**
 * Description:
 * Author: flr
 * Date: Created in 2020/7/15
 * Company: 航通星空
 * Modified By:
 */
public class OvmsLoginException extends Exception {

    private static final long serialVersionUID = 1L;

    public OvmsLoginException(String msg) {
        super(msg);
    }

    public OvmsLoginException(String msg, Throwable t) {
        super(msg, t);
    }
}
