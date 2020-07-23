package com.nio.demo.asyn;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @Description: 订单配置
 * @Author Yan XinYu
 **/
@Data
public class OrderConfiguration {

    private Map<String, LinkedBlockingQueue> order_map = new ConcurrentHashMap<>();
}
