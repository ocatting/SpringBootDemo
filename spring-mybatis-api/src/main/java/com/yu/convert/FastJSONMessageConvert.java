package com.yu.convert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class FastJSONMessageConvert implements MessageConvert{

    @Override
    public String convert(List<JSONObject> list) {
        return JSON.toJSONString(list);
    }
}
