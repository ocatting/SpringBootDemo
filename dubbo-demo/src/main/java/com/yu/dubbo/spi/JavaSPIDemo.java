package com.yu.dubbo.spi;

import java.util.ServiceLoader;

/**
 * @Description: java SPI 机制
 * 读取 META-INF/services 下文件
 * 如 mysql-connect-java.jar 也实现了该机制。
 *
 * 在Dubbo中使用了该机制，但是对SPI数据做了升级:MATA-INF/dubbo/ 下文件 :key = class.path
 * @Author Yan XinYu
 **/
public class JavaSPIDemo {

    public static void main(String[] args) {
        /**
         * 查找该MATE-INF/services 下类路径为 TestService.class 下的所有实现
         */
        ServiceLoader<TestService> load = ServiceLoader.load(TestService.class);
        TestService testService = load.iterator().next();
        System.out.println(testService.getName());

    }
}
