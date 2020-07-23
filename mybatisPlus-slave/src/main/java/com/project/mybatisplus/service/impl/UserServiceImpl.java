package com.project.mybatisplus.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.project.mybatisplus.entity.User;
import com.project.mybatisplus.mapper.UserMapper;
import com.project.mybatisplus.service.UserService;
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
@DS("master")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public List<User> selectById(int id) {
        return userMapper.selectById(id);
    }

    @Override
    @DS("slave_1")
    public List<User> selectByOrder(String index) {
        return userMapper.selectById(Integer.valueOf(index));
    }
}
