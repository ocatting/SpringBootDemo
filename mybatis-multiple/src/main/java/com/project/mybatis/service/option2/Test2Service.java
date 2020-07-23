package com.project.mybatis.service.option2;

import com.project.mybatis.entity.Test;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface Test2Service {

    Test findById(int id);

    void save(Test test);
}
