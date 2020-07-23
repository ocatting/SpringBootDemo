package com.project.slave.service.slave;

import com.project.slave.entity.User;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface UserService {

    List<User> selectById(int id);
}
