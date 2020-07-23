package com.yu.juc.executor;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 线程池的应用
 * @Author Yan XinYu
 **/
public class ExecutorTestDemo {

    /**
     * 缓存线程池: 无界队列
     * 初始大小为空，根据执行的线程的多少自动扩容线程数量，
     * 若线程 60s 未执行过则自动剔除。
     */
    public static void cachedThreadPool(){
        ExecutorService executorService = Executors.newCachedThreadPool();
        for(int i=0;i<100;i++){
            //executorService.execute();
            executorService.submit(()->{
                String name = Thread.currentThread().getName();
                System.out.println("CachedThreadPool线程:"+name+"开始，"+System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("CachedThreadPool线程:"+name+"结束，"+System.currentTimeMillis());
            });
        }
        executorService.shutdown();
    }

    /**
     * 固定线程池大小,最多有固定线程活动 无界队列
     */
    public static void fixedThreadPool(){
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i=0;i<10;i++){
            //executorService.execute();
            executorService.submit(()->{
                String name = Thread.currentThread().getName();
                System.out.println("FixedThreadPool线程:"+name+"开始，"+System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("FixedThreadPool线程:"+name+"结束，"+System.currentTimeMillis());
            });
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }

    /**
     * 固定只有一个线程活动，无界队列
     */
    public static void singleThreadExecutor(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for(int i=0;i<10;i++){
            //executorService.execute();
            executorService.submit(()->{
                String name = Thread.currentThread().getName();
                System.out.println("SingleThreadExecutor线程:"+name+"开始，"+System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("SingleThreadExecutor线程:"+name+"结束，"+System.currentTimeMillis());
            });
        }
        executorService.shutdown();
    }

    /**
     * 定时任务线程池
     * 封装有 newSingleThreadScheduledExecutor 单线程执行
     */
    public static void scheduledThreadPool(){
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

        //固定速率执行: 准备时间为1秒 ，接下来每隔1秒执行
//        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("执行");
//            }
//        },1,1, TimeUnit.SECONDS);

        //固定延迟执行 :准备时间为1秒 ，上一个任务结束后 延迟1秒执行
//        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("执行");
//            }
//        },1,1, TimeUnit.SECONDS);

        //延期执行，只执行一次。
        scheduledExecutorService.schedule(new Runnable() {
            @Override
            public void run() {
                System.out.println("执行");
            }},1,TimeUnit.SECONDS);

//        scheduledExecutorService.shutdown();
    }

    /**
     * 利用Fork join pool 工作机制 并行执行
     * 会根据CPU个数并行线程
     */
    public static void workStealingPool(){
        ExecutorService executorService = Executors.newWorkStealingPool();//(int parallelism)
        for(int i=0;i<10;i++){
            //executorService.execute();
            executorService.submit(()->{
                String name = Thread.currentThread().getName();
                System.out.println("SingleThreadExecutor线程:"+name+"开始，"+System.currentTimeMillis());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("SingleThreadExecutor线程:"+name+"结束，"+System.currentTimeMillis());
            });
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        //测试缓存线程池
//        ExecutorTestDemo.cachedThreadPool();

        //测试固定线程池
        ExecutorTestDemo.fixedThreadPool();

        //测试单例线程池
//        ExecutorTestDemo.singleThreadExecutor();

        //定时执行线程池
//        ExecutorTestDemo.scheduledThreadPool();

        //fork join pool 并行执行
        ExecutorTestDemo.workStealingPool();

        //ThreadPoolExecutor 原始线程池

    }
}
