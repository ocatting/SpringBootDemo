package com.sync.core.constant;

/**
 * @Description: 常量池
 * @Author: Yan XinYu
 * @Date: 2021-03-03 21:14
 */
public class CommonConstant {

    /**
     * 超时时间
     */
    public static final int TIMEOUT_SECONDS = 15;
    /**
     * 最大重试次数
     */
    public static final int MAX_TRY_TIMES = 4;
    /**
     * 最大SOCKET超时时间
     */
    public static final int SOCKET_TIMEOUT_IN_SECOND = 172800;

    /**
     * model 单独模式
     */
    public static final String STANDALONE_MODEL = "StandAlone";

    /**
     * 数据库类型
     */
    public static final String DB_TYPE_MYSQL = "mysql";
    public static final String DB_TYPE_PGSQL = "postgres";

    /**
     * 表名占位符
     */
    public static String TABLE_NAME_PLACEHOLDER = "@table";

    /**
     * insert占位符
     */
    public static String INSERT_OR_REPLACE_TEMPLATE_MARK = "insertOrReplaceTemplate";
}
