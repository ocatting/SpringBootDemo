package com.upload.file.service;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public interface FileService {

    /**
     * 保存多个文件
     * @param files
     */
    void saveFiles(String savePath,MultipartFile... files) throws IOException;

    /**
     * 分段文件保存
     * @param savePath
     * @param fileSeparator
     * @throws IOException
     */
    void saveFiles(String savePath, FileSeparator fileSeparator) throws IOException;

    /**
     * 文件下载
     * @param fileId
     * @param response
     */
    void downFile(String fileId, HttpServletResponse response,long position,long size) throws IOException;

}
