package com.undertow.demo.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class HelloService {

    @Async
    public void execute(){
        log.info("execute ====> start");
        log.info("Hello execute current thread: {}",Thread.currentThread().getName());
        IntStream.range(0, 5).forEach(d -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        log.info("execute ====> end");
    }
}
