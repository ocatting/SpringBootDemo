package com.project.mybatis.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public final class DynamicDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();

    public static String getContextHolder() {
        return CONTEXT_HOLDER.get();
    }

    public static void setContextHolder(String dbType){
        CONTEXT_HOLDER.set(dbType);
    }

    public static void clearDataSource() {
        CONTEXT_HOLDER.remove();
    }

    public DynamicDataSource(DataSource dataSource, Map<Object,Object> target){
        super.setDefaultTargetDataSource(dataSource);
        super.setTargetDataSources(target);
        super.afterPropertiesSet();
    }

    public enum Db {

        MASTER("master"),
        SLAVE("slave");

        private String value;

        Db(String value){
            this.value = value;
        }

        public String getValue(){
            return this.value;
        }
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getContextHolder();
    }
}
