package com.demo.io;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @Description: 文件追加
 * @Author: Yan XinYu
 * @Date: 2021-01-21 14:28
 */
@Slf4j
public class LogFileAppend {

    /**
     * 读取文件最后多少 MB 的大小
     * @param logFile
     * @param mb
     * @return
     */
    public static String readLogTailByMb(File logFile,int mb){
        return readLogTailByKb(logFile,mb * 1024);
    }

    /**
     * 读取文件最后多少 KB 的大小
     * @param logFile
     * @param kb
     * @return
     */
    public static String readLogTailByKb(File logFile,int kb){
        return readLogTail(logFile,kb * 1024L);
    }

    /**
     * 读取文件最后多少 byte 的大小
     * @param logFile
     * @param byteSize
     * @return
     */
    public static String readLogTail(File logFile,long byteSize) {
        if (!logFile.exists()) {
            return null;
        }
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(logFile, "r");
            long totalSize = raf.length();
            if (totalSize == 0L){
                return null;
            }
            if(totalSize < byteSize){
                byteSize = totalSize;
            }
            long pos = totalSize - byteSize;
            raf.seek(pos);
            byte[] bytes = new byte[(int) byteSize];
            raf.read(bytes);
            return new String(bytes,StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
        } finally {
            if(raf != null){
                try {
                    raf.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }

    /**
     * 读取日志信息，
     * 通过传入行 fromLineNum 从哪行开始读写
     * @param logFile
     * @param fromLineNum
     * @return
     */
    public static LogResult readLog(File logFile,int fromLineNum){

        if (!logFile.exists()) {
            return new LogResult(fromLineNum, 0, "readLog fail, logFile not exists", true);
        }

        StringBuilder logContentBuffer = new StringBuilder(1024);
        int toLineNum = 0;
        LineNumberReader reader = null;
        try {
            reader = new LineNumberReader(new InputStreamReader(new FileInputStream(logFile), StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine())!=null) {
                toLineNum = reader.getLineNumber();
                if (toLineNum >= fromLineNum) {
                    logContentBuffer.append(line).append("\n");
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return new LogResult(fromLineNum, toLineNum, logContentBuffer.toString(), false);
    }

    public static void append(File logFile , String line){
        if (!logFile.exists()) {
            try {
                if (!logFile.createNewFile()) {
                    log.error("create new file:{} is fail",logFile.getName());
                    return;
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                return;
            }
        }

        if (line == null) {
            line = "";
        }

        line += "\r\n";

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(logFile, true);
            fos.write(line.getBytes(StandardCharsets.UTF_8));
            fos.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

    }

}
