package com.sync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;
import java.util.Date;import java.util.List;
import lombok.Data;

/**
 * @Description: ${description}
 * @Author: Yan XinYu
 * @Date: 2021-04-11 9:58
 */


@Data
@TableName(value = "t_sync_write_config")
public class SyncWriteConfig {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 表名称
     */
    @TableField(value = "`table`")
    private String table;

    /**
     * 写入模式
     */
    @TableField(value = "write_mode")
    private String writeMode;

    /**
     * 前置执行SQL
     */
    @TableField(value = "before_sql")
    private String beforeSql;

    /**
     * 执行SQL
     */
    @TableField(value = "exec_sql")
    private String execSql;

    /**
     * 后置执行SQL
     */
    @TableField(value = "post_sql")
    private String postSql;

    /**
     * 字段映射JSON
     */
    @TableField(value = "mapping_json")
    private String mappingJson;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    public static final String COL_ID = "id";

    public static final String COL_TABLE = "table";

    public static final String COL_WRITE_MODE = "write_mode";

    public static final String COL_BEFORE_SQL = "before_sql";

    public static final String COL_EXEC_SQL = "exec_sql";

    public static final String COL_POST_SQL = "post_sql";

    public static final String COL_MAPPING_JSON = "mapping_json";

    public static final String COL_CREATE_TIME = "create_time";

    @TableField(exist = false)
    private SyncDb syncDb;
    /**
     * 写数据字段
     */
    @TableField(exist = false)
    private List<String> columns;
}