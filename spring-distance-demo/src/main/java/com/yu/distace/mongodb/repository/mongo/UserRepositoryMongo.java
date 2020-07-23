package com.yu.distace.mongodb.repository.mongo;

import com.yu.distace.mongodb.entity.User;
import com.yu.distace.mongodb.repository.UserRepository;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class UserRepositoryMongo
//        extends SimpleMongoRepository<User,String> implements UserRepository
{


//    public UserRepositoryMongo(MongoEntityInformation<User, String> metadata, MongoOperations mongoOperations) {
//        super(metadata, mongoOperations);
//    }
//
//    @Override
//    public List<User> findByLocationNear(Point location, Distance distance) {
//        return null;
//    }
}
