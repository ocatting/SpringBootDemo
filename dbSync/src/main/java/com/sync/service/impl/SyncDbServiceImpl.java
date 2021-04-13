package com.sync.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sync.core.JdbcFactory;
import com.sync.entity.SyncDb;
import com.sync.mapper.SyncDbMapper;
import com.sync.service.SyncDbService;
import org.springframework.stereotype.Service;

import java.sql.Connection;

/**
 * @Description: ${description}
 * @Author: Yan XinYu
 * @Date: 2021-03-10 16:39
 */
@Service
public class SyncDbServiceImpl extends ServiceImpl<SyncDbMapper, SyncDb> implements SyncDbService {

    @Override
    public boolean connectTest(Integer dbId) throws Exception {
        SyncDb db = getById(dbId);
        Connection connection = JdbcFactory.getConnection(db);
        return connection.isValid(5000);
    }
}


