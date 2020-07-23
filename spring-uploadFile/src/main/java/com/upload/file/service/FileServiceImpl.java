package com.upload.file.service;

import com.upload.file.util.FileCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public void saveFiles(String savePath, MultipartFile... files) throws IOException {
        for(MultipartFile file : files){
            File saveFile = new File(savePath + file.getOriginalFilename());
            file.transferTo(saveFile);
        }
    }

    @Override
    public void saveFiles(String savePath, FileSeparator fileSeparator) throws IOException {
        File file = new File(savePath + fileSeparator.getFileName() + fileSeparator.getFileType());
        FileCopyUtil.write(fileSeparator,file);
    }

    @Override
    public void downFile(String fileId, HttpServletResponse response,long position,long size) throws IOException {
        String path = "d://sss" + fileId;
        FileCopyUtil.transferTo(new File(path),response,position,size);
    }
}
