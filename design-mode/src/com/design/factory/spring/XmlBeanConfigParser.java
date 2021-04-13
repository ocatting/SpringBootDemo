package com.design.factory.spring;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class XmlBeanConfigParser implements BeanConfigParser {

    @Override
    public List<BeanDefinition> parse(String configLocation) {
        InputStream in = null;
        try {
            in = this.getClass().getResourceAsStream("/" + configLocation);
            if (in == null) {
                throw new RuntimeException("Can not find config file: " + configLocation);
            }
            return parse(in);
         } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    // TODO: log error
                    throw new RuntimeException("解析异常:",e.getCause());
                }
            }
        }
    }

    @Override
    public List<BeanDefinition> parse(InputStream inputStream) {

        // 省略实现...
        return null;
    }
}
