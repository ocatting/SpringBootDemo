package com.yu.handler;

import com.alibaba.fastjson.JSONObject;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class BaseResultHandler implements ResultHandler {

    @Override
    public  List<JSONObject> handler(ResultSet resultSet) {
        if(resultSet == null){
            return null;
        }
        List<JSONObject> resultList = new ArrayList<>();
        try {
            // 获取字段数量
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            // 解析结果
            while (resultSet.next()){
                JSONObject object = new JSONObject();
                for(int i=0;i<columnCount;i++){
                    // 字段名称 字段值
                    object.put(metaData.getColumnLabel(i),resultSet.getObject(i));
                }
                resultList.add(object);
            }
            return resultList;
        } catch (Exception e){
            throw new RuntimeException("解析结果出错: " + e);
        }
    }

}
