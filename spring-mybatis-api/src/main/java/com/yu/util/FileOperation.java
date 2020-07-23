package com.yu.util;

import java.io.*;
import java.nio.ByteBuffer;

/**
 * @Description: 文件工具
 * @Author Yan XinYu
 **/
public class FileOperation {

    public Object positionLock = new Object();

    private final File targetFile;

    public FileOperation(String path){
        this.targetFile = new File(path);
    }

    public FileOperation(File file){
        this.targetFile = file;
    }

    /**
     * 创建文件与文件夹
     * @param file
     * @throws IOException
     */
    public void create(File file) throws IOException {
        File parent = file.getParentFile();
        if (!parent.exists())
            parent.mkdirs();
        if (!file.exists())
            file.createNewFile();
    }

    /**
     * 追加方式写入数据
     * @param buffer
     * @throws IOException
     */
    public void write(ByteBuffer buffer) throws IOException {
        create(targetFile);
        FileOutputStream out = new FileOutputStream(targetFile, true);
        try{
            synchronized (positionLock){
                out.write(buffer.array());
            }
        } finally {
            out.close();
        }
    }

    /**
     * 读取所有数据
     * @return
     * @throws IOException
     */
    public ByteBuffer read() throws IOException {
        FileInputStream input = new FileInputStream(targetFile);
        int available = input.available();
        ByteBuffer allocate = ByteBuffer.allocate(available);
        try{
            byte[] bytes = new byte[available];
            int temp = 0;
            while((temp = input.read(bytes)) != -1){
                allocate.put(bytes);
            }
        } finally {
            input.close();
        }
        return allocate;
    }

}
