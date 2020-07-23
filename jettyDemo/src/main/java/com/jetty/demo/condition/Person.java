package com.jetty.demo.condition;

import lombok.Data;
import lombok.ToString;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@ToString
public class Person {

    private Integer id;
    private String name;

    public Person(Integer id,String name){
        this.id = id;
        this.name = name;
    }

}
