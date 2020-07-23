package com.yu.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class User {

    @Id
    private Integer id;
    private String name;
    private String password;
    private String role;
}
