package com.design.factory.spring;

import java.io.InputStream;
import java.util.List;

public interface BeanConfigParser {

    List<BeanDefinition> parse(String configContent);

    List<BeanDefinition> parse(InputStream inputStream);
}
