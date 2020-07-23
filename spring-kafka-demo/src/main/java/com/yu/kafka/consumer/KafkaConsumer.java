package com.yu.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Component
public class KafkaConsumer {

    /**
     * 集群配置
     * @KafkaListener(groupId = "kafka_demo",topicPartitions = {
     *             @TopicPartition(topic = "topic1",partitions = {"0","1"}),
     *             @TopicPartition(topic = "topic2",partitions = "0",
     *                     partitionOffsets =
     *                         @PartitionOffset(partition = "1",initialOffset = "100"))
     *     },concurrency = "6")
     */
    @KafkaListener(topics = "kafka_topic",groupId = "kafka_groupid_demo")
    public void listen(ConsumerRecord<String,String> record){
        String value = record.value();
        log.info("消费结果:{}",record);
        log.info("消费数据:{}",value);
    }

    @KafkaListener(topics = "kafka_topic",groupId = "kafka_groupid_demo02")
    public void listen02(ConsumerRecord<String,String> record){
        String value = record.value();
        log.info("02消费结果:{}",record);
        log.info("02消费数据:{}",value);
    }


}
