package com.sync.utils;

import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;
import java.util.Properties;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-03-12 11:46
 */

public class StringHelper {

    private static PropertyPlaceholderHelper propertyPlaceholderHelper = new PropertyPlaceholderHelper("#{", "}", ":", false);

    /**
     * 占位符替换
     * @param value text
     * @param properties 需要替换的参数
     * @return 结果
     */
    public static String replacePlaceholders(String value, final Properties properties){
        return propertyPlaceholderHelper.replacePlaceholders(value, properties);
    }

    /**
     * 占位符替换
     * @param value test
     * @param params 需要替换的参数
     * @return 结果
     */
    public static String replacePlaceholders(String value, final Map<String,String> params){
        Properties properties = new Properties();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            properties.put(entry.getKey(), entry.getValue());
        }
        return propertyPlaceholderHelper.replacePlaceholders(value, properties);
    }
}
