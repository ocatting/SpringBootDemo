package com.yu.security.common;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@lombok.Data
public class Result {

    private Integer code;
    private Data data;
    private String info;

    public Result(Integer code,String info){
        this.code = code;
        this.info = info;
    }

    public Result(Integer code,Data data,String info){
        this.code = code;
        this.data = data;
        this.info = info;
    }

    public static Result fair(){
        return new Result(500,"网络异常");
    }

    public static Result fair(String info){
        return new Result(500,info);
    }

    public static Result success(){
        return new Result(200,"success");
    }

    public static Result success(String info){
        return new Result(200,info);
    }

    public static Result success(Data data,String info){
        return new Result(200,data,info);
    }

}
