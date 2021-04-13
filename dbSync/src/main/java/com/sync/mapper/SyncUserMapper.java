package com.sync.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sync.entity.SyncUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description:  用户
 * @Author: Yan XinYu
 * @Date:    2021-03-16 20:37
 */
@Mapper
public interface SyncUserMapper extends BaseMapper<SyncUser> {
}