package com.sync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description:  ${description}
 * @Author: Yan XinYu
 * @Date:    2021-04-12 15:25
 */


/**
 * 数据库同步任务信息
 */
@Data
@TableName(value = "t_sync_task_info")
public class SyncTaskInfo {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务组ID
     */
    @TableField(value = "group_id")
    private Integer groupId;

    /**
     * 任务执行CRON
     */
    @TableField(value = "task_cron")
    private String taskCron;

    /**
     * 任务描述
     */
    @TableField(value = "task_desc")
    private String taskDesc;

    /**
     * 读数据库配置
     */
    @TableField(value = "read_db_id")
    private Integer readDbId;

    /**
     * 读配置
     */
    @TableField(value = "read_conf_id")
    private Integer readConfId;

    /**
     * 写数据库配置
     */
    @TableField(value = "write_db_id")
    private Integer writeDbId;

    /**
     * 写配置
     */
    @TableField(value = "write_conf_id")
    private Integer writeConfId;

    /**
     * 模式：0-local，1-http
     */
    @TableField(value = "model")
    private Integer model;

    /**
     * 调度状态：0-停止，1-运行
     */
    @TableField(value = "trigger_status")
    private Integer triggerStatus;

    /**
     * 上次调度时间
     */
    @TableField(value = "trigger_last_time")
    private Date triggerLastTime;

    /**
     * 下次调度时间
     */
    @TableField(value = "trigger_next_time")
    private Date triggerNextTime;

    /**
     * 增量字段(注意该字段确保存在索引)
     */
    @TableField(value = "increment_col")
    private String incrementCol;

    /**
     * 增量值
     */
    @TableField(value = "increment_val")
    private String incrementVal;

    /**
     * 增量类型，1 自增id,2 time
     */
    @TableField(value = "increment_type")
    private Integer incrementType;

    /**
     * 备注
     */
    @TableField(value = "remark")
    private String remark;

    public static final String COL_ID = "id";

    public static final String COL_GROUP_ID = "group_id";

    public static final String COL_TASK_CRON = "task_cron";

    public static final String COL_TASK_DESC = "task_desc";

    public static final String COL_READ_DB_ID = "read_db_id";

    public static final String COL_READ_CONF_ID = "read_conf_id";

    public static final String COL_WRITE_DB_ID = "write_db_id";

    public static final String COL_WRITE_CONF_ID = "write_conf_id";

    public static final String COL_MODEL = "model";

    public static final String COL_TRIGGER_STATUS = "trigger_status";

    public static final String COL_TRIGGER_LAST_TIME = "trigger_last_time";

    public static final String COL_TRIGGER_NEXT_TIME = "trigger_next_time";

    public static final String COL_INCREMENT_COL = "increment_col";

    public static final String COL_INCREMENT_VAL = "increment_val";

    public static final String COL_INCREMENT_TYPE = "increment_type";

    public static final String COL_REMARK = "remark";

    @TableField(exist = false)
    private SyncReadConfig syncReadConfig;
    @TableField(exist = false)
    private SyncWriteConfig syncWriteConfig;
    /**
     * 增量最大值
     */
    @TableField(exist = false)
    private String maxVal;
}