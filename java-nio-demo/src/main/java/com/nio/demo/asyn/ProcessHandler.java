package com.nio.demo.asyn;

/**
 * @Description: 流程处理
 * @Author Yan XinYu
 **/
public interface ProcessHandler {

    Message handle(Message message,StopTime stopTime);

}
