package com.nio.demo.asyn;

import java.util.concurrent.TimeUnit;

/**
 * @Description: 计时器
 * @Author Yan XinYu
 **/
public class StopTime {

    private int time;
    private TimeUnit timeUnit;

    public StopTime(int time,TimeUnit timeUnit){
        this.time = time;
        this.timeUnit = timeUnit;
    }

    // 获取剩余时间
    public long residue(){
        long current = System.currentTimeMillis();
        // 转换为毫秒
        long a = TimeUnit.MILLISECONDS.convert(time,timeUnit);
        long result = current - a;
        if(result <= 0){
            throw new RuntimeException("超时");
        }
        return result;
    }

}
