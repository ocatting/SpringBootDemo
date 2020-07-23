package com.jetty.demo.condition;


import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class WinCondition implements Condition {

    /**
     * 执行一段逻辑并返回之歌注解是否可被使用
     * @param context
     * @param metadata
     * @return
     */
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        //获取ioc使用的beanFactory
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        //获取类加载器
        ClassLoader classLoader = context.getClassLoader();
        //获取当前环境信息
        Environment environment = context.getEnvironment();
        //获取bean定义的注册类
        BeanDefinitionRegistry registry = context.getRegistry();
        //获得当前系统名
        String property = environment.getProperty("os.name");
        System.out.println("windows:"+property);
        //包含Windows则说明是windows系统，返回true
        System.out.println("sfgghhg: "+property.startsWith("Windows"));
        if (property.startsWith("Windows")){
            return true;
        }
        return false;
    }
}
