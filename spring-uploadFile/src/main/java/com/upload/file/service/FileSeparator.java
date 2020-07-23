package com.upload.file.service;

import com.upload.file.util.FileCopyUtil;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description: 文件分离器
 * @Author Yan XinYu
 **/
public class FileSeparator implements ChunkFile{

    private String fileName;

    private String fileType;

    // 事务ID
    private String transactionId;

    // 文件总大小
    private long totalSize;

    // 当前块大小
    private long size;

    // 分成了多少块
    private int chunkSize;

    // 当前块位置
    private int index;

    // 文件内容
    private MultipartFile file;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return file.getInputStream();
    }

    public void transferTo(String dest) throws IOException, IllegalStateException{
        FileCopyUtil.write(this,dest);
    }

    @Override
    public void transferTo(File dest) throws IOException, IllegalStateException{
        FileCopyUtil.write(this,dest);
    }

}
