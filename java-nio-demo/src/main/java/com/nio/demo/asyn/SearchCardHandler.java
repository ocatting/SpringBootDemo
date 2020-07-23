package com.nio.demo.asyn;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class SearchCardHandler implements ProcessHandler{

    @Override
    public Message handle(Message message,StopTime stopTime){
        message.setType("SearchCard");
        System.out.println("SearchCardHandler" + message.toString());
        return message;
    }
}
