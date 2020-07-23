package com.demo.cache.redis.service.impl;

import com.demo.cache.redis.entity.User;
import com.demo.cache.redis.service.RedisService;
import com.demo.cache.redis.tool.RedisConstant;
import com.demo.cache.redis.tool.RedisTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StreamOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RedisTool redisTool;

    @Override
    public void add() {
        log.info("redis:add start");

        ArrayList<String> objects = new ArrayList<>(0);
        objects.add("objects:user");
        User user = new User();
        user.setId(123129);
        user.setName("张三");
        user.setPassword("123456");
        user.setDesc("人生赢家");
        user.setList(objects);
        user.setCreateTime(LocalDateTime.now());
        String key = RedisConstant.setLoginKey("1");
        redisTool.set(key,user, Duration.ofMinutes(1));

        log.info("redis:add end");
    }


//    ===============  数据结构 ===============

    /**
     * 存放Hash结构
     */
    public void hash(){
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String key = "hello";
        //存放单个
        hashOperations.put(key,"hashKey","value");

        //存放多个
        Map<String,String> map = new HashMap();
        hashOperations.putAll(key,map);

        //查
        hashOperations.get(key,"hashKey");

        //是否包含hashKey字段
        hashOperations.hasKey(key,"hashKey");

        //获取所有key - value
        hashOperations.keys(key);
        List<Object> listKey = new ArrayList<>();
        hashOperations.multiGet("key", listKey);
        hashOperations.values(key);
        //删除
        hashOperations.delete(key,"hashKey");
    }

    /**
     * 获取 set 结构并且取并集交集
     *
     */
    public void set(){
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        String key = "setKey";
        //添加
        setOperations.add(key,"nihao","kaixin");

        //包含某一个值
        setOperations.isMember(key,"value");

        //删除
        setOperations.remove(key,"value");//删除某一个值

        /**
         * 使用场景：她也关注的主播，共同关注的好友。
         */
        //取差集(两个集合的差集)
        setOperations.difference(key,"otherKey");

        //取交集(两个集合的交集)
        setOperations.intersect(key,"otherKey");

    }

    /**
     * 队列实现方式
     */
    public void stream(){
        StreamOperations<String, Object, Object> streamOperations = redisTemplate.opsForStream();
//        streamOperations.acknowledge();//手动确认消息
//        streamOperations.createGroup();//创建消费组
//        streamOperations.add();//生产记录
    }
}
