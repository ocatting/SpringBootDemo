package com.demo.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-01-21 14:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogResult implements java.io.Serializable {

    private int fromLineNum;
    private int toLineNum;
    private String logContent;
    private boolean isEnd;

}
