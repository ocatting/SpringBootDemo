package com.yu.distace.redis.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Description: 地理位置解决方案
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class RedisDistanceService {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 为member 添加一个地理位置。
     * 对应Redis 命令 geoadd
     * @param point
     * @param id
     */
    public void addPoint(Point point,String id){
        redisTemplate.opsForGeo().add(GeoConstant.DEFAULT_KEY,point,id);
    }

    public void addPoint(float x,float y,String id){
        redisTemplate.opsForGeo().add(GeoConstant.DEFAULT_KEY,new Point(x,y),id);
    }

    public void addPoint(String key,float x,float y,String id){
        redisTemplate.opsForGeo().add(key,new Point(x,y),id);
    }

    // batch
    public void addPoint(String key, Map<Object, Point> memberCoordinateMap){
        redisTemplate.opsForGeo().add(key,memberCoordinateMap);
    }

    /**
     *  移除对应主题或者member
     *  对应Redis 命令 georemove
     * @param key
     */
    public void remove(String key){
        redisTemplate.opsForGeo().remove(key);
    }

    public void remove(String... id){
        redisTemplate.opsForGeo().remove(GeoConstant.DEFAULT_KEY,id);
    }

    public void remove(String key,String... id){
        redisTemplate.opsForGeo().remove(key,id);
    }

    /**
     *  对应Redis 命令 geodist
     */
    public double distanceValue (String member1, String member2){
        Distance distance = redisTemplate.opsForGeo().distance(GeoConstant.DEFAULT_KEY, member1, member2);
        return distance == null?-1:distance.getValue();
    }

    public Distance distance (String member1, String member2){
        return redisTemplate.opsForGeo().distance(GeoConstant.DEFAULT_KEY,member1,member2);
    }

    public Distance  distance (String key,String member1,String member2){
        return redisTemplate.opsForGeo().distance(key,member1,member2);
    }

    /**
     * 固定返回单位
     * @param member1
     * @param member2
     * @return
     */
    public Distance distanceByKm (String member1, String member2){
        return redisTemplate.opsForGeo().distance(GeoConstant.DEFAULT_KEY,member1,member2, RedisGeoCommands.DistanceUnit.KILOMETERS);
    }

    public Distance distanceByUnit (String member1, String member2, RedisGeoCommands.DistanceUnit distanceUnit){
        return redisTemplate.opsForGeo().distance(GeoConstant.DEFAULT_KEY,member1,member2, distanceUnit);
    }

    /**
     * geohash ： 获取元素经纬度坐标经过geohash算法生成的base32编码值
     * @param id
     * @return
     */
    public List<String> hash(String... id){
        return redisTemplate.opsForGeo().hash(GeoConstant.DEFAULT_KEY,id);
    }

    public List<String> hash(String key,String... id){
        return redisTemplate.opsForGeo().hash(key,id);
    }


    /**
     * 查找某人的位置
     * 对应命令：geopos
     * @param id
     * @return
     */
    public List<Point> position(String... id){
        return redisTemplate.opsForGeo().position(GeoConstant.DEFAULT_KEY,id);
    }

    public List<Point> position(String key,String... id){
        return redisTemplate.opsForGeo().position(key,id);
    }

    public List<Object> findByRadius(String key,Point point){
        //圆心，地球半径
        Circle circle = new Circle(point,Metrics.KILOMETERS.getMultiplier());
        // 距离圆心 距离
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs()
                .includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> radius = redisTemplate.opsForGeo().radius(key, circle,args);
        log.info("通过经纬度附近的人：" , radius);
        return Collections.EMPTY_LIST;
    }

    public List<Object> findByRadius(String key,String member,int km){
        //通过地方查找附近5km 的50 个人
        RedisGeoCommands.GeoRadiusCommandArgs args2 = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeDistance().includeCoordinates().sortAscending().limit(50);
        GeoResults<RedisGeoCommands.GeoLocation<Object>> radius = redisTemplate.opsForGeo().radius("home", "张三", new Distance(km, Metrics.KILOMETERS),args2);
        return Collections.EMPTY_LIST;
    }




    //定义一些常量
    public class GeoConstant {
        //主题
        private final static String DEFAULT_KEY = "home";
    }

}
