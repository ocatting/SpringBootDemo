package com.project.slave.service.slave.impl;

import com.project.slave.entity.User;
import com.project.slave.mapper.UserMapper;
import com.project.slave.service.slave.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectById(int id) {
        return userMapper.selectById(id);
    }
}
