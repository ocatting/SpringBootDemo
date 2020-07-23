package com.yu.distace.mongodb.service;

import com.yu.distace.mongodb.entity.User;
import com.yu.distace.mongodb.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.NearQuery;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class MongoDbDistanceService {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserRepository userRepository;

    public void test(){
        //初始化打车人胡汉山的经纬度点
        Point point = new Point(43.7, 48.8);
        //初始化老王牛肉面馆所在的经纬度点
        Point point1 = new Point(50.2,60.8);
        //初始化老汉卤肉铺,假设老汉卤肉铺的位置跟下单人同一经纬度.
        User user = new User(2,"胡汉山","男",point);
        //初始化老王牛肉面
        User person1 = new User(1,"老王","男",point1);
        //将老汉卤肉铺当前的位置存入数据库里
        mongoTemplate.insert(user);
        //将老王牛肉面当前的位置存入数据库里
        mongoTemplate.insert(person1);
        //设置距离,查找3公里以内的所有商铺
        Distance distance = new Distance(3, Metrics.KILOMETERS);
        List<User> list = userRepository.findByLocationNear(point, distance);

        list.forEach(u ->{ log.info("find:{}",u); });
    }

    /**
     * 查找附近的人1km ~ 5km 的人
     */
    public void nearSphere(){
        Query location = new Query(Criteria.where("location")
                        .nearSphere(new GeoJsonPoint(120.666666666, 30.888888888))
                .maxDistance(5000).minDistance(1000)
        );
        List<User> users = mongoTemplate.find(location, User.class);
        users.forEach(user -> {log.info("nearSphere:{}",user);});
    }

    /**
     * “查找周围1km~5km内附近的人”，并返回距离信息。
     */
    public void nearQuery() {
        long startTime = System.currentTimeMillis();
        NearQuery near = NearQuery
                .near(new GeoJsonPoint(120.666666666, 30.888888888))
                .spherical(true)
                .maxDistance(5, Metrics.KILOMETERS)
                .minDistance(1, Metrics.KILOMETERS)
                .num(10);
        GeoResults<User> results = mongoTemplate.geoNear(near, User.class);

        long endTime = System.currentTimeMillis();
        System.out.println("程序运行时间：" + (endTime - startTime) + "ms");

        for (GeoResult<User> result : results) {
            System.out.println(result);
        }
    }


}
