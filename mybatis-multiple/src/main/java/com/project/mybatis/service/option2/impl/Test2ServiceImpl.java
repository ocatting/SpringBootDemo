package com.project.mybatis.service.option2.impl;

import com.project.mybatis.entity.Test;
import com.project.mybatis.mapper.TestMapper;
import com.project.mybatis.service.option2.Test2Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Transactional(transactionManager = "jtaTransactionManager")
@Service
public class Test2ServiceImpl implements Test2Service {

    @Autowired private TestMapper testMapper;

    public Test findById(int id) {
        return testMapper.findById(id);
    }

    @Override
    public void save(Test test) {
        testMapper.save(test);
    }
}
