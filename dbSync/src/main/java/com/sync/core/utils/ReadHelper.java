package com.sync.core.utils;

import com.sun.org.apache.bcel.internal.generic.RETURN;
import com.sync.core.JdbcFactory;
import com.sync.core.channel.BufferedRecordExchanger;
import com.sync.core.db.DbLoadFactory;
import com.sync.core.db.Reader;
import com.sync.core.db.Writer;
import com.sync.core.element.Record;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @Description: 读工具
 * @Author: Yan XinYu
 * @Date: 2021-03-09 15:15
 */
@Slf4j
public class ReadHelper {

    private static final Map<String,ReadCThread> CURRENT_MAPPING = new ConcurrentHashMap<>(16);

    private static final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("remove-kill-thread").daemon(true).build(),
            new ThreadPoolExecutor.AbortPolicy());

    static {

        executorService.scheduleAtFixedRate(()->{
            try {
                scanKillThread();
            } catch (Throwable ignored) {}
        },10, 15, TimeUnit.MINUTES);

        Runtime.getRuntime().addShutdownHook(new Thread(executorService::shutdown));
    }

    public static List<Record> readData(String uuid, SyncReadConfig readConfig){

        if (CURRENT_MAPPING.containsKey(uuid)) {
            return CURRENT_MAPPING.get(uuid).getRecords();
        }
        ReadCThread thread = new ReadCThread(readConfig);
        thread.start();
        CURRENT_MAPPING.put(uuid,thread);
        return thread.getRecords();

    }

    public static void writeData(SyncWriteConfig writeConfig,Triple<List<String>, List<Integer>, List<String>> resultSetMetaData, List<Record> list){
        SyncDb syncDb = writeConfig.getSyncDb();
        Writer writer = DbLoadFactory.getWriter(syncDb.getDbType());
        writer.init(writeConfig);
        writer.startWrite(resultSetMetaData,list);
        writer.shutdown();
    }

    public static Triple<List<String>, List<Integer>, List<String>> columnProperty(SyncWriteConfig writeConfig){
        Connection connection = JdbcFactory.getConnection(writeConfig.getSyncDb());
        return DBUtil.getColumnMetaData(connection,writeConfig.getTable(), StringUtils.join(writeConfig.getColumns(),","));
    }

    public static class ReadCThread extends Thread {

        private final SyncReadConfig readConfig;
        private final BufferedRecordExchanger bufferedRecordExchanger;

        public ReadCThread(SyncReadConfig readConfig){
            Assert.notNull(readConfig,"readConfig is null");

            this.readConfig = readConfig;

            int limitMemoryBytes = readConfig.getLimitMemoryBytes() == 0?256*1024*1024:readConfig.getLimitMemoryBytes();
            this.bufferedRecordExchanger = new BufferedRecordExchanger(0,limitMemoryBytes);
        }

        public List<Record> getRecords(){
            List<Record> list = new LinkedList<>();
            for (int i = 0; i < 1000; i++) {
                list.add(bufferedRecordExchanger.getFromReader());
            }
            return list;
        }

        @Override
        public void run() {
            SyncDb readDb = readConfig.getSyncDb();
            Reader reader = DbLoadFactory.getReader(readDb.getDbType());
            reader.init(readConfig);
            reader.startRead(bufferedRecordExchanger);
            bufferedRecordExchanger.shutdown();
        }
    }

    /**
     * 扫描没用的线程关闭
     */
    public static void scanKillThread() {
        Iterator<Map.Entry<String,ReadCThread>> it = CURRENT_MAPPING.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ReadCThread> next = it.next();
            ReadCThread rep = next.getValue();
            if (!rep.isAlive()) {
                it.remove();
            }
        }
    }

}
