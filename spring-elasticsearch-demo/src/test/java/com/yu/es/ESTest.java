package com.yu.es;

import com.yu.es.entity.User;
import com.yu.es.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ESTest {

    @Autowired
    private UserService userService;

    @Test
    public void insertAll(){
        User user = new User();
        user.setId(1);
        user.setName("张三");
        user.setPassword("1231231");
        user.setRole("admin");
        userService.save(user);
    }
}
