package com.project.mybatis;

import com.project.mybatis.config.MybatisDataConfigs;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@Import({MybatisDataConfigs.class})
public class MybatisMultipleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybatisMultipleApplication.class,args);
    }
}
