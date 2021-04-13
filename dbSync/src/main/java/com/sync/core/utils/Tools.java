package com.sync.core.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 工具
 */
public class Tools {

    public static String buildInsertSql(String table, String[] fields) {
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO ").append(table).append(" (");
        sb.append(String.join(", ", fields));
        sb.append(") ");
        sb.append("VALUES (");
        for (int i = 0; i < fields.length; i++) {
            if (i == fields.length - 1) {
                sb.append("?").append(")");
                break;
            }
            sb.append("?").append(", ");
        }
        return sb.toString();
    }

    /**
     * * 读取json文件，返回json串
     *
     * @param fileName 文件名
     * @return 文件内容
     */
    public static String readJsonFile(String fileName) throws IOException {
        if(fileName == null){
            throw new RuntimeException("未指定配置文件位置");
        }
        String jsonStr = "";
        File jsonFile = new File(fileName);

        Reader reader = new InputStreamReader(new FileInputStream(jsonFile), StandardCharsets.UTF_8);
        int ch ;
        StringBuffer sb = new StringBuffer();
        while ((ch = reader.read()) != -1) {
            sb.append((char) ch);
        }
        reader.close();
        jsonStr = sb.toString();
        return jsonStr;
    }


}