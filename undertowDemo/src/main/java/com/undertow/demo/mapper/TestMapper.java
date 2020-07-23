package com.undertow.demo.mapper;

import com.undertow.demo.entity.Test;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Mapper
public interface TestMapper {

    List<Test> selectById(Integer id);

    void insert(Test test);
}
