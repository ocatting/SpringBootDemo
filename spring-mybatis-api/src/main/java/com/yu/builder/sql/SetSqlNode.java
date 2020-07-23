package com.yu.builder.sql;

import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class SetSqlNode extends TrimSqlNode {

    private static final List<String> COMMA = Collections.singletonList(",");

    public SetSqlNode(SqlNode contents) {
        super(contents, "SET", COMMA, null, COMMA);
    }
}
