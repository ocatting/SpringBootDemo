package com.yu.config;

import com.yu.convert.FastJSONMessageConvert;
import com.yu.convert.MessageConvert;
import com.yu.web.servlet.NoCrudHttpServlet;
import com.yu.web.servlet.NoCrudServletRegistrationBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRegistration;
import javax.sql.DataSource;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Configuration
@ConditionalOnSingleCandidate(DataSource.class)
@AutoConfigureAfter(DataSourceAutoConfiguration.class)
public class NoCrudAutoConfiguration {

    @Bean
    public MappingConfiguration mappingConfiguration(){
        return new MappingConfiguration();
    }

    /**
     * 默认执行器配置
     * @param dataSource
     * @return
     */
//    @Bean
//    public SQLExecutor sqlExecutor(MappingConfiguration mappingConfiguration,DataSource dataSource){
//        return new BaseSQLExecutor(mappingConfiguration,dataSource);
//    }

//    @Bean
//    public Service service(MappingConfiguration mappingConfiguration){
//        return new CacheService(mappingConfiguration);
//    }

    /**
     * HTTPServlet 注册
     */
    @Configuration
    @ConditionalOnClass(ServletRegistration.class)
    @EnableConfigurationProperties(NoCrudProperties.class)
    protected static class NoCrudServletRegistrationConfiguration {

        private final NoCrudProperties noCrudProperties;

        private static final String DEFAULT_NOCRUD_SERVLET_BEAN_NAME = "noCrudServlet";

        public NoCrudServletRegistrationConfiguration(NoCrudProperties noCrudProperties){
            this.noCrudProperties = noCrudProperties;
        }

        @Bean
        public NoCrudServletRegistrationBean noCrudServletRegistrationBean() {
            NoCrudServletRegistrationBean registration = new NoCrudServletRegistrationBean(new NoCrudHttpServlet()
                    ,noCrudProperties.getServlet().getPath());
            registration.setName(DEFAULT_NOCRUD_SERVLET_BEAN_NAME);
            registration.setLoadOnStartup(this.noCrudProperties.getServlet().getLoadOnStartup());

            String username = this.noCrudProperties.getServlet().getUsername();
            String password = this.noCrudProperties.getServlet().getPassword();
            registration.addInitParameter("loginUsername", username==null?"root":username);
            registration.addInitParameter("loginPassword", password==null?"123456":password);
            return registration;
        }

    }

    @Bean
    public MessageConvert messageConvert(){
        return new FastJSONMessageConvert();
    }

    // 从磁盘中读取设定好的SQL

}
