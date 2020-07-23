package com.yu.juc.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 电影票
 * @Author Yan XinYu
 **/
public class CinemaTicketDelay implements Delayed {

    private final long delay;

    private final long expire;

    private final String msg;

    private final long now;

    public CinemaTicketDelay(){
        this(null);
    }

    public CinemaTicketDelay(String msg){
        this(msg,1000);
    }

    public CinemaTicketDelay(String msg,long delay){
        this.msg = msg;
        this.delay = delay;
        this.now = System.currentTimeMillis();
        this.expire = now + delay;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(expire
                - System.currentTimeMillis(),TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        return (int)(this.getDelay(TimeUnit.MILLISECONDS)
                        - o.getDelay(TimeUnit.MILLISECONDS));
    }

    @Override
    public String toString() {
        return "CinemaTicketDelay{" +
                "delay=" + delay +
                ", expire=" + expire +
                ", msg='" + msg + '\'' +
                ", now=" + now +
                '}';
    }
}
