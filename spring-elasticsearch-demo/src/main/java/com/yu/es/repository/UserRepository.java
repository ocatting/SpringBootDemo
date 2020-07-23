package com.yu.es.repository;

import com.yu.es.entity.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchCrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Repository
public interface UserRepository<User,Long> extends ElasticsearchCrudRepository<User,Long> {

}
