package com.yu.convert;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Description: 消息转化器
 * @Author Yan XinYu
 **/
public interface MessageConvert {

    String convert(List<JSONObject> list);

}
