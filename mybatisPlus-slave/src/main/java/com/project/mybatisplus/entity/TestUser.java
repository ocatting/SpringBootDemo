package com.project.mybatisplus.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class TestUser {

    private Integer id;
    private String name;
    private BigDecimal fee;
    private List<String> list;
    private Map<String,Object> map;
    private LocalDateTime time;

}
