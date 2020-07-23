package com.yu.juc.lock;

import java.util.concurrent.CountDownLatch;

/**
 * @Description: 同步工具,当线程全部执行完成后在继续执行主线程
 * @Author Yan XinYu
 **/
public class CountDownLatchTestDemo {

    public void run (CountDownLatch countDownLatch,long threadWaitTime){
        String name = Thread.currentThread().getName();
        try{
            System.out.println("线程:"+name+" 等待 "+threadWaitTime+"ms,"+System.currentTimeMillis());
            Thread.sleep(threadWaitTime);
            System.out.println("线程:"+name+" 结束 "+threadWaitTime+"ms,"+System.currentTimeMillis());
        }catch (InterruptedException e){
            System.out.println(e);
        }finally {
            if(countDownLatch!=null){
                countDownLatch.countDown();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //设定同时进入的线程数
        CountDownLatch countDownLatch = new CountDownLatch(2);
        CountDownLatchTestDemo countDownLauchTestDemo = new CountDownLatchTestDemo();
        //等待队友全部进入房间
        //我是队友一，进入房间需要2s的时间
        Thread t1 = new Thread(()->countDownLauchTestDemo.run(countDownLatch,2000),"handler one");
        t1.start();
        t1.join(1000);//主线程进入等待(若选择了时间1s则只等待1s时间后继续执行)，直到t1执行完成
        //我是队友二，进入房间需要3s的时间
        Thread t2 = new Thread(()->countDownLauchTestDemo.run(countDownLatch,4000),"handler secend");
        t2.start();

//        countDownLatch.await();
//        可设置一秒后超时主线程继续执行
//        countDownLatch.await(1, TimeUnit.SECONDS);
//        t1.interrupt();
//        t2.interrupt();

        System.out.println("我们都已进入房间:"+System.currentTimeMillis());

    }

}
