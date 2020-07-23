package com.project.mybatisplus.service;

import com.project.mybatisplus.entity.User;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface UserService {

    List<User> selectById(int id);

    List<User> selectByOrder(String index);
}
