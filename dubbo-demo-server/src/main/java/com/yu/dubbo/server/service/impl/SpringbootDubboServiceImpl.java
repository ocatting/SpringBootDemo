package com.yu.dubbo.server.service.impl;

import com.yu.dubbo.service.SpringbootDubboService;

import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Service(version = "0.0.1")
@Component
public class SpringbootDubboServiceImpl implements SpringbootDubboService {

    @Override
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getName();
    }
}
