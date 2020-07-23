package com.yu.juc.unsafe;

import sun.misc.Unsafe;

/**
 * @Description: unsafe 实现显示加解 synchtonized 底层采用 monitor
 * @Author Yan XinYu
 **/
public class ObjectMonitorTest {

    private static final Unsafe unsafe = UnsafeInstance.getUnsafe();

    /**
     * 该方法已被弃用，采用java ReentrantLock 加解锁
     * @param args
     */
    public static void main(String[] args) {

        Object o = new Object();
        //加锁
        unsafe.monitorEnter(o);

        //解锁
        unsafe.monitorExit(o);

    }
}
