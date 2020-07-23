package com.project.mybatisplus.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @Description: tomcat 配置
 * @Author Yan XinYu
 **/
@Slf4j
@Component
public class TomcatWebServerConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    /** 获取物理内核 */
    private static final int N_THREADS = Runtime.getRuntime().availableProcessors();

    /** 默认最大线程数量 */
    private static final int DEFAULT_MAX_THREAD_COUNT = 1000;

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        if(log.isDebugEnabled()){
            log.debug("Tomcat config start...");
        }
    }

    /**
     * 设置tomcat参数
     * @param factory
     */
    private void setCustomizeConfig(TomcatServletWebServerFactory factory){
        factory.addConnectorCustomizers();
    }
}
