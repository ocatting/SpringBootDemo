package com.yu.security.entity;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
@ToString
public class TUser {

    private Integer id;
    private String username;
    private String password;
    private Integer age;
    private String email;

    private List<Privilege> privileges = new ArrayList<>();


}
