package com.yu.security.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * @Description: 开启缓存管理
 * @Author Yan XinYu
 **/
@Configuration
@EnableCaching
public class CacheConfiguration {

    /**
     * 本文，采用配置为 手动生成 Cache方式生成缓存点。
     */

    /**
     * maven 导入配置 spring-cache jar
     * @EnableCaching
     * spring已经提供了一些常用的缓存工具 (RedisCacheManager,JCacheCacheManager,EhCacheCacheManager)
     * 可以自定义导入配置。
     * spring-cache 提供 几个注解可作用在方法上
     * @Cacheable 最常用的一个, 若缓存中没有对应键, 则执行方法, 并把返回值放入缓存, 如果有则从缓存中取, 不执行方法
     * @CacheEvict 删除缓存
     * @CachePut 修改缓存
     * @Caching 同时使用上面多个的时候使用
     * @CacheConfig 放在类上, 可统一指定该类上的其它注解的一些设置
     *
     */

    /**
     * 例如:
     * 可以定义多个CacheManager,在使用时进行选择
     * 不过需要指定一个默认的,不然启动会报错
     * @param redisConnectionFactory
     * @return
     */
//    @Primary
//    @Bean("permission")
//    public CacheManager permissionCacheManager(RedisConnectionFactory redisConnectionFactory) {
//        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
//                //前缀处理,也就是指定的缓存名称将以什么姓氏存到redis
//                .computePrefixWith(cacheName -> "permission" + ":" + cacheName + ":")
//                //键的序列化
//                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(keySerializer()))
//                //值的序列化
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer()))
//                //是否缓存空值
//                .disableCachingNullValues()
//                //过期时间
//                .entryTtl(Duration.ofDays(1));
//
//        return RedisCacheManager.RedisCacheManagerBuilder
//                .fromCacheWriter(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
//                .cacheDefaults(cacheConfiguration)
//                .build();
//    }
}
