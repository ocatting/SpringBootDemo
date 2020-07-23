package com.yu.db;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @Description: 外部db数据库配置参数
 * @Author Yan XinYu
 **/
@Data
@Valid
public class DbConfig {

    @NotBlank
    private String jdbcUrl;
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String driverClassName;

    private Integer minIdle = 0;
    private Integer maxPoolSize = 20;
    private Long idleTimeout = 5000L;
    private Long maxLifetime = 12000000L;

}
