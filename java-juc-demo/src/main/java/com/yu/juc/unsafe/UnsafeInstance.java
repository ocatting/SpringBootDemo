package com.yu.juc.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Description: unsafe 实例化
 * @Author Yan XinYu
 **/
public class UnsafeInstance {

    /**
     * 通过反射方式获取unsafe.class
     * @return
     */
    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe) field.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
