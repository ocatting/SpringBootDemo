package com.sync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * @Description:  ${description}
 * @Author: Yan XinYu
 * @Date:    2021-04-09 18:43
 */
    
    
@Data
@TableName(value = "t_sync_group")
public class SyncGroup {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分组名称
     */
    @TableField(value = "group_name")
    private String groupName;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    public static final String COL_ID = "id";

    public static final String COL_GROUP_NAME = "group_name";

    public static final String COL_CREATE_TIME = "create_time";
}