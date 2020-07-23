package com.yu.builder.sql;

import com.yu.util.ExpressionEvaluator;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class IfSqlNode implements SqlNode{

    private final String test;
    private final SqlNode contents;
    private final ExpressionEvaluator evaluator;

    public IfSqlNode(SqlNode contents, String test) {
        this.test = test;
        this.contents = contents;
        this.evaluator = new ExpressionEvaluator();
    }

    @Override
    public boolean apply(DynamicContext context) {
        if (evaluator.evaluateBoolean(test, context.getBindings())) {
            contents.apply(context);
            return true;
        }
        return false;
    }
}
