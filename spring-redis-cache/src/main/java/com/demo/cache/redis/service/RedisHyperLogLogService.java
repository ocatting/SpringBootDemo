package com.demo.cache.redis.service;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface RedisHyperLogLogService {

    /**
     * 添加一个访问用户
     * 由于底层是一个set 集合，不会存在同样的userId 所以不需判断是否添加过
     * @param page
     * @param userId
     */
    void setUser(String page,String userId);

    /**
     * 获取页面访问次数
     * @param page /name 页面或者合并后的name
     * @return
     */
    Long getVisitCount(String page);

    /**
     * 合并后存放到一个 key 中 下次可重getUser中获取
     * @param name
     * @param pages
     * @return
     */
    Long getMultiple(String name,String... pages);

}
