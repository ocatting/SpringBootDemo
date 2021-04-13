package com.sync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @Description:  ${description}
 * @Author: Yan XinYu
 * @Date:    2021-03-16 20:37
 */
    
    
@Data
@TableName(value = "t_sync_user")
public class SyncUser {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 账号
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 角色：0-普通用户、1-管理员
     */
    @TableField(value = "`role`")
    private Integer role;

    /**
     * 权限：执行器ID列表，多个逗号分割
     */
    @TableField(value = "permission")
    private String permission;

    public static final String COL_ID = "id";

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWORD = "password";

    public static final String COL_ROLE = "role";

    public static final String COL_PERMISSION = "permission";
}