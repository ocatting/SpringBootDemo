package com.yu.juc.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @Description: Atomic 实现原理会发生ABA问题
 * @Author Yan XinYu
 **/
public class AtomicTest {

    public static void atomicTest(){
        System.out.println("测试Atomic Test 问题");
        AtomicInteger atomicInteger = new AtomicInteger();
        try {
            int count = 100;
            CountDownLatch countDownLatch = new CountDownLatch(count);
            for(int i=0;i<count;i++){
                new Thread(()->{
                    try {
                        atomicInteger.incrementAndGet();
                        System.out.println(Thread.currentThread().getName()+":执行完成");
                    } finally {
                        if(countDownLatch!=null){
                            countDownLatch.countDown();
                        }
                    }
                },"t"+i).start();
            }
            //等待主线程执行完毕
            countDownLatch.await();
        } catch (InterruptedException e) {
            System.out.println("线程中断异常:"+e.getMessage());
        }
        System.out.println("Atomic测试线程全部执行完成:"+atomicInteger.get());
    }

    /**
     * ABA问题 执行流程:
     * 初始值为1
     * main 线程 暂停 1s
     * other 线程 修改 1+1-1(此时other线程已经修改了原本值)
     * main 线程 修改值为 2 (由于other线程值已经改回来，但是main线程没有发现任然修改了值)
     *  A 修改成 B 接着修改回 A
     */
    public static void atomicABA(){
        System.out.println("测试Atomic Test 问题");
        AtomicInteger atomicInteger = new AtomicInteger(1);
        Thread main = new Thread(()->{
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //旧值是1,更新为2
            boolean b = atomicInteger.compareAndSet(1, 2);
            System.out.println("main更新状态:"+b+" 当前值:"+atomicInteger.get());

        },"main");
        main.start();
        Thread other = new Thread(()->{
            //+1
            atomicInteger.incrementAndGet();
            //-1
            atomicInteger.decrementAndGet();
        },"other");
        other.start();
        System.out.println("结束，当前值:"+atomicInteger.get());
    }

    /**
     * 解决ABA问题
     * 通过标记stampe(印记/版本)来修改
     *
     */
    public static void resolveAtomicABA(){
        AtomicStampedReference<Integer> atomicStampedReference = new AtomicStampedReference(1,0);

        Thread main = new Thread(()->{
            int stamped = atomicStampedReference.getStamp();
            try {
                System.out.println("初始值1");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //旧值是1,更新为2
            boolean b = atomicStampedReference.compareAndSet(1, 2,stamped,stamped+1);
            System.out.println("main更新状态:"+b+" 当前值:"+atomicStampedReference.getReference());

        },"main");
        main.start();
        Thread other = new Thread(()->{
            int stamped = atomicStampedReference.getStamp();
            //+1
            atomicStampedReference.compareAndSet(1,2,stamped,stamped+1);
            //-1
            stamped = atomicStampedReference.getStamp();
            atomicStampedReference.compareAndSet(2,1,stamped,stamped+1);
        },"other");
        other.start();
        System.out.println("结束，当前值:"+atomicStampedReference.getReference());
    }

    public static void main(String[] args) {
        AtomicTest.resolveAtomicABA();
    }
}
