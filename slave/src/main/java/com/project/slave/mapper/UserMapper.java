package com.project.slave.mapper;

import com.project.slave.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Mapper
public interface UserMapper {

    List<User> selectById(@Param("id") Integer id);

    void save(@Param("user") User user);
}
