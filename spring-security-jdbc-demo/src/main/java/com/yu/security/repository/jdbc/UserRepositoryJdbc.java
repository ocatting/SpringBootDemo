package com.yu.security.repository.jdbc;

import com.yu.security.entity.TUser;
import com.yu.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Repository
public class UserRepositoryJdbc implements UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public TUser find(String username) {
        final String sql = "select * from user where username = ? ";
        final List<TUser> query = jdbcTemplate.query(sql, new Object[]{username}, new BeanPropertyRowMapper(TUser.class));
        return query.isEmpty() ? null : query.get(0);
    }
}
