package com.yu.builder.sql;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface SqlNode {

    boolean apply(DynamicContext context);
}
