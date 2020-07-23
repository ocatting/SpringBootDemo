package com.nio.demo.asyn;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 调用链依次执行相关的操作
 * @Author Yan XinYu
 **/
public class BaseFilterChain implements FilterChain {

    private static final List<ProcessHandler> handlers = new ArrayList<>();
    private int index = 0;

    private Message message;
    private StopTime stopTime;

    public BaseFilterChain(Message message,StopTime stopTime){
        this.message = message;
        this.stopTime = stopTime;
        this.handlers.add(new SearchCardHandler());
        this.handlers.add(new OrderHandler());
    }

    public BaseFilterChain(Message message,StopTime stopTime,List<ProcessHandler> handlers){
        this.message = message;
        this.stopTime = stopTime;
        if(handlers != null){
            this.handlers.addAll(handlers);
        }
    }

    public void setHandlers(ProcessHandler processHandler){
        this.handlers.add(processHandler);
    }

    @Override
    public Object proceed() {
        if (this.index >= this.handlers.size()){
            return this.message;
        }
        System.out.println(index);
        ProcessHandler handler = handlers.get(index++);
        this.message = handler.handle(message,stopTime);
        return proceed();
    }

    public static void main(String[] args) {
        Message message = new Message();
        message.setId("1");
        message.setQueue("01");
        BaseFilterChain baseFilterChain = new BaseFilterChain(message,null);
        Object proceed = baseFilterChain.proceed();
        System.out.println(proceed.toString());
    }
}
