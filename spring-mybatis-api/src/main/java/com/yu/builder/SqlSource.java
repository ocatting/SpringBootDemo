package com.yu.builder;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface SqlSource {

    BoundSql getBoundSql(Object parameterObject);

}
