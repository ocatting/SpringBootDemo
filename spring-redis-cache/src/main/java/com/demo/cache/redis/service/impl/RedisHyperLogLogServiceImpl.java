package com.demo.cache.redis.service.impl;

import com.demo.cache.redis.service.RedisHyperLogLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * @Description: HyperLogLog 实现
 * redis 实现命令
 *  添加pfadd key value ;
 *  统计：pfcount key ;
 *  合并统计：PFMERGE key...;
 *
 *  应用场景：(统计网页用户访问量)需要统计网页每天的用户访问量的数据，同一个用户一天之内的多次访问请求只能计数一次，
 *      要求每一个网页请求都需要带上用户的 ID，无论是登陆用户还是未登陆用户都需要一个唯一 ID 来标识。
 *
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class RedisHyperLogLogServiceImpl implements RedisHyperLogLogService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    public void setUser(String page,String userId){
        redisTemplate.opsForHyperLogLog().add(page,userId);
    }

    public Long getVisitCount(String page){
        return redisTemplate.opsForHyperLogLog().size(page);
    }

    public Long getMultiple(String name,String... pages){
        return redisTemplate.opsForHyperLogLog().union(name,pages);
    }

}
