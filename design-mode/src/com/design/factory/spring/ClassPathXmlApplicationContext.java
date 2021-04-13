package com.design.factory.spring;

/**
 * xml 文件解析方法实现
 * @author mou ren
 */
public class ClassPathXmlApplicationContext implements ApplicationContext {

    private final BeansFactory beansFactory;
    private final BeanConfigParser beanConfigParser;

    public ClassPathXmlApplicationContext(String configLocation) {
        this.beansFactory = new BeansFactory();
        this.beanConfigParser = new XmlBeanConfigParser();
        loadBeanDefinitions(configLocation);
    }

    /**
     * 解析配置文件
     * @param configLocation
     */
    private void loadBeanDefinitions(String configLocation) {
        beanConfigParser.parse(configLocation);
    }

    @Override
    public Object getBean(String beanId) {
        return beansFactory.getBean(beanId);
    }
}
