package com.demo.cache.redis.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class User {

    private long id;
    private String name;
    private String password;
    private String desc;
    private LocalDateTime createTime;
    private List<String> list;

}
