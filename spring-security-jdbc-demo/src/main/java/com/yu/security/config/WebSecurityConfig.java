package com.yu.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Description: 登录相关的关键代码
 * @Author Yan XinYu
 **/
//@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired private WebUserDetailsService webUserDetailsService;

    @Autowired private CacheManager cacheManager;

    @Autowired private WebAuthHandler webAuthHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
        auth.eraseCredentials(false);
    }

    /**
     * 服务安全配置 如 配置 对公共资源，静态资源的 免登陆
     * @param web
     * @throws Exception
     */
    @Override
    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/public/**", "/static/**","/json");
        web.ignoring().mvcMatchers("/public/**", "/static/**","/json");
    }

    /**
     * http 请求配置 如: 登录登出的拦截
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 对单点登录(sso) 访问地址的跨域的控制，若没有单点登录则忽略
        http.csrf().ignoringAntMatchers("/oauth/authorize", "/oauth/token", "/oauth/rest_token");


        //对请求赋予权限
        http.authorizeRequests()
                // permitAll() 的URL路径属于公开访问，不需要权限
                .antMatchers("/public/**").permitAll()
                .antMatchers("/static/**").permitAll()
                .antMatchers("/oauth/rest_token*").permitAll()
                .antMatchers("/login*").permitAll()

                // /user/ 开头的URL需要 ADMIN 权限
                .antMatchers("/user/**").hasAnyRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/login*").anonymous()
                .anyRequest().authenticated();

        http.formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/signin")
                .successForwardUrl("/signin")
//                .failureUrl("/login?error=1")
                .failureUrl("/json")
                .usernameParameter("username")
                .passwordParameter("password")
                //重写登录成功后 successForwardUrl 配置地址无效 ，Handler必须自定义返回参数与跳转
                .successHandler(webAuthHandler)
                //重写登录失败后 failureUrl 配置地址无效 ，Handler必须自定义返回参数
//                .failureHandler(webAuthHandler)
            .and()
                .logout()
                .logoutUrl("/signout")
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
            .and()
                .exceptionHandling();

        http.authenticationProvider(authenticationProvider());
    }

    /**
     * 对用户的控制，如有需要记住密码，登录次数超时可对此进行重写
     * 可拥有多个对象如QQ，webchat
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        WebAuthenticationProvider webAuthenticationProvider = new WebAuthenticationProvider();
        webAuthenticationProvider.setUserDetailsService(webUserDetailsService);
        webAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        webAuthenticationProvider.setCacheManager(cacheManager);
        return webAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
