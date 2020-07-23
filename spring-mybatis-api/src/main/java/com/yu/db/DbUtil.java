package com.yu.db;

import com.yu.util.ConvertUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;

/**
 * @Description: Hikari 连接数据库
 * org.postgresql.Driver
 * com.mysql.cj.jdbc.Driver
 * @Author Yan XinYu
 **/
public class DbUtil {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public static void validate(Object obj) {
        Set<ConstraintViolation<Object>> set = validator.validate(obj, Default.class);
        if (set != null && set.size() != 0) {
            for (ConstraintViolation<Object> cv : set) {
                throw new IllegalArgumentException(cv.getPropertyPath().toString() + cv.getMessage());
            }
        }
    }

    /**
     * 配置数据源信息
     * @return
     */
    public static DataSource getDataSource(DbConfig config){
        validate(config);
        HikariConfig conf = new HikariConfig();
        ConvertUtils.copyProperties(config,conf);
        return new HikariDataSource(conf);
    }

}
