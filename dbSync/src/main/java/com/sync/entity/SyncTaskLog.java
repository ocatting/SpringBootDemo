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
 * @Date:    2021-03-10 10:44
 */
    
    
/**
    * 任务日志
    */
@Data
@TableName(value = "t_sync_task_log")
public class SyncTaskLog {
    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    @TableField(value = "task_id")
    private Integer taskId;

    @TableField(value = "trigger_time")
    private Date triggerTime;

    @TableField(value = "trigger_code")
    private String triggerCode;

    /**
     * 单位毫秒(ms)
     */
    @TableField(value = "handle_time")
    private Integer handleTime;

    /**
     * 处理状态 succsss/fail
     */
    @TableField(value = "handle_code")
    private String handleCode;

    public static final String COL_ID = "id";

    public static final String COL_TASK_ID = "task_id";

    public static final String COL_TRIGGER_TIME = "trigger_time";

    public static final String COL_TRIGGER_CODE = "trigger_code";

    public static final String COL_HANDLE_TIME = "handle_time";

    public static final String COL_HANDLE_CODE = "handle_code";
}