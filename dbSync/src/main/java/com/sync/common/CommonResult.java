package com.sync.common;

/**
 * 通用返回对象
 * @author yanxinyu
 */
public class CommonResult<T> implements java.io.Serializable {

    public static final int SUCCESS_CODE = 200;
    public static final int FAIL_CODE = 500;

    private int code;
    private String message;
    private T data;

    protected CommonResult() {}

    protected CommonResult(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功返回结果
     */
    public static <T> CommonResult<T> success() {
        return new CommonResult<T>(SUCCESS_CODE,  null, null);
    }

    /**
     * 成功返回结果
     * @param data 获取的数据
     */
    public static <T> CommonResult<T> success(T data) {
        return new CommonResult<T>(SUCCESS_CODE,  null, data);
    }

    /**
     * 成功返回结果
     *
     * @param data 获取的数据
     * @param  message 提示信息
     */
    public static <T> CommonResult<T> success(T data, String message) {
        return new CommonResult<T>(SUCCESS_CODE, message, data);
    }

    /**
     * 失败返回结果
     * @param code 提示码
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(Integer code,String message) {
        return new CommonResult<T>(code, message, null);
    }

    /**
     * 失败返回结果
     * @param message 提示信息
     */
    public static <T> CommonResult<T> failed(String message) {
        return new CommonResult<T>(FAIL_CODE, message, null);
    }

    /**
     * 失败返回结果
     */
    public static <T> CommonResult<T> failed() {
        return failed( FAIL_CODE,null);
    }

    public final boolean isSuccess(){
        return SUCCESS_CODE == getCode();
    }

    public long getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
