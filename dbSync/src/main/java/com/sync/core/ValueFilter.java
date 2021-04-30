package com.sync.core;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * @Description: 字段值过滤器，后面扩展吧，不用把这里逻辑注释掉
 * @Author: Yan XinYu
 * @Date: 2021-04-16 9:09
 */
public class ValueFilter {

    private static final Map<Class<?>, List<Object>> VI = new HashMap<>();

    static {

        VI.put(String.class, Collections.singletonList("NULL"));

    }

    public static String StringFilter(String val){
        if(StringUtils.isEmpty(val)){
            return val;
        }
        List<Object> objs = VI.get(String.class);
        for (Object obj : objs) {
            if(val.equals(obj)){
                return null;
            }
        }
        return val;
    }
}
