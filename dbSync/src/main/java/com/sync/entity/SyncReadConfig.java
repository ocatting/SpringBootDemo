package com.sync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:  ${description}
 * @Author: Yan XinYu
 * @Date:    2021-04-08 17:15
 */
@Data
@TableName(value = "t_sync_read_config")
public class SyncReadConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 表名称
     */
    @TableField(value = "`table`")
    private String table;

    /**
     * 自定义SQL
     */
    @TableField(value = "query_sql")
    private String querySql;

    /**
     * 限制内存大小
     */
    @TableField(value = "limit_memory_bytes")
    private Integer limitMemoryBytes;

    /**
     * 强制转码
     */
    @TableField(value = "mandatory_encoding")
    private String mandatoryEncoding;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private SyncDb syncDb;

    public static final String COL_ID = "id";

    public static final String COL_TASK_ID = "task_id";

    public static final String COL_TABLE = "table";

    public static final String COL_QUERY_SQL = "query_sql";

    public static final String COL_LIMIT_MEMORY_BYTES = "limit_memory_bytes";

    public static final String COL_MANDATORY_ENCODING = "mandatory_encoding";

    public static final String COL_CREATE_TIME = "create_time";
}