package com.nio.demo.asyn;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @Description: 阻塞式异步交互
 * socket 异步提交后等待响应
 * @Author Yan XinYu
 **/
public class AsynObserver {

    public static final ExecutorService es = new ThreadPoolExecutor(0, 10, 0L, TimeUnit.MICROSECONDS,
            new LinkedBlockingDeque<>(),
            new ThreadFactoryBuilder().setNameFormat("postgres-cache-task" + "-%d").build(),
            new ThreadPoolExecutor.AbortPolicy());

    public void test(){
        StopTime stopTime = new StopTime(20,TimeUnit.SECONDS);
        Message message = new Message();
        // 发起寻卡
//        发布消息
//        订阅消息
        // 等待寻卡结果
        try {
            LockObject.lock(message);
        } catch (InterruptedException e) {
            System.out.println("被取消或者中断");
        }
        // 寻卡结果

        // 营销查询获取优惠信息

        // 发起订单

        // 订单支付完成

        // 支付结果
    }

    /**
     * 计时器
     */
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

    public class Message {
        public String id;
        public String type;
        public String message;
    }

    public static void main (String[] args){
//        Future submit = es.submit(new Callable() {
//
//            @Override
//            public Object call() throws Exception {
//                return "1";
//            }
//        });
//        try {
//            submit.get();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }

        // 阻塞方法
        LockSupport.parkUntil(new Object(),1000);

    }



}
