package com.yu.es.service;

import com.yu.es.entity.User;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface UserService {

    void save(User user);

    void delete(Integer id);
}
