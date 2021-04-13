package com.sync.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sync.entity.SyncUser;
    /**
 * @Description:  ${description}
 * @Author: Yan XinYu
 * @Date:    2021-03-16 20:37
 */
    
public interface SyncUserService extends IService<SyncUser>{

    /**
     * 用户登录
     * @param username
     * @return
     */
    SyncUser loadByUserName(String username);
}
