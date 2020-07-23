package com.project.mybatis.mapper;

import com.project.mybatis.entity.Test;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Mapper
public interface TestMapper {

    Test findById(@Param("id") Integer id);

    void save(@Param("data") Test test);
}
