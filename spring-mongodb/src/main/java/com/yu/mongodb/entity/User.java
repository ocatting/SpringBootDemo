package com.yu.mongodb.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class User implements Serializable {

    @Id
    private String _id;

    private int id;
    private String name;
    private String sex;
}
