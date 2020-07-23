package com.upload.file.controller;

import com.upload.file.service.FileSeparator;
import com.upload.file.service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private FileService fileService;

    private String savePath = "d://";

    /**
     * 调用上传文件接口必须指定POST 请求 ，并携带 multipart/form-data 参数
     * @return
     */
    @PostMapping(value = "/uploadFile",headers ={"Content-Type=multipart/form-data"})
    public Object uploadFile(MultipartFile[] files) throws IOException {
        Assert.notEmpty(files,"files is null");

        fileService.saveFiles(savePath,files);
        return "success";
    }

    /**
     * 分段文件上传
     * @param fileSeparator
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/uploadFileSeparator",headers ={"Content-Type=multipart/form-data"} )
    public Object uploadFile(FileSeparator fileSeparator) throws IOException {
        Assert.notNull(fileSeparator,"fileSeparator is null");

        fileService.saveFiles(savePath,fileSeparator);
        return "success";
    }

    /**
     * 获取文件信息，为大文件下载方便
     * @param fileId
     * @return
     */
    @GetMapping("/queryFileInfo")
    public Object queryFileInfo(String fileId){

        return "succsss";
    }

    /**
     * 下载文件
     * @param fileId
     * @param position
     * @param size
     * @param response
     * @throws IOException
     */
    @PostMapping("/downFile")
    public void downFile(String fileId,Long position,Long size, HttpServletResponse response) throws IOException {
        Assert.hasLength(fileId,"fileId is null");
        Assert.isNull(position,"position is null");
        Assert.isNull(size,"size is null");

        fileService.downFile(fileId,response,position,size);
    }

}
