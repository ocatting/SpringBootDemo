package com.sync.core.exception;

/**
 * @Description: 远程调用异常
 * @Author: Yan XinYu
 * @Date: 2021-04-13 9:39
 */
public class RemoteWriteException extends RuntimeException {

    public RemoteWriteException() {
        super();
    }

    public RemoteWriteException(String message) {
        super(message);
    }

    public RemoteWriteException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemoteWriteException(Throwable cause) {
        super(cause);
    }

    protected RemoteWriteException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
