package com.upload.file.service;

import org.springframework.core.io.InputStreamSource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface ChunkFile extends InputStreamSource {

    /**
     * 获取文件流
     * @return
     * @throws IOException
     */
    @Override
    InputStream getInputStream() throws IOException;

    /**
     * 文件复制到磁盘
     * @param dest
     * @throws IOException
     * @throws IllegalStateException
     */
    void transferTo(File dest) throws IOException, IllegalStateException;
}
