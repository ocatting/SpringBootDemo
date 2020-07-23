package com.yu.builder;

import com.yu.builder.sql.DynamicContext;
import com.yu.builder.sql.SqlNode;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class DynamicSqlSource implements SqlSource{

    private final SqlNode rootSqlNode;

    public DynamicSqlSource(SqlNode node){
        this.rootSqlNode=node;
    }
    @Override
    public BoundSql getBoundSql(Object parameterObject) {
        DynamicContext context = new DynamicContext(parameterObject);
        rootSqlNode.apply(context);
        SqlSourceBuilder sqlSourceBuilder = new SqlSourceBuilder();
        SqlSource sqlSource = sqlSourceBuilder.parse(context.getSql());
        return sqlSource.getBoundSql(parameterObject);
    }
}
