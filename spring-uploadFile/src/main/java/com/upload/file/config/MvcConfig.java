package com.upload.file.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;

import javax.servlet.MultipartConfigElement;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
public class MvcConfig {

    /**
     * 或者在配置文件中直接配置
     * @return
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件设置 最大10M,DataUnit提供5中类型B,KB,MB,GB,TB
        factory.setMaxFileSize(DataSize.of(50, DataUnit.MEGABYTES));
        /// 同时支持 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.of(150, DataUnit.MEGABYTES));
        return factory.createMultipartConfig();
    }
}
