package com.demo.cache.redis.tool;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class RedisConstant {

    /** 服务前缀 */
    private final static String SPRING_CACHE_REDIS_PREFIX = "demo:cache:redis:";

    /** 用户登陆 */
    public final static String USER_LOGIN = SPRING_CACHE_REDIS_PREFIX + "user:login:%s";

    public static String setLoginKey(String key){
        return String.format(USER_LOGIN,key);
    }

}
