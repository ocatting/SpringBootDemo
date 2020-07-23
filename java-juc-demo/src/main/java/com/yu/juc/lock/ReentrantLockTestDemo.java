package com.yu.juc.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Description: 对ReentrantLock的学习
 *
 * 对于下面锁的使用，
 * Synchronized JVM级别的内置锁，实现了自动加锁与解锁的方式。
 * ReentrantLock 是代码级别的锁，提供了手动上锁与可重入，公平/非公平方式。比Synchronized更加灵活
 * ReentrantReadWriteLock 是在ReentrantLock是进行更加细粒度的划分锁，分为读锁与写锁，提升相互间的访问隔离。
 *
 * @Author Yan XinYu
 **/
public class ReentrantLockTestDemo implements Runnable{

    private ReentrantLock reentrantLock = new ReentrantLock(true);
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();


    /**
     * tryLock不管拿到拿不到都直接返回;
     * lock如果拿不到则会一直等待。
     */
    public void lock(){

        try {
            reentrantLock.lock();

            System.out.println("my name is :"+Thread.currentThread().getName());

            reentrantLock.lock();
            System.out.println("lock rewait：锁的重入与重解锁");
            reentrantLock.unlock();

        }finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 读锁的目的是防止其他线程写。
     * 读锁:我要读数据其他人不许写数据进去
     * 写锁：我要写数据这个时候不要读
     */
    public void readWriteLock(){
        try{
            reentrantReadWriteLock.readLock().lock();
            try {
                System.out.println("readWriteLock：读锁:"+Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(3);
                System.out.println("readWriteLock：读锁释放:"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (1>0){
                //此时我需要写入数据，其他人不许进来
                writeLock();
            }

        } finally {
            reentrantReadWriteLock.readLock().unlock();
        }
    }

    /**
     * 写锁：此时线程被独占
     */
    public void writeLock(){
        try {
            reentrantReadWriteLock.writeLock().lock();
            try {
                System.out.println("readWriteLock：写锁:"+Thread.currentThread().getName());
                TimeUnit.SECONDS.sleep(4);
                System.out.println("readWriteLock：写锁释放:"+Thread.currentThread().getName());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } finally {
            reentrantReadWriteLock.writeLock().unlock();
        }
    }

    @Override
    public void run() {
//        lock();
        readWriteLock();
    }

    public static void main(String[] args) {
        ReentrantLockTestDemo reentrantLockTestDemo = new ReentrantLockTestDemo();
        new Thread(reentrantLockTestDemo,"T1").start();

        new Thread(()->reentrantLockTestDemo.writeLock(),"T2").start();
    }
}
