package com.yu.config;

import com.yu.domain.ThemeFolder;
import com.yu.domain.UrlSqlSource;
import com.yu.domain.UrlStore;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 映射参数
 * @Author Yan XinYu
 **/
public class MappingConfiguration {

    /**
     * 映射文件夹地址
     */
    private static final Map<String, ThemeFolder> mappingThemeFolder = new ConcurrentHashMap<>();

    /**
     * 映射： id 的 sql 关系
     */
    private static final Map<String, UrlStore> mappingSql = new ConcurrentHashMap<>();

    /**
     * 映射 解析后 SQL 关系
     */
    private static final Map<String, UrlSqlSource> paramAndSqlSource = new ConcurrentHashMap<>();

    /**
     * 映射 url 列表对应 SQL 关系地址
     */
    private static final Map<String, String> uriAndSqlSource = new ConcurrentHashMap<>();

    public void put(String id,UrlStore urlStore){
        mappingSql.put(id,urlStore);
    }

    public UrlStore get(String id){
        return mappingSql.get(id);
    }

    public UrlStore getSqlByURI(String uri){
        return mappingSql.get(uri);
    }

    public void delete(String id){
        mappingSql.remove(id);
    }

    public void addSqlMapper(String id, UrlSqlSource urlSqlSource){
        paramAndSqlSource.put(id,urlSqlSource);
    }

    public UrlSqlSource getSqlMapper(String id){
       return paramAndSqlSource.get(id);
    }

    public void addThemeFolder(String id, ThemeFolder themeFolder){
        mappingThemeFolder.put(id,themeFolder);
    }

    public ThemeFolder getThemeFolder(String id){
        return mappingThemeFolder.get(id);
    }

    public void addUri(String uri, String urlSqlSourceId){
        uriAndSqlSource.put(uri,urlSqlSourceId);
    }

    public String getUri(String uri){
        return uriAndSqlSource.get(uri);
    }

}
