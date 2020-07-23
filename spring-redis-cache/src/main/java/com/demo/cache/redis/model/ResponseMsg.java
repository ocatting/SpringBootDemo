package com.demo.cache.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@AllArgsConstructor
public class ResponseMsg {

    private int code;
    private Object data;
    private String msg;

    public static ResponseMsg success(Object data){
        return new ResponseMsg(200, data, "success");
    }

    public static ResponseMsg fail(Object data){
        return new ResponseMsg(500, data, "fail");
    }
}
