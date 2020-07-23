package com.yu.distace.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.geo.Point;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class User {

    @Id
    private Integer id;
    private String name;
    private String sex;

    //必须强制命名为 location
    private Point location;

    public User(){}

    public User(Integer id,String name,String sex,Point location){
        this.id = id;
        this.name = name;
        this.sex = sex;
        this.location = location;
    }

}
