package com.yu.juc.atomic;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Description: unsafe 操作对象值
 * @Author Yan XinYu
 **/
public class AtomicUnsafeUpdate {

    private String name;
    private int age;

    /**
     * Unsafe获取方式有两种:
     * 一:通过 unsafe.getUnsafe() 方式获取 但此类必须是由boostrapClassLoad (引导类加载器加载)
     * 二:通过 反射方式获取。如下getUnsafe()；方法
     */
    private static final Unsafe unsafe = getUnsafe();
//    private static final Unsafe unsafe = Unsafe.getUnsafe(); 异常
    private static final long valueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset
                    (AtomicUnsafeUpdate.class.getDeclaredField("age"));
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    public AtomicUnsafeUpdate(String name,int age){
        this.name = name;
        this.age = age;
    }

    public int getAge(){
        return this.age;
    }

    /**
     * 通过调用unsafe类获取修改age
     * @param expect
     * @param update
     */
    public void compareAndSwapAge(int expect,int update){
        //param : 当前修改类, 偏移量(指age在内存地址的位置), 原值, 目标值
        unsafe.compareAndSwapInt(this,valueOffset,expect,update);
    }

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

    public static void main(String[] args) {
        AtomicUnsafeUpdate atomicUnsafeUpdate = new AtomicUnsafeUpdate("张三",18);
        atomicUnsafeUpdate.compareAndSwapAge(18,21);
        System.out.println("更新张三后的值:"+atomicUnsafeUpdate.getAge());
    }


}
