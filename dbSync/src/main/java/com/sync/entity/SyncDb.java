package com.sync.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Description:  数据库连接配置
 * @Author: Yan XinYu
 * @Date:    2021-03-23 9:19
 */
@Data
@TableName(value = "t_sync_db")
public class SyncDb {

    @TableId(value = "id", type = IdType.INPUT)
    private Integer id;

    /**
     * 名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 数据库类型
     */
    @TableField(value = "db_type")
    private String dbType;

    /**
     * jdbc地址连接
     */
    @TableField(value = "jdbc_url")
    private String jdbcUrl;

    /**
     * 驱动
     */
    @TableField(value = "driver")
    private String driver;

    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;

    /**
     * 密码
     */
    @TableField(value = "`password`")
    private String password;

    /**
     * 开启远程模式：0-false,1-true
     */
    @TableField(value = "open_remote")
    private Boolean openRemote;

    /**
     * 远程调用地址
     */
    @TableField(value = "remote_address")
    private String remoteAddress;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 描述
     */
    @TableField(value = "remark")
    private String remark;

    public static final String COL_ID = "id";

    public static final String COL_NAME = "name";

    public static final String COL_DB_TYPE = "db_type";

    public static final String COL_JDBC_URL = "jdbc_url";

    public static final String COL_DRIVER = "driver";

    public static final String COL_USERNAME = "username";

    public static final String COL_PASSWORD = "password";

    public static final String COL_OPEN_REMOTE = "open_remote";

    public static final String COL_REMOTE_ADDRESS = "remote_address";

    public static final String COL_CREATE_TIME = "create_time";

    public static final String COL_REMARK = "remark";

    /**
     * 不为表字段
     */
    @TableField(exist = false)
    private String host;
    @TableField(exist = false)
    private String port;
    @TableField(exist = false)
    private String database;
    @TableField(exist = false)
    private String params;

    public void setJdbcUrl(String jdbcUrl){
        this.jdbcUrl = jdbcUrl;

        int pos, pos1, pos2;
        String connUri;

        if(jdbcUrl == null || !jdbcUrl.startsWith("jdbc:")
                || (pos1 = jdbcUrl.indexOf(':', 5)) == -1) {
            throw new IllegalArgumentException("Invalid JDBC url.");
        }

        if((pos2 = jdbcUrl.indexOf(';', pos1)) == -1){
            connUri = jdbcUrl.substring(pos1 + 1);
        } else {
            connUri = jdbcUrl.substring(pos1 + 1, pos2);
            params = jdbcUrl.substring(pos2 + 1);
        }

        if(connUri.startsWith("//")){
            if((pos = connUri.indexOf('/', 2)) != -1){
                host = connUri.substring(2, pos);
                String databaseAndParmas = connUri.substring(pos + 1);
                if((pos = databaseAndParmas.indexOf("?")) != -1){
                    params = databaseAndParmas.substring(pos + 1);
                    database = databaseAndParmas.substring(0,pos);
                } else {
                    database = databaseAndParmas;
                }
                if((pos = host.indexOf(':')) != -1){
                    port = host.substring(pos + 1);
                    host = host.substring(0, pos);
                }
            }
        } else {
            database = connUri;
        }
    }
}