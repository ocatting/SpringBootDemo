package com.project.mybatisplus.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Getter
@Setter
public class ResponseMsg<T> {

    private int code;
    private T data;
    private String msg;

    public ResponseMsg(T data){
        this.data = data;
    }

}
