package com.sync.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Description: 锁工具
 * @Author: Yan XinYu
 * @Date: 2021-04-17 21:18
 */
public class LockUtil {

    private static final Map<String,Object> USER_LOCK = new ConcurrentHashMap<>();

    private static final Object LOCAL_LOCK = new Object();

    public static Object getLock(String uuid){
        synchronized (LOCAL_LOCK){
            USER_LOCK.computeIfAbsent(uuid, k -> new Object());
            return USER_LOCK.get(uuid);
        }
    }

    public static void unLock(String uuid){
        synchronized (LOCAL_LOCK){
            USER_LOCK.remove(uuid);
        }
    }
}
