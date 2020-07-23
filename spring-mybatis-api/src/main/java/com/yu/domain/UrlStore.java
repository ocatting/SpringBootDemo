package com.yu.domain;

import com.yu.builder.SqlSource;
import com.yu.parsing.ParameterMapping;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class UrlStore {

    private String id;

    private String name;

    // uri address
    private String uri;

    // method name
    private String methodName;

    // sample param
    private String sampleParam;

    // 动态表达式SQL
    private String schemaSQL;

    // 指定解析SQL源地址
    private String urlSqlSourceId;

}
