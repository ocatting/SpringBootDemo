package com.yu.juc.unsafe;

import java.util.concurrent.locks.LockSupport;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class LockSupportTest {

    public static void main(String[] args) {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("thread - is running----");
                LockSupport.park();//阻塞当前线程
                System.out.println("thread is over-----");
            }
        });

        t.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LockSupport.unpark(t);//唤醒指定的线程

        //放置 暂停线程
//        LockSupport.park();

        //解锁 当前线程
//        LockSupport.unpark(Thread.currentThread());


    }
}
