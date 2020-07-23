package com.yu.juc.lock;

import java.util.concurrent.Semaphore;

/**
 * @Description: 对Semaphroe源码分析，实现共享锁 实现来自AQS
 * @Author Yan XinYu
 **/
public class SemaphroeTestDemo implements Runnable {

    private int state = 2;
    private Semaphore semaphore = new Semaphore(state);

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        try {
            semaphore.acquire();

            System.out.println("线程:"+name+":aquire() at time:"+System.currentTimeMillis());

            Thread.sleep(3000);

        } catch (InterruptedException e){
            System.out.println("线程 "+name+" 被中断"+e.getMessage());
        }finally{
            semaphore.release();
        }
    }

    public static void main(String[] args) {
        SemaphroeTestDemo semaphroeTestDemo = new SemaphroeTestDemo();
        for(int i=0;i<5;i++)
            new Thread(semaphroeTestDemo).start();
    }
}
