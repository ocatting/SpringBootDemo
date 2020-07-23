package com.cache.ehcache;

import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @Description: 简单使用Ehcache Demo
 * @Author Yan XinYu
 **/
@Slf4j
public class EhcacheDemo {


    public static void main(String[] args) {
        String path = "D:/AWork/IdeaWorkSpace/SpringBootDemo/spring-cache-demo/target/classes/ehcache.xml";
//        String path = EhcacheDemo.class.getResource("/").toString() +"ehcache.xml";
        log.info("Ehcache config地址:{}",path);
        CacheManager cacheManager = CacheManager.create(path);
        cacheManager.addCacheIfAbsent("cache_test");

        Cache cache = cacheManager.getCache("cache_test");

        // 存放元素
        for(int i=0;i<100;i++){
            cache.put(new Element("firstCache"+i,"第一个元素firstCache="+i));
        }

        // 获取元素
        Element firstCache = cache.get("firstCache1");
        log.info("获取缓存元素:{}",firstCache);
        log.info("获取元素的属性：创建时间{},过期时间：{}，命中率：{}"
                ,firstCache.getCreationTime()
                ,firstCache.getExpirationTime()
                ,firstCache.getHitCount()
        );

        log.info("磁盘使用空间：{}",cache.getDiskStoreSize());
        log.info("缓存大小:{}",cache.getKeys().size());

    }

}
