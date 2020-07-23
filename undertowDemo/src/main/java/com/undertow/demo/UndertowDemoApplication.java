package com.undertow.demo;

import com.undertow.demo.aspect.TestAspect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@EnableAsync
@SpringBootApplication
@EnableAspectJAutoProxy
//@MapperScan
public class UndertowDemoApplication {

    /**
     * Demo ：undertowDemo 集成 @Async 异步注解
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(UndertowDemoApplication.class,args);
    }


}
