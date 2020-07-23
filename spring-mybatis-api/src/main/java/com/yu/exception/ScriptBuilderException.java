package com.yu.exception;

/**
 * @Description:  Script 构建异常
 * @Author Yan XinYu
 **/
public class ScriptBuilderException extends RuntimeException{

    private static final long serialVersionUID = -3885164021020443281L;

    public ScriptBuilderException() {
        super();
    }

    public ScriptBuilderException(String message) {
        super(message);
    }

    public ScriptBuilderException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScriptBuilderException(Throwable cause) {
        super(cause);
    }
}
