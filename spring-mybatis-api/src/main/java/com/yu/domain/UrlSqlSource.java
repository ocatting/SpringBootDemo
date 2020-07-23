package com.yu.domain;

import com.yu.builder.SqlSource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class UrlSqlSource {

    private String id;

    // 参数
    private Map paramsMap;

    // 参数动态解析器
    private SqlSource sqlSource;

}
