package com.sync.core.exception;

/**
 * @Description: 远程调用异常
 * @Author: Yan XinYu
 * @Date: 2021-04-13 9:39
 */
public class RemoteInvokeException extends RuntimeException {

    public RemoteInvokeException() {
        super();
    }

    public RemoteInvokeException(String message) {
        super(message);
    }

    public RemoteInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteInvokeException(Throwable cause) {
        super(cause);
    }

    protected RemoteInvokeException(String message, Throwable cause,
                               boolean enableSuppression,
                               boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
