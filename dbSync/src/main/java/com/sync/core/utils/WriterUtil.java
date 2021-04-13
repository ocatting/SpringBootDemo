package com.sync.core.utils;

import com.alibaba.druid.sql.parser.ParserException;
import com.sync.core.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 数据库写工具
 */
public final class WriterUtil {

    private static final Logger LOG = LoggerFactory.getLogger(WriterUtil.class);

    public static final String UPDATE_MODE = "update";
    public static final String INSERT_MODE = "insert";

    public static List<String> renderPreOrPostSqls(List<String> preOrPostSqls, String tableName) {
        if (null == preOrPostSqls) {
            return Collections.emptyList();
        }

        List<String> renderedSqls = new ArrayList<String>();
        for (String sql : preOrPostSqls) {
            //preSql为空时，不加入执行队列
            if (StringUtils.isNotBlank(sql)) {
                renderedSqls.add(sql.replace(CommonConstant.TABLE_NAME_PLACEHOLDER, tableName));
            }
        }

        return renderedSqls;
    }

    public static void executeSqls(Connection conn, List<String> sqls, String basicMessage,DataBaseType dataBaseType) {
        Statement stmt = null;
        String currentSql = null;
        try {
            stmt = conn.createStatement();
            for (String sql : sqls) {
                currentSql = sql;
                DBUtil.executeSqlWithoutResultSet(stmt, sql);
            }
        } catch (Exception e) {
            throw new RuntimeException(dataBaseType+":"+currentSql,e);
        } finally {
            DBUtil.closeDBResources(null, stmt, null);
        }
    }

    public static String getWriteTemplate(List<String> columnHolders, String writeMode, String table,String dataBaseType) {
        boolean isWriteModeLegal = writeMode.trim().toLowerCase().startsWith("insert")
                || writeMode.trim().toLowerCase().startsWith("replace")
                || writeMode.trim().toLowerCase().startsWith("update");

        if(StringUtils.isBlank(table)){
            throw new RuntimeException(
                    String.format("您所配置的 tableName:%s 是空 ,请您设置",table));
        }

        if (!isWriteModeLegal) {
            throw new RuntimeException(
                    String.format("您所配置的 writeMode:%s 错误. 因为目前仅支持replace,update 或 insert 方式. 请检查您的配置并作出修改.", writeMode));
        }

        List<String> valueHolders = new ArrayList<>(columnHolders.size());
        for (int i = 0; i < columnHolders.size(); i++) {
            valueHolders.add("?");
        }

        // && writeMode.trim().toLowerCase().startsWith("replace")
        String writeDataSqlTemplate;
        if (((dataBaseType.equals(DataBaseType.MySql.getTypeName())) && writeMode.trim().toLowerCase().startsWith(UPDATE_MODE))) {
            writeDataSqlTemplate = new StringBuilder().append("REPLACE INTO `").append(table).append("` (").append(StringUtils.join(columnHolders, ","))
                    .append(") VALUES(").append(StringUtils.join(valueHolders, ","))
                    .append(")")
                    .append(onDuplicateKeyUpdateString(columnHolders))
                    .toString();
        } else {

            //这里是保护,如果其他错误的使用了update,需要更换为replace
            if (writeMode.trim().toLowerCase().startsWith(UPDATE_MODE)) {
                writeMode = "replace";
            }
            writeDataSqlTemplate = new StringBuilder().append(writeMode.toUpperCase()).append(" INTO `").append(table).append("` (").append(StringUtils.join(columnHolders, ","))
                    .append(") VALUES(").append(StringUtils.join(valueHolders, ","))
                    .append(")").toString();
        }

        return writeDataSqlTemplate;
    }

    public static String getWriteTemplate(List<String> columns, Map<String,String> mapping, String writeMode,String table, String dataBaseType) {
        boolean isWriteModeLegal = writeMode.trim().toLowerCase().startsWith("insert")
                || writeMode.trim().toLowerCase().startsWith("replace")
                || writeMode.trim().toLowerCase().startsWith("update");

        if(StringUtils.isBlank(table)){
            throw new RuntimeException(
                    String.format("您所配置的 tableName:%s is empty ,请您设置",table));
        }

        if (!isWriteModeLegal) {
            throw new RuntimeException(
                    String.format("您所配置的 writeMode:%s 错误. 因为目前仅支持replace,update 或 insert 方式. 请检查您的配置并作出修改.", writeMode));
        }

        List<String> columnHolders = new ArrayList<>(columns.size());
        for (String column : columns) {
            if(mapping == null || mapping.isEmpty()){
                columnHolders.add(column);
                continue;
            }
            columnHolders.add(mapping.getOrDefault(column,column));
        }

        List<String> valueHolders = new ArrayList<>(columns.size());
        for (int i = 0; i < columnHolders.size(); i++) {
            valueHolders.add("?");
        }

        // && writeMode.trim().toLowerCase().startsWith("replace")
        String writeDataSqlTemplate;
        if (((dataBaseType.equals(DataBaseType.MySql.getTypeName())) && writeMode.trim().toLowerCase().startsWith(UPDATE_MODE))) {
            writeDataSqlTemplate = new StringBuilder().append("REPLACE INTO `").append(table).append("` (").append(StringUtils.join(columnHolders, ","))
                    .append(") VALUES(").append(StringUtils.join(valueHolders, ","))
                    .append(")")
                    .append(onDuplicateKeyUpdateString(columnHolders))
                    .toString();
        } else {

            //这里是保护,如果其他错误的使用了update,需要更换为replace
            if (writeMode.trim().toLowerCase().startsWith(UPDATE_MODE)) {
                writeMode = "replace";
            }
            writeDataSqlTemplate = new StringBuilder().append(writeMode.toUpperCase()).append(" INTO `").append(table).append("` (").append(StringUtils.join(columnHolders, ","))
                    .append(") VALUES(").append(StringUtils.join(valueHolders, ","))
                    .append(")").toString();
        }

        return writeDataSqlTemplate;
    }

    public static String onDuplicateKeyUpdateString(List<String> columnHolders){
        if (columnHolders == null || columnHolders.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" ON DUPLICATE KEY UPDATE ");
        boolean first = true;
        for(String column:columnHolders){
            if(!first){
                sb.append(",");
            }else{
                first = false;
            }
            sb.append(column);
            sb.append("=VALUES(");
            sb.append(column);
            sb.append(")");
        }

        return sb.toString();
    }

    public static void preCheckPrePareSQL(String table,String type) {

        List<String> preSqls = new ArrayList<>(0);
        List<String> renderedPreSqls = WriterUtil.renderPreOrPostSqls(preSqls, table);
        if (!renderedPreSqls.isEmpty()) {
            LOG.info("Begin to preCheck preSqls:[{}].",StringUtils.join(renderedPreSqls, ";"));
            for(String sql : renderedPreSqls) {
                try{
                    DBUtil.sqlValid(sql, type);
                }catch(ParserException e) {
                    throw new RuntimeException(sql,e);
                }
            }
        }
    }

    public static void preCheckPostSQL(String table, String type) {

        List<String> preSqls = new ArrayList<>(0);
        List<String> renderedPostSqls = WriterUtil.renderPreOrPostSqls(preSqls, table);
        if (!renderedPostSqls.isEmpty()) {
            LOG.info("Begin to preCheck postSqls:[{}].",StringUtils.join(renderedPostSqls, ";"));
            for(String sql : renderedPostSqls) {
                try{
                    DBUtil.sqlValid(sql, type);
                }catch(ParserException e){
                    throw new RuntimeException(sql,e);
                }
            }
        }
    }

}
