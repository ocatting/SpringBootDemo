package com.yu.security.config;

import com.yu.security.entity.TUser;
import com.yu.security.repository.jdbc.UserRepositoryJdbc;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @Description: 查询用户信息
 * @Author Yan XinYu
 **/
@Slf4j
@Component
public class WebUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepositoryJdbc userRepositoryJdbc;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TUser tUser = userRepositoryJdbc.find(username);
        log.info("登录用户信息:{}",tUser);
        if(tUser == null){
            throw new UsernameNotFoundException("Not found any user for username[" + username + "]");
        }
        //此数据将会与用户登录数据进行对比，可对此密码进行加密
        return new SOSUserDetails(tUser);
    }
}
