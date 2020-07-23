package com.yu.rocketmq.demo.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.MixAll;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Filter的使用有两种方式，
 *      一为 sql92结构过滤
 *      二为 上传自定义拦截实现的java文件
 *
 *      而且必须确保 rocket broker.conf 文件中开启了filter的使用
 *      # enablePropertyFilter=true
 * @Author Yan XinYu
 **/
@Slf4j
public class FilterPushConsumer {

    private String groupid = "batch_send_groupid";
    private String namesrvAddr = "192.168.124.101:9876";
    private String topic = "topic_sync_demo";

    /**
     * 通过 sql92 匹配数据
     * @throws MQClientException
     * @throws InterruptedException
     */
    @Test
    public void filterData() throws MQClientException, InterruptedException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupid);
        consumer.setNamesrvAddr(namesrvAddr);
        // a between 0 and 3 and b = 'yangguo'
        consumer.subscribe(topic, MessageSelector.bySql("property_a between 0 and 3 and property_b = 'b'"));
//          通过message tags 字段查询
//        consumer.subscribe(topic, MessageSelector.byTag("tag_a|tag_b|tag_c"));

        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext)->{
            for (MessageExt msg : list){
                log.info("收到消息:{}",new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();

        log.info("consumer started");

        TimeUnit.MINUTES.sleep(30);
    }

    /**
     * 通过指定文件匹配数据
     * @throws MQClientException
     * @throws InterruptedException
     * @throws IOException
     */
    @Test
    public void filterByJavaFilter() throws MQClientException, InterruptedException, IOException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(groupid);
        consumer.setNamesrvAddr(namesrvAddr);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File classFile = new File(classLoader.getResource("MessageFilterImpl.java").getFile());

        String filterCode = MixAll.file2String(classFile);
        consumer.subscribe("TopicFilter7", "org.apache.rocketmq.example.filter.MessageFilterImpl",
                filterCode);

        consumer.registerMessageListener((List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext)->{
            for (MessageExt msg : list){
                log.info("收到消息:{}",new String(msg.getBody()));
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        consumer.start();

        log.info("consumer started");

        TimeUnit.MINUTES.sleep(30);
    }

}
