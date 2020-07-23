package com.nio.demo;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * @Description: FileChannel 的使用
 * FileInputStream 和FileChannel最终均调用了native的ReadFile方法，本质是一样的！
 * FileOutputStream
 * 提供的模板方法为，队列数据持久化
 * 1.有数据写入，将数据放入磁盘队列中;
 * 首先存放队列文件，20bytes存放topic,{{tag(4),offset(8),msgSize(8)一条消息},...}
 * commitlog 数据文件不停追加(或者可固定大小){50k,100k,200k,40k}
 *
 * @Author Yan XinYu
 **/
public class FileChannelDemo {

    /**
     * 将对象写入文件，最好捕捉采用close();
     */
    public void ObjectOutputStreamTest() throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream(new File("D://obj。test")));
        objectOutputStream.writeObject(new User("张三", 18));
        objectOutputStream.close();
    }

    /**
     * 对象字节码转换
     * @param obj
     * @throws IOException
     */
    public void ObjectOutputStreamTest(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        // Obj转字节码
        byte[] bytes = bos.toByteArray();

        oos.close();
        bos.close();

        ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
        ObjectInputStream ois = new ObjectInputStream (bis);
        // 字节码转Obj
        Object newObj = ois.readObject();

        bis.close();
        ois.close();

    }

    /**
     * FileInputStream 典型代码
     */
    public void FileInputStreamTest(){
        System.out.println(System.getProperty("user.dir"));
        File file = new File(System.getProperty("user.dir") + "/src/oio/file.txt");
        System.out.println("file name: " + file.getName());

        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            int len = inputStream.read(bytes);
            System.out.println("bytes len :" + len + " detail: " + new String(bytes));
        } catch (IOException e) {
            e.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }


    /**
     * FileChannel 典型代码
     * @throws IOException
     */
    public void FileChannelTest() throws IOException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(4);//读取4个字节
        Path path = Paths.get(System.getProperty("user.dir") + "/assets/file.txt");
        FileChannel fileChannel = FileChannel.open(path, StandardOpenOption.READ);//只读
        int len = fileChannel.read(byteBuffer);//
        while (len != -1) {
            byteBuffer.flip();//
            while (byteBuffer.hasRemaining()){
                System.out.print((char) byteBuffer.get());//
            }
            byteBuffer.clear();//
            len = fileChannel.read(byteBuffer);//
        }
    }

    public static void main(String[] args) {

    }
}
