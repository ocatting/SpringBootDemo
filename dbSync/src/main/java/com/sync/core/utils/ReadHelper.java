package com.sync.core.utils;

import com.sync.core.JdbcFactory;
import com.sync.core.TaskCollector;
import com.sync.core.channel.BufferedRecordExchanger;
import com.sync.core.db.DbLoadFactory;
import com.sync.core.db.Reader;
import com.sync.core.db.Writer;
import com.sync.core.domain.RemoteReadVo;
import com.sync.core.element.Record;
import com.sync.entity.SyncDb;
import com.sync.entity.SyncReadConfig;
import com.sync.entity.SyncWriteConfig;
import com.sync.utils.LockUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.util.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Description: 读工具
 * @Author: Yan XinYu
 * @Date: 2021-03-09 15:15
 */
@Slf4j
public class ReadHelper {

    /**
     * CURRENT_MAPPING 会存在死线程积压
     */
    private static final Map<String,ReadCThread> CURRENT_MAPPING = new ConcurrentHashMap<>(16);

    public static RemoteReadVo readData(String uuid, SyncReadConfig readConfig) {

        ReadCThread oldThread = CURRENT_MAPPING.get(uuid);

        if (oldThread != null) {
            List<Record> records = CURRENT_MAPPING.get(uuid).getRecords();
            return RemoteReadVo.builder().uuid(uuid)
                    .alive(oldThread.isAlive())
                    .records(records)
                    .build();
        }

        ReadCThread thread = new ReadCThread(uuid,readConfig);
        thread.start();
        CURRENT_MAPPING.put(uuid,thread);
        List<Record> records = thread.getRecords();

        return RemoteReadVo.builder().uuid(uuid)
                .alive(thread.isAlive())
                .records(records)
                .build();
    }

    public static void writeData(SyncWriteConfig writeConfig, ListTriple resultSetMetaData, List<Record> list){
        SyncDb syncDb = writeConfig.getSyncDb();
        Writer writer = DbLoadFactory.getWriter(syncDb.getDbType());
        writer.init(null,writeConfig);
        writer.startWrite(resultSetMetaData,list);
        writer.shutdown();
    }

    public static ListTriple columnProperty(SyncWriteConfig writeConfig){
        Connection connection = JdbcFactory.getConnection(writeConfig.getSyncDb());
        return DBUtil.getColumnMetaData(connection,writeConfig.getTable(), StringUtils.join(writeConfig.getColumns(),","));
    }

    public static class ReadCThread extends Thread {

        /**
         * 默认等待 re.next() 的最大时间，单位(nano)
         */
        public final static long DEFAULT_MAX_RS_NEXT_WAIT_TIME = 2 * 60 * 60 * 1000 * 100;

        private final String uuid;
        private final SyncReadConfig readConfig;
        private final BufferedRecordExchanger bufferedRecordExchanger;
        private final long maxRsNextWaitTime;
        private Reader reader;

        public ReadCThread(String uuid,SyncReadConfig readConfig){
            this(uuid,readConfig,DEFAULT_MAX_RS_NEXT_WAIT_TIME);
        }

        public ReadCThread(String uuid,SyncReadConfig readConfig,long maxRsNextWaitTime){

            Assert.notNull(readConfig,"readConfig is null");

            this.uuid = uuid;
            this.readConfig = readConfig;
            this.maxRsNextWaitTime = maxRsNextWaitTime;

            int limitMemoryBytes = readConfig.getLimitMemoryBytes() == 0?256*1024*1024:readConfig.getLimitMemoryBytes();
            this.bufferedRecordExchanger = new BufferedRecordExchanger(0,limitMemoryBytes);
            super.setName("read-"+uuid);
        }

        public List<Record> getRecords(){
            List<Record> list = new LinkedList<>();
            try {
                Record record;
                while ((record = bufferedRecordExchanger.getFromReader()) != null) {
                    list.add(record);
                    if (list.size() >= 1000) {
                        return list;
                    }
                }
                if (!list.isEmpty()) {
                    return list;
                }
            } catch (Exception e) {
                log.error("ignore this e:",e);
            }
            return list;
        }

        public boolean isTimeout(){
            if(this.reader == null ){
                return true;
            }
            // nano 纳秒
            long rsNextLastTime = reader.getRsNextLastTime();
            long rsNextWaitTime = System.nanoTime() - rsNextLastTime;
            return maxRsNextWaitTime > rsNextWaitTime;
        }

        public void shutdown(){
            if(this.reader != null){
                this.reader.shutdown();
            }
            if (this.bufferedRecordExchanger != null) {
                this.bufferedRecordExchanger.shutdown();
            }
        }

        @Override
        public void run() {
            SyncDb readDb = readConfig.getSyncDb();
            this.reader = DbLoadFactory.getReader(readDb.getDbType());
            this.reader.init(readConfig);
            this.reader.startRead(bufferedRecordExchanger);
            this.reader.shutdown();
            bufferedRecordExchanger.terminate();
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
            if(rep.isTimeout()){
                rep.shutdown();
                it.remove();
            }
        }
    }

}
