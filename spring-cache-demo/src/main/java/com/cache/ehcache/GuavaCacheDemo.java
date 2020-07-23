package com.cache.ehcache;

import com.google.common.cache.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
public class GuavaCacheDemo {

    public static LoadingCache<String,String> cache = CacheBuilder.newBuilder()
    .maximumSize(20)
    .expireAfterWrite(20, TimeUnit.SECONDS)
    .expireAfterAccess(20,TimeUnit.SECONDS)
    .removalListener(new RemovalListener<String, String>() {
        @Override
        public void onRemoval(RemovalNotification<String, String> notification) {
            log.info("监听移除:{}={},原因:{}",notification.getKey(),notification.getValue(),notification.getCause());
        }
    })
    .build(new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            log.info("缓存中...key:" + key);
            return "test" + "_" + key;
        }
    });

    public static void main(String[] args) {

        cache.put("nihao","你好");
        cache.put("nihao1","你好");
        try {
            System.out.println("获取:"+cache.get("nihao"));
        } catch (ExecutionException e) {
           log.info("获取出错:{}",e.getMessage());
        }
        System.out.println("haha"+cache.asMap());
        //清除
        cache.invalidateAll();
        System.out.println("haha"+cache.asMap());

    }
}
