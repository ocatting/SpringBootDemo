package com.project.mybatis.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
public class MybatisDataConfigs {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.test1" )
    public DataSource dataSourceTest1() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.druid.test2" )
    public DataSource dataSourceTest2() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public DynamicDataSource multipleDataSource(DataSource dataSourceTest1,DataSource dataSourceTest2){
        Map<Object, Object> target = new HashMap<>(2);
        target.put(DynamicDataSource.Db.MASTER.getValue(),dataSourceTest1);
        target.put(DynamicDataSource.Db.SLAVE.getValue(),dataSourceTest2);
        return new DynamicDataSource(dataSourceTest1,target);
    }

    @Bean
    @Primary
    public JtaTransactionManager jtaTransactionManager() {
        return new JtaTransactionManager(new UserTransactionImp(), new UserTransactionManager());
    }
}
