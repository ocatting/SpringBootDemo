package com.yu.rocketmq.demo.filter;

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
import java.util.ArrayList;

/**
 * @Description: 过滤器,支持 sql92 过滤数据
 * @Author Yan XinYu
 **/
@Slf4j
public class FilterSyncProducer {

    private String groupid = "batch_send_groupid";
    private String namesrvAddr = "192.168.124.101:9876";
    private String topic = "topic_sync_demo";

    @Test
    public void producerData() throws MQClientException, UnsupportedEncodingException, RemotingException, InterruptedException, MQBrokerException {

        DefaultMQProducer producer = new DefaultMQProducer(groupid);
        producer.setNamesrvAddr(namesrvAddr);
        //如果超时设置时间更长一些
        producer.setSendMsgTimeout(20*1000);
        producer.start();

        //搜索日志时 tags,keys 会用到
        String tags = "sync_test";
        //搜索日志时可以理解为orderID
        String keys = "tag";
        String msg = "hello rocketMQ sync test";

        ArrayList<Message> list = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            Message message = new Message(topic, tags, keys, msg.getBytes(RemotingHelper.DEFAULT_CHARSET));
            /**
             *  filter 过滤字段设置 property_a
             */
            message.putUserProperty("property_a",String.valueOf(i % 2));
            if((i % 3) > 0){
                message.putUserProperty("property_b","b");
            }else {
                message.putUserProperty("property_b","+b");
            }
            list.add(message);
        }

        SendResult send = producer.send(list);

        log.info("发送消息结果:{}",send);

        producer.shutdown();
    }

}
