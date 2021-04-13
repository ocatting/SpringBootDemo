package com.sync.core.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @Description: 数据库字段描述
 * @Author: Yan XinYu
 * @Date: 2021-03-30 23:32
 */
@Data
public class SqlColumn {

    public String tableName;
    public String columnName;
    public String columnType;
    public String nullAble;
    public String isAutoincrement;
    public String remark;
    public String columnDef;
    public String columnTypeText;
    public int dataType;
    public int columnSize;
    public int digitSize;

    public String after;

    public boolean equals(SqlColumn column){
        if (column == null) { return false; }
        if (column == this) { return true; }
        return new EqualsBuilder()
                .reflectionAppend(this,column)
                .isEquals();
    }

}
