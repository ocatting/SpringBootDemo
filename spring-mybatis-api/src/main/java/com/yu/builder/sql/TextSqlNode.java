package com.yu.builder.sql;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class TextSqlNode implements SqlNode{
    private final String text;

    public TextSqlNode(String text) {
        this.text = text;
    }

    @Override
    public boolean apply(DynamicContext context) {
        return true;
    }
}
