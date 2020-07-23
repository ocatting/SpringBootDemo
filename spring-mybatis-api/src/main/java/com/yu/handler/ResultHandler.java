package com.yu.handler;

import com.alibaba.fastjson.JSONObject;

import java.sql.ResultSet;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface ResultHandler {

    List<?> handler(ResultSet resultSet);

}
