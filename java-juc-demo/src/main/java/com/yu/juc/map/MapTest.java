package com.yu.juc.map;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description:
 * map的数据结构是 数组 + 链表 (1.8 + 红黑树)
 * hash 数组 数据链表
 * @Author Yan XinYu
 **/
public class MapTest {

    public void hashMapTest(){
        // 1.7 版本 hashMap 多线程环境 链表死锁
        // hashMap 多线程环境始终出现  命中 null ，为内置数组扩容 导致。
        // 初始容量:16 扩容因子: 0.75
        Map map = new HashMap<String,String>();
        map.put("1","一");
        map.put("2","二");
        map.put("3","三");
        System.out.println(map.toString());

    }

    public static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * 1.7 版本存在死锁
     */
    public void deadlockTest(){
        Map map = new HashMap<Integer,Integer>();
        AtomicInteger atomicInteger = new AtomicInteger();

        while (atomicInteger.get() < 100000){
            map.put(atomicInteger.get(),atomicInteger.get());
            atomicInteger.incrementAndGet();
        }
    }

    /**
     * 1、& 计算
     *  0011 & 0101 两个操作数都为1才为1，结果0001。
     * 2、| 计算
     * 0001 | 0011 只要有一位为1即为1，结果0011
     *
     * @param args
     */
    public static void main(String[] args) {



//        MapTest mapTest = new MapTest();
//        mapTest.hashMapTest();

//        mapTest.deadlockTest();

        /**
         * 与 计算 数据 +1 为相同的数据返回
         */

//        System.out.println( 15 & 15); System.out.println(" Binary :"+Integer.toBinaryString(15));
//        System.out.println( 15 & 16); System.out.println(" Binary :"+Integer.toBinaryString(16));
//        System.out.println( 15 & 17); System.out.println(" Binary :"+Integer.toBinaryString(17));
//        System.out.println( 15 & 18); System.out.println(" Binary :"+Integer.toBinaryString(18));
//        System.out.println( 15 & 19); System.out.println(" Binary :"+Integer.toBinaryString(19));
//        System.out.println( 15 & 20); System.out.println(" Binary :"+Integer.toBinaryString(20));
//
//        int n = 16 - 1;
//        n |= n >>> 1;
//        n |= n >>> 2;
//        n |= n >>> 4;
//        n |= n >>> 8;
//        n |= n >>> 16;
//        System.out.println(n);
    }


}
