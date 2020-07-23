package com.undertow.demo.service;

import com.undertow.demo.entity.Test;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface TestService {

    void execute();

    List<Test> selectById(Integer id);

    @Transactional(rollbackFor = Exception.class)
    void insert(Test test);
}
