package com.yu.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * @Description: Test实例
 * @Author Yan XinYu
 **/
@Slf4j
public class KafkaTestDemo {

    @Test
    public void kafkaProducer() throws InterruptedException {
        log.info("kafkaProducer Test...");

        KafkaProducer<String, String> stringKafkaProducer = new KafkaProducer<>(kafkaProducerConfig());

        int count = 5;
        CountDownLatch countDownLatch = new CountDownLatch(count);

        for (int i = 0; i < count; i++) {
            ProducerRecord<String, String> producerRecord =
                    new ProducerRecord<String, String>("kafka_topic","id01","my is consumer data");
            //同步
            kafkaProducerSendByBlock(stringKafkaProducer,producerRecord);
            //异步
//            kafkaProducerSendByAsync(stringKafkaProducer,producerRecord);
            countDownLatch.countDown();
        }

        countDownLatch.await();
        stringKafkaProducer.close();

        log.info("kafkaProducer Test complete!");

    }

    /**
     * 生产数据，阻塞发送
     * @param stringKafkaProducer
     * @param producerRecord
     */
    public void kafkaProducerSendByBlock(KafkaProducer<String, String> stringKafkaProducer,ProducerRecord<String, String> producerRecord){
        try {
            RecordMetadata recordMetadata = stringKafkaProducer.send(producerRecord).get();
            log.info("TimeTemp:{} 阻塞方式发送消息结果: topic-{}|partition-{}|offset-{}",System.currentTimeMillis(),recordMetadata.topic(),recordMetadata.partition(),recordMetadata.offset());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * 生产消息，异步方式
     * @param stringKafkaProducer
     * @param producerRecord
     */
    public void kafkaProducerSendByAsync(KafkaProducer<String, String> stringKafkaProducer,ProducerRecord<String, String> producerRecord){

//        stringKafkaProducer.send(producerRecord, new Callback() {
//            @Override
//            public void onCompletion(RecordMetadata metadata, Exception exception) {
//                if(exception !=null){
//                    log.error("kafka 发送失败: ",exception.getStackTrace());
//                    return;
//                }
//                log.info("TimeTemp:{} 阻塞方式发送消息结果: topic-{}|partition-{}|offset-{}",System.currentTimeMillis(),metadata.topic(),metadata.partition(),metadata.offset());
//            }
//        });

        //推荐 lamda 方式书写
        stringKafkaProducer.send(producerRecord, (metadata,exception) -> {
            if(exception !=null){
                log.error("kafka 发送失败: ",exception.getStackTrace());
                return;
            }
            log.info("TimeTemp:{} 阻塞方式发送消息结果: topic-{}|partition-{}|offset-{}",System.currentTimeMillis(),metadata.topic(),metadata.partition(),metadata.offset());
        });

    }

    public Properties kafkaProducerConfig(){

        Properties props = new Properties();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        /**
         * 发送消息持久化机制参数
         * acks=0;表示producer不需要等broker确认收到消息的回复，就可以继续发送下一条消息。性能最高，但是最容易丢消息。
         * acks1;至少要等待leader已经成功将数据写入本地log，但是不需要等待所有follower是否成功写入。
         *       就可以继续发送下一条消息。 这种情况下，如果follower没有成功备份数据，而此时leader
         * acks=‐1或all： 这意味着leader需要等待所有备份(min.insync.replicas配置的备份个数)都成功写入日志，这种策略会保证只要有 一个备份存活就不会丢失数据。
         * 这是最强的数据保证。一般除非是金融级别，或跟钱打交道的场景才会使用这种配置。
         *
         */
        props.put(ProducerConfig.ACKS_CONFIG,"1");
        /**
         * 发送失败重试次数，默认重试时间间隔为100ms,若失败做好幂等处理
         */
        props.put(ProducerConfig.RETRIES_CONFIG,"3");
        /**
         * 重试时间间隔,默认100ms
         */
        props.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG,300);
        /**
         * 设置发送消息的本地缓冲区，如果设置了该缓冲区，消息会先发送到本地缓冲区，可以提高消息发送性能，默认值是33554432，即32MB
         */
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,33554432);
        /**
         * kafka本地线程会从缓冲区取数据，批量发送到broker，
         * 设置批量发送消息的大小，默认值是16384，即16kb，就是说一个batch满了16kb就发送出去
         */
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);
        /**
         * 默认值是0，意思就是消息必须立即被发送，但这样会影响性能
         * 一般设置100毫秒左右，就是说这个消息发送完后会进入本地的一个batch，如果100毫秒内，这个batch满了16kb就会随batch一起被发送出去
         * 如果100毫秒内，batch没满，那么也必须把消息发送出去，不能让消息的发送延迟时间太长
         */
        props.put(ProducerConfig.LINGER_MS_CONFIG,100);

        //key-value 序列化方式
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        return props;
    }


    @Test
    public void kafkaConsumer(){
        log.info("kafkaConsumer Test...");
        KafkaConsumer<String, String> stringKafkaConsumer = new KafkaConsumer<>(kafkaConsumerConfig());
        String topic = "kafka_topic";

        stringKafkaConsumer.subscribe(Arrays.asList(topic));
//        stringKafkaConsumer.assign(Arrays.asList(new TopicPartition(topic,0)));

        //消息回溯，从什么地方开始消费
//        stringKafkaConsumer.seekToBeginning(Arrays.asList(new TopicPartition(topic,0)));
        //指定 offset消费
//        stringKafkaConsumer.seek(new TopicPartition(topic,0),10);

        while (true){

            ConsumerRecords<String, String> records = stringKafkaConsumer.poll(Duration.ofMinutes(1));
            for (ConsumerRecord record: records) {
                log.info("接收到消息：offset={},key={},value={}",record.offset(),record.key(),record.value());
            }
            if (records.count() > 0){
                //提交offset
                stringKafkaConsumer.commitAsync();
            }
        }

    }

    /**
     * 客户端配置
     * @return
     */
    public Properties kafkaConsumerConfig(){
        Properties props = new Properties();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        //消费分组
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"kafka_groupid_demo");
        //自动提交offset 时间间隔
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,1000);
        //是否自动提交
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,true);
        /**
         * 心跳时间
         * 服务端broker通过心跳确认consumer是否故障，如果发现故障，
         * 就会通过心跳下发rebalance的指令给其他的consumer通知他们进行rebalance操作。
         */
        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG,1000);
        //服务端broker多久感知不到一个consumer心跳就认为他故障了，默认是10秒
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,10 * 1000);
        /**
         * 如果两次poll操作间隔超过了这个时间，broker就会认为这个consumer处理能力太弱，会将其踢出消费组，将分区分配给别的consumer消费
         */
        props.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG,30 * 1000);
        //序列化方式
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringDeserializer.class.getName());

        return props;
    }

}
