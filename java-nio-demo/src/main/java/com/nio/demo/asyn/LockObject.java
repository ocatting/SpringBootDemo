package com.nio.demo.asyn;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class LockObject {

    public static final void lock(Object o) throws InterruptedException {
        synchronized (o){
            o.wait();
        }
    }

    public static final void lock(Object o,long timeout) throws InterruptedException {
        synchronized (o){
            o.wait(timeout);
        }
    }

    public static final void unlock(Object o){
        synchronized (o){
            o.notifyAll();
        }
    }
}
