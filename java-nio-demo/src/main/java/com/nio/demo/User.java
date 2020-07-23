package com.nio.demo;

import lombok.Data;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class User {

    public User(String name,int age){
        this.name=name;
        this.age=age;
    }

    private String name;
    private int age;

}
