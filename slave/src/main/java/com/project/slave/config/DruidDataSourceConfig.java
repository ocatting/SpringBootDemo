package com.project.slave.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
public class DruidDataSourceConfig {

    /**
     * 分页拦截器
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.master")
    public DataSource dataSourceMaster(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.slave")
    public DataSource dataSourceSlave(){
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicDataSource multipleDataSource(DataSource dataSourceMaster,DataSource dataSourceSlave){
        Map<Object, Object> target = new HashMap<>(2);
        target.put(DynamicDataSource.Db.MASTER.getValue(),dataSourceMaster);
        target.put(DynamicDataSource.Db.SLAVE.getValue(),dataSourceSlave);
        return new DynamicDataSource(dataSourceMaster,target);
    }

}
