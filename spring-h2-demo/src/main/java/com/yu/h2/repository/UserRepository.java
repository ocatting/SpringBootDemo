package com.yu.h2.repository;

import com.yu.h2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
}
