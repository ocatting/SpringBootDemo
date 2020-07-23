package com.nio.demo.asyn;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 订单支付
 * LockSupport.park函数是将当前调用Thread阻塞，而unpark函数则是将指定线程Thread唤醒。操作对象是线程。
 * Object.wait 是锁定该对象实例。操作对象是Object。
 * @Author Yan XinYu
 **/
@Slf4j
public class OrderService {

    private final List<ProcessHandler> handlers = new ArrayList<>();
    private OrderConfiguration configuration;

    private Map<String,Message> messageMap = new ConcurrentHashMap<>();
    private int index = 0;

    public OrderService(){
        handlers.add(new SearchCardHandler());
    }

    public Object proceed(){
        // 消息链路追踪
        MDC.put("traceId", UUID.randomUUID().toString());
        StopTime stopTime = new StopTime(20, TimeUnit.SECONDS);
        // 一个新消息
        String id = UUID.randomUUID().toString();
        Message message = new Message();
        message.setId(id);
        message.setQueue("1");

        BaseFilterChain baseFilterChain = new BaseFilterChain(message,stopTime);
        return baseFilterChain.proceed();
    }

    /**
     * 取消支付
     * @param messageId
     * @return
     */
    public boolean cancel(String messageId){
        if(messageId ==null || messageId.isEmpty()){
            return false;
        }
        Message message = messageMap.get(messageId);
        if(message == null){
            message.setState(MessageState.CANCEL);
            message.setFinishTime(LocalDateTime.now());
            LockObject.unlock(message);
            return true;
        }
        return false;
    }

    /**
     * 分配队列
     * @param message
     * @return
     */
    public Object dispatcherQueue(Message message){


        return null;
    }

    /**
     * 寻卡
     * @return
     */
    public Object searchCard(){


        return null;
    }


    /**
     * 创建订单
     * @return
     */
    public Object createOrder(){


        return null;
    }

    /**
     * 订单支付
     * @return
     */
    public Object orderPay(){

        return null;
    }

    public static void main(String[] args) throws InterruptedException {
        Object o = new Object();

        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                System.out.println("阻塞线程1");
                synchronized (o){
                    o.wait(10000);
                }
                System.out.println("释放阻塞线程1");
            }
        }).start();
        new Thread(new Runnable() {
            @SneakyThrows
            @Override
            public void run() {
                System.out.println("阻塞线程2");
                synchronized (o) {
                    o.wait(10000);
                }
                System.out.println("释放阻塞线程1");
            }
        }).start();


        Thread.sleep(5 * 1000);

        synchronized (o) {
            o.notify();
        }

    }


}
