package com.jetty.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@SpringBootApplication
public class JettyDemoApplication {

    public static void main(String[] args) {
//        List<String> list = new ArrayList<>(3);
//        list.add(null);
//        list.add("1");
//        list.add(null);
//        System.out.println(list);
//        list.removeAll(Collections.singleton(null));
//        System.out.println(list);

        SpringApplication.run(JettyDemoApplication.class,args);
    }
}
