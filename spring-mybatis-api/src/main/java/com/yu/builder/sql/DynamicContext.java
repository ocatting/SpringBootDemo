package com.yu.builder.sql;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class DynamicContext {

    private final StringBuilder sqlBuilder = new StringBuilder();
    private final Map bindings;

    public DynamicContext(Object parameterObject){
        this.bindings = (Map) parameterObject;
    }

    public Map<String, Object> getBindings() {
        return bindings;
    }

    public void appendSql(String sql) {
        sqlBuilder.append(sql);
        sqlBuilder.append(" ");
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }
}
