package com.yu.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description:
 * @Author Yan XinYu
 **/

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class HikariApplicationTests {

    @Autowired
    DataSource dataSource;

    /**
     * 测试发现 MySQL6.0以上必须配置  serverTimezone=Asia/Shanghai {serverTimezone=UTC 全球标准时区}
     */
    @Test
    public void dataSourceTest(){
        log.info("开始测试datasource======>");
        try {
            Connection connection = dataSource.getConnection();
            log.info("测试datasource======>:{}",connection);
            log.info("测试地址datasource======>:{}",connection.getMetaData().getURL());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
