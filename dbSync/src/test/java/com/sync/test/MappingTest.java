package com.sync.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sync.core.element.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;
import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * @Description: 字段映射
 * @Author: Yan XinYu
 * @Date: 2021-04-15 10:30
 */
@Slf4j
public class MappingTest {

    /**
     * 车辆抓拍字段映射
     */
    @Test
    public void carColumnMappingTest(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("communityid","community_code");
        jsonObject.put("devicecode","device_id");
        jsonObject.put("collectionpointname","bayonet_id");
        jsonObject.put("carsize","car_type");
        jsonObject.put("platenumber","license");
        jsonObject.put("cardirect","car_direct");
        jsonObject.put("collecttime","collect_time");
        jsonObject.put("uploadtime","storage_time");
        jsonObject.put("fileurl","photo_address");
        jsonObject.put("platecolorname","license_color");

        System.out.println(jsonObject.toJSONString());

    }

    /**
     * 人脸字段映射
     */
    @Test
    public void faceColumnMappingTest(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("community_id","community_code");
        jsonObject.put("device_id","device_id");
        jsonObject.put("device_name","device_name");
        jsonObject.put("collecttime","collect_time");
        jsonObject.put("storage_time","storage_time");
        jsonObject.put("big_photo","photo_address");
        jsonObject.put("remark","remark");
        jsonObject.put("origin_id","original_id");

        System.out.println(jsonObject.toJSONString());

    }

    /**
     * wifi嗅探字段映射
     */
    @Test
    public void wifiMapping(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("collectdeviceid","collect_device_id");
        jsonObject.put("collectmacaddress","collect_mac_address");
        jsonObject.put("collecttime","collect_time");
        jsonObject.put("communityid","community_id");
        jsonObject.put("communityname","community_name");
        jsonObject.put("fieldintensity","field_intensity");
        jsonObject.put("latitude","latitude");
        jsonObject.put("longitude","longitude");
        jsonObject.put("storagetime","storage_time");
        jsonObject.put("xaxis","x_axis");
        jsonObject.put("yaxis","y_axis");

        System.out.println(jsonObject.toJSONString());

    }

    /**
     * 人脸数据映射
     */
    @Test
    public void faceMapping(){

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("basephoneaddress","basephone_address");
        jsonObject.put("basementcode","basement_code");
        jsonObject.put("collecttime","collect_time");
        jsonObject.put("communityid","community_code");
        jsonObject.put("communityname","community_name");
        jsonObject.put("createtime","create_time");
        jsonObject.put("devicecode","device_id");
        jsonObject.put("identifies","identifies");
        jsonObject.put("name","name");
        jsonObject.put("remark","remark");
        jsonObject.put("similary","similary");

        System.out.println(jsonObject.toJSONString());
    }

    public static void main(String[] args) throws ParseException {

//        Date atime = ColumnCast.string2Date(new StringColumn("atime", "1618553574000"));
        Date atime = ColumnCast.string2Date(new StringColumn("atime", "2021-04-16 14:12:54"));
        FastDateFormat instance = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
        System.out.println(instance.format(atime));

//        List<Record> records = new ArrayList<>();
//
//        Record record = new Record();
//        record.addColumn(new StringColumn("as","12323"));
//        record.addColumn(new DateColumn("atime",new Date()));
//        record.addColumn(new DoubleColumn("adou",12.23));
//
//        records.add(record);

//        String atime = JSON.toJSONString(record.getColumn("atime"), SerializerFeature.WriteClassName);
//        Column column = JSON.parseObject(atime, Column.class);
//        if (column instanceof DateColumn){
//            System.out.println("对");
//        }

//        Map<String,Column> columns = new HashMap<>();
//        Column atime1 = record.getColumn("atime");
//        columns.put("QW",atime1);
//
//        String maps = JSON.toJSONString(columns, SerializerFeature.WriteClassName);
//        System.out.println(maps);
//
//        Map<String, Column> stringColumnMap = JSON.parseObject(maps, new TypeReference<Map<String, Column>>() {});
//
//        Column qw = stringColumnMap.get("QW");
//        if(qw instanceof DateColumn){
//            System.out.println("对");
//        }

//        String recordJson = JSON.toJSONString(records, SerializerFeature.WriteClassName);
//        System.out.println(recordJson);
//        List<Record> record1 = JSON.parseObject(recordJson, new TypeReference<List<Record>>() {});
//        System.out.println(record1);


//        JSONObject json = new JSONObject();
//        json.put("uuid","qweq");
//        json.put("writeConfig","writeConfig");
//        json.put("resultSetMetaData","resultSetMetaData");
//        json.put("records",JSON.toJSONString(records, SerializerFeature.WriteClassName));
//
//        String classJson = JSON.toJSONString(json, SerializerFeature.WriteClassName);
//        System.out.println(classJson);
//
//        JSONObject jsonObject = JSON.parseObject(classJson);
//        List<Record> records2 = JSON.parseObject(jsonObject.getString("records"), new TypeReference<List<Record>>() {});
//        System.out.println(records2.toString());
    }
}
