package com.sync.core.binlog;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.deserialization.EventDeserializer;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 基础功能测试
 * @Author: Yan XinYu
 * @Date: 2021-04-25 22:47
 */
@Slf4j
public class BaseTest {

    public static void main(String[] args) throws Exception {

        Config config = new Config();
        config.setMysqlAddr("127.0.0.1");
        config.setMysqlPort(3306);
        config.setMysqlUsername("root");
        config.setMysqlPassword("123456");

        log.info(config.toString());

        BinlogPositionManager binlogPositionManager = new BinlogPositionManager(config);

        BinaryLogClient binaryLogClient = new BinaryLogClient(config.getMysqlAddr(),
                config.getMysqlPort(),
                config.getMysqlUsername(),
                config.getMysqlPassword());
        binaryLogClient.setBlocking(true);
        binaryLogClient.setServerId(1001);

        EventDeserializer eventDeserializer = new EventDeserializer();
        eventDeserializer.setCompatibilityMode(EventDeserializer.CompatibilityMode.DATE_AND_TIME_AS_LONG,
                EventDeserializer.CompatibilityMode.CHAR_AND_BINARY_AS_BYTE_ARRAY);
        binaryLogClient.setEventDeserializer(eventDeserializer);

        binaryLogClient.registerEventListener(event -> {
            log.info(event.toString());
        });

        binaryLogClient.setBinlogFilename(binlogPositionManager.getBinlogFilename());
        binaryLogClient.setBinlogPosition(binlogPositionManager.getPosition());

        binaryLogClient.connect(3000);

        // 等待修改表结构或者insert、delete、update等操作。
        try {
            TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
