package com.yu.builder.sql;

import java.util.Arrays;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class WhereSqlNode extends TrimSqlNode {

    private static List<String> prefixList = Arrays.asList("AND ","OR ","AND\n", "OR\n", "AND\r", "OR\r", "AND\t", "OR\t");

    public WhereSqlNode(SqlNode contents) {
        super(contents, "WHERE", prefixList, null, null);
    }
}
