package com.nio.demo.asyn;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class OrderHandler implements ProcessHandler{
    @Override
    public Message handle(Message message,StopTime stopTime) {
        message.setType("order");
        System.out.println(1/0);
        System.out.println("OrderHandler" + message.toString());
        return message;
    }
}
