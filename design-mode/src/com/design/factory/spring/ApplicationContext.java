package com.design.factory.spring;

public interface ApplicationContext {

    /**
     * 获取bean对象
     * @param beanId
     * @return
     */
    Object getBean(String beanId);
}
