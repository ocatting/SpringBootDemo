package com.yu.dubbo.client;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.spring.context.annotation.EnableDubbo;
import com.yu.dubbo.service.SpringbootDubboService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Description:
 * @Author Yan XinYu
 **/
//@EnableDubbo
@SpringBootApplication
public class DubboClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(DubboClientApplication.class,args);
    }

//    @Reference
//    private SpringbootDubboService springbootDubboService;

//    @Bean
//    public ApplicationRunner getBean(){
//        return args -> {
//            System.out.println(springbootDubboService.getName());
//        };
//    }
}
