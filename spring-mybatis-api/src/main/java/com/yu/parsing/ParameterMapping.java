package com.yu.parsing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @Description: 参数映射数据库表中字段类型关系
 * @Author Yan XinYu
 **/
@Data
@Builder
@AllArgsConstructor
public class ParameterMapping {

    private String property;
    private Class<?> javaType = String.class;
    private String javaTypeName = "string";

}
