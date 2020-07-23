package com.yu.distace.mongodb.repository;

import com.yu.distace.mongodb.entity.User;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Component
public interface UserRepository extends MongoRepository<User,String> {

    /**
     * 查询附近的用户
     * @param location 当前位置
     * @param distance 搜索范围
     * @return
     */
    List<User> findByLocationNear(Point location, Distance distance);
}
