package com.sync.core.domain;

import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;

/**
 * @Description: 表索引
 * @Author: Yan XinYu
 * @Date: 2021-03-31 8:27
 */
@Data
public class SqlIndex {

    private String tableName;
    private String indexName;
    private String columnName;
    private String indexQualifier;
    private short ordinalPosition;
    private boolean nonUnique;
    private short type;

    public boolean equals(SqlIndex sqlIndex){
        if(sqlIndex == null){
            return false;
        }
        if(this == sqlIndex){
            return true;
        }
        return new EqualsBuilder()
                .reflectionAppend(this,sqlIndex)
                .isEquals();

    }

}
