package com.yu.mongodb.Model;

import lombok.Data;

/**
 * @Description: 返回信息
 * @Author Yan XinYu
 **/
@Data
public class ResponseResult {

    private int code;
    private Object data;
    private String info;

    public ResponseResult(){

    }

    public ResponseResult(int code,Object data,String info){
        this.code = code;
        this.data = data;
        this.info = info;
    }

    public static final ResponseResult success(Object data){
        return new ResponseResult(200,data,"success");
    }

    public static final ResponseResult fail(Object data){
        return new ResponseResult(500,data,"fail");
    }


}
