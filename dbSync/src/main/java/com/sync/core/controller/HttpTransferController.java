package com.sync.core.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sync.common.CommonResult;
import com.sync.core.JdbcFactory;
import com.sync.core.domain.RemoteReadVo;
import com.sync.core.element.Record;
import com.sync.core.utils.DBUtil;
import com.sync.core.utils.ListTriple;
import com.sync.core.utils.ReadHelper;
import com.sync.core.utils.ReaderUtil;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;
import com.sync.utils.AesUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @Description: http传输控制
 * @Author: Yan XinYu
 * @Date: 2021-03-19 16:57
 */
@Slf4j
@RestController
@RequestMapping("/transfer")
public class HttpTransferController {

    @PostMapping("/getIncrementVal")
    public String getIncrementVal(@RequestBody String encryptStr) {
        JSONObject json = JSON.parseObject(AesUtil.decrypt(encryptStr,AesUtil.SECURITY_KEY));
        SyncDb syncDb = json.getObject("syncDb",SyncDb.class);
        String querySql = json.getString("querySql");
        log.info("getIncrementVal,jdbc:{},querysql:{}",syncDb.getJdbcUrl(),querySql);
        return ReaderUtil.readIncrementValByOne(syncDb, querySql);
    }

    @PostMapping("/getColumns")
    public List<String> getColumns(@RequestBody String encryptStr){

        JSONObject json = JSON.parseObject(AesUtil.decrypt(encryptStr,AesUtil.SECURITY_KEY));
        SyncDb syncDb = json.getObject("syncDb",SyncDb.class);
        String table = json.getString("table");

        log.info("getColumns,jdbc:{},table:{}",syncDb.getJdbcUrl(),table);

        Connection connection = JdbcFactory.getConnection(syncDb);
        return DBUtil.getTableColumnsByConn(connection, table);
    }

    @PostMapping("/execSql")
    public CommonResult<?> execSql(@RequestBody String encryptStr) throws SQLException {

        JSONObject json = JSON.parseObject(AesUtil.decrypt(encryptStr,AesUtil.SECURITY_KEY));

        SyncWriteConfig writeConfig = json.getObject("config",SyncWriteConfig.class);
        SyncDb syncDb = writeConfig.getSyncDb();

        String execSql = json.getString("execSql");

        log.info("execSql,jdbc:{},execSql:{}",syncDb.getJdbcUrl(),execSql);

        Connection connection = JdbcFactory.getConnection(syncDb);
        DBUtil.executeSql(connection,execSql);
        return CommonResult.success();
    }

    @PostMapping("/readData")
    public String readData(@RequestBody String encryptStr){
        JSONObject json = JSON.parseObject(AesUtil.decrypt(encryptStr,AesUtil.SECURITY_KEY));
        String uuid = json.getString("uuid");
        SyncReadConfig readConfig = json.getObject("readConfig",SyncReadConfig.class);

        log.info("readData,uuid:{},readConfig:{}",uuid,readConfig);

        RemoteReadVo vo = ReadHelper.readData(uuid, readConfig);
        return JSON.toJSONString(vo, SerializerFeature.WriteClassName);
    }

    @PostMapping("/writeData")
    public CommonResult<Boolean> writeData(@RequestBody String encryptStr){
        JSONObject json = JSON.parseObject(AesUtil.decrypt(encryptStr,AesUtil.SECURITY_KEY));
        String uuid = json.getString("uuid");
        SyncWriteConfig writeConfig = json.getObject("writeConfig", SyncWriteConfig.class);
        ListTriple resultSetMetaData = json.getObject("resultSetMetaData", new TypeReference<ListTriple>() {});
        List<Record> records = JSON.parseObject(json.getString("records"), new TypeReference<List<Record>>() {});
        try {
            log.info("writeData,uuid:{},writeConfig:{}",uuid,writeConfig);
            ReadHelper.writeData(writeConfig,resultSetMetaData,records);
            return CommonResult.success(true);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @PostMapping("/write/columnProperty")
    public CommonResult<ListTriple> columnProperty(@RequestBody String encryptStr){
        JSONObject json = JSON.parseObject(AesUtil.decrypt(encryptStr,AesUtil.SECURITY_KEY));
        SyncWriteConfig writeConfig = json.getObject("writeConfig", SyncWriteConfig.class);

        try {
            log.info("write/columnProperty,writeConfig:{}",writeConfig);
            ListTriple resultSetMetaData = ReadHelper.columnProperty(writeConfig);
            return CommonResult.success(resultSetMetaData);
        } catch (Exception e) {
            return CommonResult.failed(e.getMessage());
        }
    }

    @Data
    public static class GatherRecord {

        /**
         * 写数据库配置
         */
        private String writeConfig;

        /**
         * 行记录
         */
        private List<Record> records;

    }

}
