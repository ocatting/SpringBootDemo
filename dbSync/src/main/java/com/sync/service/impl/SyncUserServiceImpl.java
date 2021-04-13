package com.sync.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sync.entity.SyncUser;
import com.sync.mapper.SyncUserMapper;
import com.sync.service.SyncUserService;
import org.springframework.stereotype.Service;
/**
 * @Description:  登录用户
 * @Author: Yan XinYu
 * @Date:    2021-03-16 20:37
 */
@Service
public class SyncUserServiceImpl extends ServiceImpl<SyncUserMapper, SyncUser> implements SyncUserService{

    @Override
    public SyncUser loadByUserName(String username) {
        return getOne(Wrappers.<SyncUser>lambdaQuery().eq(SyncUser::getUsername,username),false);
    }
}
