package com.yu.rocketmq.demo.schedule;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

/**
 * @Description: 延时队列原理，采用中间延时队列存放数据，等数据到期后放入正常队列中被消费。
 * @Author Yan XinYu
 **/
@Slf4j
public class ScheduleSyncProducer {

    private String groupid = "groupid_producer_demo";
    private String namesrvAddr = "192.168.124.101:9876";
    private String topic = "topic_sync_demo";

    @Test
    public void sync_producer() throws InterruptedException, RemotingException,
            MQClientException, MQBrokerException, UnsupportedEncodingException {

        DefaultMQProducer producer = new DefaultMQProducer(groupid);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setSendMsgTimeout(20*1000);
        producer.start();

        String topic = "topic_sync_demo";
        //搜索日志时 tags,keys 会用到
        String tags = "sync_test";
        //搜索日志时可以理解为orderID
        String keys = "tag";
        String msg = "hello rocketMQ sync test";
        Message message = new Message(topic,tags,keys,
                msg.getBytes(RemotingHelper.DEFAULT_CHARSET));

        //设定延时等级
        // org.apache.rocketmq.store.config.MessageStoreConfig
        // private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
        message.setDelayTimeLevel(6);

        SendResult send = producer.send(message);
        log.info("发送消息结果:{}",send);

        producer.shutdown();
    }
}
