package com.project.slave.service.master.impl;

import com.project.slave.entity.User;
import com.project.slave.mapper.UserMapper;
import com.project.slave.service.master.UserMainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class UserMainServiceImpl implements UserMainService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void save(User user) {
        log.info("查询:{}",userMapper.selectById(1));
//        userMapper.save(user);
    }
}
