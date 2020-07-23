package com.upload.file.util;

import com.upload.file.service.FileSeparator;
import org.springframework.util.Assert;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * @Description: 文件复制工具
 * @Author Yan XinYu
 **/
public class FileCopyUtil {

    /**
     * 将分段文件写入到磁盘文件中
     * @param file
     */
    public static final void write(FileSeparator file,String targetPath) throws IOException {
        write(file,new File(targetPath));
    }

    /**
     * 将分段文件写入到磁盘文件中
     * @param file
     */
    public static final void write(FileSeparator file,File targetFile) throws IOException {
        Assert.notNull(file,"FileCopyUtil.write file is null");
        Assert.notNull(targetFile,"FileCopyUtil.write targetFile is null");

        FileOutputStream output = new FileOutputStream(targetFile);
        FileChannel channel = output.getChannel();
        //不存在则先创建一个固定大小的空文件，若空间不够则抛出异常
        if(!targetFile.exists()){
            channel.write(ByteBuffer.allocate(1),file.getTotalSize());
        }
        long position = (file.getIndex()-1) * file.getSize();
        long size = file.getSize();
        FileLock lock = channel.tryLock(position,size,false);
        try{
            // 读取流中数据文件
            InputStream inputStream = file.getInputStream();
            byte[] bytes = new byte[(int) file.getSize()];
            int len = inputStream.read(bytes);
            // 写入磁盘 position = 第几块（2） * 块大小
            ByteBuffer wrap = ByteBuffer.wrap(bytes);
            channel.write(wrap,position);
        } finally {
            lock.close();
        }
    }

    /**
     * 文件分离器，并copy到网络中
     * @param targetFile
     * @param response
     * @param position
     * @param size
     * @throws IOException
     */
    public static final void transferTo(File targetFile, HttpServletResponse response,long position,long size) throws IOException {
        Assert.isNull(targetFile,"FileCopyUtil.write targetFile is null");

        FileOutputStream output = new FileOutputStream(targetFile);
        FileChannel channel = output.getChannel();
        ByteBuffer allocate = ByteBuffer.allocate((int) size);
        channel.read(allocate,position);

        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="+targetFile.getName());
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(allocate.array());
    }


}
