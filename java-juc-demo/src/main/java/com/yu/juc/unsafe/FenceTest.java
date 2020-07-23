package com.yu.juc.unsafe;

import sun.misc.Unsafe;

/**
 * @Description: 内存屏障
 * @Author Yan XinYu
 **/
public class FenceTest {

    private static final Unsafe unsafe = UnsafeInstance.getUnsafe();

    public static void main(String[] args) {

        unsafe.loadFence();//读内存屏障

        unsafe.storeFence(); //写内存屏障

        unsafe.fullFence();//读写内存屏障
    }

}
