package com.project.mybatis.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Description: 自定义主从双机数据库切换
 * @Author Yan XinYu
 **/
@Slf4j
@Aspect
@Component
public class DataSourceAspect {

    //   *任意返回       com.project.*.service全类路径  .. 当前包与所有子包 * 类名，‘*’即所有类      .*(..)所有方法名与所有参数
    @Pointcut("execution(* com.project.mybatis.service.option1..*.*(..))")
    public void masterPointCut(){}

    @Pointcut("execution(* com.project.mybatis.service.option2..*.*(..))")
    public void slavePointCut(){}

    @Before("masterPointCut()")
    public void masterBefore(){
        setDataSource(DynamicDataSource.Db.MASTER);
    }

    @Before("slavePointCut()")
    public void slaveBefore(){
        setDataSource(DynamicDataSource.Db.SLAVE);
    }

    public void setDataSource(DynamicDataSource.Db DbType){
        String contextHolder = DynamicDataSource.getContextHolder();
        if(DbType.getValue().equals(contextHolder)){
            return;
        }
        if(log.isDebugEnabled()){
            log.debug("Dynamic DataSource current holder {} convert to c {} dataSource ....",contextHolder,DbType.getValue());
        }
        //切换设置数据源
        DynamicDataSource.setContextHolder(DbType.getValue());
    }

}
