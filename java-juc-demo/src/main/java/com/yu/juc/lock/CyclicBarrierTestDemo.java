package com.yu.juc.lock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @Description: 删拦 指执行过程中等待全部线程执行完成后接着执行所定义的函数
 * 通俗来说就是 等待所有线程到达指定位置后继续执行这些线程。
 * @Author Yan XinYu
 **/
public class CyclicBarrierTestDemo {

    public void run (CyclicBarrier cyclicBarrier,int threadWaitTime){
        String name = Thread.currentThread().getName();
        try{
            System.out.println("线程:"+name+" 等待 "+threadWaitTime+"ms,"+System.currentTimeMillis());
            //当前线程挂起，等待其余线程执行
            cyclicBarrier.await();
            System.out.println("线程:"+name+" 等待结束，其余线程也到达指定位置，我要继续执行任务 "
                    +threadWaitTime+"ms,"+System.currentTimeMillis());
        }catch (InterruptedException e){
            //线程中断
            System.out.println(e);
        }catch (BrokenBarrierException e) {
            //阻塞屏障异常(Barrier:屏障)
            System.out.println(e);
        }

    }

    /**
     *
     * 与CountDownLauch不同 ：当所有线程到达删拦位置后，子线程被执行
     * @param args
     * @throws BrokenBarrierException
     * @throws InterruptedException
     */
    public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
        CyclicBarrierTestDemo cyclicBarrierTestDemo = new CyclicBarrierTestDemo();
        /**
         * 等待2个线程执行到指定位置后，其余线程继续执行
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);
        /**
         * 等待2个线程执行到指定位置后 子线程被执行，其余线程继续执行
         */
//        CyclicBarrier cyclicBarrier = new CyclicBarrier(2,
//                ()-> System.out.println("start：执行方法"));

        new Thread(()->cyclicBarrierTestDemo.run(cyclicBarrier,2000) ,"t1").start();
        new Thread(()->cyclicBarrierTestDemo.run(cyclicBarrier,2000) ,"t2").start();
        new Thread(()->cyclicBarrierTestDemo.run(cyclicBarrier,3000) ,"t3").start();

//        cyclicBarrier.await();

        System.out.println("我是主线程我先执行完成:"+System.currentTimeMillis());
    }
}
