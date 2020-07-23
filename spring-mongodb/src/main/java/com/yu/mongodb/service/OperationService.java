package com.yu.mongodb.service;

import com.yu.mongodb.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @Description: MongoDb 操作
 * 模糊查询仅支持正则表达式
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class OperationService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public void add(User user){
        for (int i = 0; i < 10; i++) {
            mongoTemplate.insert(user);
        }
    }

    public List<User> findAll(){
        List<User> all = mongoTemplate.findAll(User.class);
        all.forEach(user -> log.info("查询信息为:{}",user));
        return all;
    }

    public void update(){
        mongoTemplate.updateFirst(query(where("name").is("你好")), Update.update("name","康康"),User.class);
    }

    public void remove(User user){
        user = new User();
        user.setId(2);
        mongoTemplate.remove(user);
    }

}
