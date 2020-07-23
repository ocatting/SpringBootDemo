package com.yu.builder.sql;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class ForEachSqlNode implements SqlNode {

    public static final String ITEM_PREFIX = "__frch_";

    private final String collectionExpression;
    private final SqlNode contents;
    private final String open;
    private final String close;
    private final String separator;
    private final String item;
    private final String index;

    public ForEachSqlNode(SqlNode contents, String collectionExpression, String index, String item, String open, String close, String separator) {
        this.collectionExpression = collectionExpression;
        this.contents = contents;
        this.open = open;
        this.close = close;
        this.separator = separator;
        this.index = index;
        this.item = item;
    }

    @Override
    public boolean apply(DynamicContext context) {
        return false;
    }
}
