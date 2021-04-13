package com.sync.common.exception;

/**
 * @Description: 业务异常
 * @Author: Yan XinYu
 * @Date: 2021-03-16 20:10
 */
public class ServiceException extends RuntimeException {

    public ServiceException() {
        super();
    }

    public ServiceException(String s) {
        super(s);
    }

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceException(Throwable cause) {
        super(cause);
    }
}
