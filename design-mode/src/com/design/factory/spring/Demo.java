package com.design.factory.spring;

import com.design.factory.spring.demo.RateLimiter;

/**
 * DEMO 测试入口
 */
public class Demo {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("factorybeans.xml");
        RateLimiter rateLimiter = (RateLimiter) applicationContext.getBean("rateLimiter");
        System.out.println(rateLimiter);
    }
}
