package com.sync.core.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.sync.common.CommonResult;
import com.sync.core.element.Record;
import com.sync.core.exception.RemoteInvokeException;
import com.sync.core.exception.RemoteWriteException;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;
import com.sync.utils.AesUtil;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.List;
/**
 * @Description: 远程调用工具
 * @Author: Yan XinYu
 * @Date: 2021-03-20 16:28
 */
public class RemoteUtil {

    public static final String GET_INCREMENT_VAL_API = "/transfer/getIncrementVal";
    public static final String GET_COLUMNS_API = "/transfer/getColumns";
    public static final String GET_READ_DATA_API = "/transfer/readData";
    public static final String GET_WRITE_DATA_API = "/transfer/writeData";
    public static final String GO_EXEC_SQL_API = "/transfer/execSql";
    public static final String GO_COLUMN_PROPERTY_API = "/write/columnProperty";


    public static final RestTemplate REST_TEMPLATE = new RestTemplateBuilder()
            .setConnectTimeout(Duration.ofSeconds(5)).setReadTimeout(Duration.ofSeconds(60)).build();

    public static String getIncrementVal(String address, SyncDb syncDb, String querySql){

        JSONObject json = new JSONObject();
        json.put("syncdb",syncDb);
        json.put("querySql",querySql);

        String encryptStr = AesUtil.encrypt(json.toJSONString(),AesUtil.SECURITY_KEY);
        ResponseEntity<String> responseEntity = REST_TEMPLATE.postForEntity(address + GET_INCREMENT_VAL_API, encryptStr, String.class);
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            throw new RuntimeException("请求服务发生异常:"+responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }

    public static List<String> getColumns(String address, SyncDb syncDb, String table){

        JSONObject json = new JSONObject();
        json.put("syncdb",syncDb);
        json.put("table",table);

        String encryptStr = AesUtil.encrypt(json.toJSONString(),AesUtil.SECURITY_KEY);

        ResponseEntity<String> responseEntity = REST_TEMPLATE.postForEntity(address + GET_COLUMNS_API, encryptStr, String.class);
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            throw new RuntimeException("请求服务发生异常:"+responseEntity.getStatusCode());
        }
        return JSON.parseObject(responseEntity.getBody(),new TypeReference<List<String>>(){});
    }

    public static Triple<List<String>, List<Integer>, List<String>> getColumnProperty(String address,SyncWriteConfig writeConfig){
        JSONObject json = new JSONObject();
        json.put("writeConfig",writeConfig);

        String encryptStr = AesUtil.encrypt(json.toJSONString(),AesUtil.SECURITY_KEY);

        ResponseEntity<String> responseEntity = REST_TEMPLATE.postForEntity(address + GO_COLUMN_PROPERTY_API, encryptStr, String.class);
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            throw new RuntimeException("请求服务发生异常:"+responseEntity.getStatusCode());
        }
        CommonResult<Triple<List<String>, List<Integer>, List<String>>> result = JSON.parseObject(responseEntity.getBody(),
                new TypeReference<CommonResult<Triple<List<String>, List<Integer>, List<String>>>>() {});
        if(result == null){
            throw new RuntimeException("请求服务执行异常:result is null ");
        }
        if(!result.isSuccess()){
            throw new RuntimeException("请求服务执行异常:"+result.getMessage());
        }
        return result.getData();
    }

    public static List<Record> readData(String uuid, SyncReadConfig readConfig){

        SyncDb syncDb = readConfig.getSyncDb();

        JSONObject json = new JSONObject();
        json.put("uuid",uuid);
        json.put("readConfig",readConfig);

        String encryptStr = AesUtil.encrypt(json.toJSONString(),AesUtil.SECURITY_KEY);

        ResponseEntity<String> responseEntity = REST_TEMPLATE.postForEntity(syncDb.getRemoteAddress() + GET_READ_DATA_API, encryptStr, String.class);
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            throw new RemoteInvokeException("请求readData服务发生异常:"+responseEntity.getStatusCode());
        }
        return JSON.parseObject(responseEntity.getBody(),new TypeReference<List<Record>>(){});
    }

    /**
     *
     * @param uuid 唯一执行标号
     * @param writeConfig 执行配置
     * @param records 记录数
     */
    public static void writeDate(String uuid, SyncWriteConfig writeConfig, Triple<List<String>, List<Integer>, List<String>> resultSetMetaData,List<Record> records) throws Exception {
        SyncDb syncDb = writeConfig.getSyncDb();

        JSONObject json = new JSONObject();
        json.put("uuid",uuid);
        json.put("writeConfig",writeConfig);
        json.put("resultSetMetaData",resultSetMetaData);
        json.put("records",records);

        String encryptStr = AesUtil.encrypt(json.toJSONString(),AesUtil.SECURITY_KEY);

        CommonResult<Boolean> result = RetryUtil.executeWithRetry(() -> {

            ResponseEntity<String> responseEntity = REST_TEMPLATE.postForEntity(syncDb.getRemoteAddress() + GET_WRITE_DATA_API, encryptStr, String.class);
            if (HttpStatus.OK != responseEntity.getStatusCode()) {
                throw new RemoteInvokeException("请求writeData服务发生异常:"+responseEntity.getStatusCode());
            }
            return JSON.parseObject(responseEntity.getBody(), new TypeReference<CommonResult<Boolean>>() {});

        }, 2, 1000L, true);

        if(result == null || !result.isSuccess()){
            // 切换模式，单条发送且没有该数据则覆盖。
            throw new RemoteWriteException("请求writeData服务接收失败:"+JSON.toJSONString(result));
        }
    }

    public static void execSql(String uuid,SyncWriteConfig writeConfig,String execSql){
        SyncDb syncDb = writeConfig.getSyncDb();

        JSONObject json = new JSONObject();
        json.put("uuid",uuid);
        json.put("config",writeConfig);
        json.put("execSql",execSql);

        String encryptStr = AesUtil.encrypt(json.toJSONString(),AesUtil.SECURITY_KEY);

        ResponseEntity<String> responseEntity = REST_TEMPLATE.postForEntity(syncDb.getRemoteAddress() + GO_EXEC_SQL_API, encryptStr, String.class);
        if (HttpStatus.OK != responseEntity.getStatusCode()) {
            throw new RuntimeException("请求execSql服务发生异常:"+responseEntity.getStatusCode());
        }
    }
}
