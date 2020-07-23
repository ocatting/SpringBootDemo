package com.project.mybatis.service.option1;

import com.project.mybatis.entity.Test;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface TestService {

    Test findById(Integer id);

    void save(Test test);
}
