package com.yu.builder.sql;


/**
 * @Description:
 * @Author Yan XinYu
 **/
public class VarDeclSqlNode implements SqlNode {

    private final String name;
    private final String expression;

    public VarDeclSqlNode(String var, String exp) {
        name = var;
        expression = exp;
    }

    @Override
    public boolean apply(DynamicContext context) {
        return false;
    }
}
