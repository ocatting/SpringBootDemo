package com.yu.security.repository;

import com.yu.security.entity.TUser;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface UserRepository extends Repository {

    TUser find(String id);
}
