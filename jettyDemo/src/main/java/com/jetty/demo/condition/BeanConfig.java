package com.jetty.demo.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
public class BeanConfig {

    @Conditional(WinCondition.class)
    @Bean(name = "bill")
    public Person person1(){
        return new Person(62,"Bill Gates");
    }

    @Conditional(LinuxCondition.class)
    @Bean("linux")
    public Person person2(){
        return new Person(48,"Linux");
    }

}
