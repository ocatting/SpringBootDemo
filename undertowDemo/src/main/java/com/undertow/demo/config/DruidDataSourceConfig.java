package com.undertow.demo.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

@Configuration
@EnableConfigurationProperties(DruidDataSourceProperties.class)
public class DruidDataSourceConfig {

    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;


    @Bean
    public DataSource dataSource() throws SQLException {
        System.out.println(druidDataSourceProperties);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(druidDataSourceProperties.getUsername());
        druidDataSource.setPassword(druidDataSourceProperties.getPassword());
        druidDataSource.setUrl(druidDataSourceProperties.getJdbcUrl());
        druidDataSource.setDriverClassName(druidDataSourceProperties.getDriverClassName());
        druidDataSource.setInitialSize(druidDataSourceProperties.getInitialSize());
        druidDataSource.setMinIdle(druidDataSourceProperties.getMinIdle());
        druidDataSource.setMaxActive(druidDataSourceProperties.getMaxActive());
        druidDataSource.setMaxWait(druidDataSourceProperties.getMaxWait());
        druidDataSource.setFilters(druidDataSourceProperties.getFilters());
     return druidDataSource;
    }

    /**
     * 定义一个 mybatis 拦截器
     * @return
     */
    @Bean
    public MybatisPugInDemo mybatisPugInDemoInterceptor(){
        return new MybatisPugInDemo();
    }


    /**
     * 将注册好的 Configuration 中添加拦截器
     * @param mybatisPugInDemoInterceptor
     * @return
     */
    @Bean
    public ConfigurationCustomizer configurationCustomizer(MybatisPugInDemo mybatisPugInDemoInterceptor){
        return (c) -> {
            c.setMapUnderscoreToCamelCase(true);//设置驼峰命名规则
            c.addInterceptor(mybatisPugInDemoInterceptor);
        };

//        return new ConfigurationCustomizer() {
//            @Override
//            public void customize(org.apache.ibatis.session.Configuration configuration) {
//                configuration.setMapUnderscoreToCamelCase(true);//设置驼峰命名规则
//                configuration.addInterceptor(mybatisPugInDemoInterceptor());
//            }
//        };
    }

}
