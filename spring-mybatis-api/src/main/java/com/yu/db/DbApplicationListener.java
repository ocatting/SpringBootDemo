package com.yu.db;

import com.yu.util.ConvertUtils;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.context.properties.source.ConfigurationPropertyName;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 解析 external.db 配置参数，与ExternalDb枚举唯一对应
 * @Author Yan XinYu
 **/
//@Configuration
public class DbApplicationListener extends DbConfig implements ApplicationListener<ContextRefreshedEvent> {

    private static final ConfigurationPropertyName EXTERNAL_DB = ConfigurationPropertyName
            .of("external.db");

    private static final Bindable<Map<String, String>> STRING_STRING_MAP = Bindable
            .mapOf(String.class, String.class);

    private static final Map<ExternalDb, DbConfig> MYSELF_DB_DB_CONFIG_MAP = new HashMap<>();

    private static final Map<ExternalDb, DataSource> MYSELF_DB_DATASOURCE_MAP = new HashMap<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Environment environment =  event.getApplicationContext().getEnvironment();
        Binder binder = Binder.get(environment);
        Map<String, String> dbs = binder.bind(EXTERNAL_DB, STRING_STRING_MAP)
                .orElseGet(Collections::emptyMap);
        dbs.forEach((name, value) -> bind(name,value));
    }

    private void bind(String name,String value){
        if(StringUtils.isEmpty(name)|| StringUtils.isEmpty(value)){
            return;
        }
        for (ExternalDb db:ExternalDb.values()) {
            if(name.startsWith(db.name())){
                String[] key = name.split("\\.");
                if(key.length != 2){
                    throw new IllegalArgumentException(
                            "springboot properties 中'external.db'参数解析错误，可能参数的格式存在问题，请检查参数:" + name);
                }
                doBind(db,key[1],value);
            }
        }
    }

    private void doBind(ExternalDb db,String fieldName,String fieldValue){
        DbConfig dbConfig = MYSELF_DB_DB_CONFIG_MAP.get(db);
        if(dbConfig == null){
            dbConfig = new DbConfig();
        }
        ConvertUtils.copyProperties(fieldName,fieldValue,dbConfig);
        MYSELF_DB_DB_CONFIG_MAP.put(db,dbConfig);
    }

    /**
     * 获取配置信息
     * @param db
     * @return
     */
    public DbConfig getConfig(ExternalDb db){
        return MYSELF_DB_DB_CONFIG_MAP.get(db);
    }

    /**
     * 获取数据源
     * @param db
     * @return
     */
    public DataSource getDataSource(ExternalDb db) {
        DataSource dataSource = MYSELF_DB_DATASOURCE_MAP.get(db);
        if (dataSource != null) {
            return dataSource;
        }
        synchronized (this) {
            if (dataSource == null) {
                dataSource = DbUtil.getDataSource(getConfig(db));
                MYSELF_DB_DATASOURCE_MAP.put(db, dataSource);
            }
            return dataSource;
        }
    }
}
