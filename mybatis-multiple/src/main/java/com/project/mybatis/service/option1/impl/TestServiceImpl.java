package com.project.mybatis.service.option1.impl;

import com.project.mybatis.entity.Test;
import com.project.mybatis.mapper.TestMapper;
import com.project.mybatis.service.option1.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Service
@Transactional(transactionManager = "jtaTransactionManager")
public class TestServiceImpl implements TestService {

    @Autowired private TestMapper testMapper;

    public Test findById(Integer id) {
        return testMapper.findById(id);
    }

    public void save(Test test){
        testMapper.save(test);
    }
}
