package com.yu.store;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 字节缓冲区
 * @Author Yan XinYu
 **/
public class StoreBuffer {

    private final List<Byte> byteArray;
    private int readIndex = 0;

    public StoreBuffer(){
        this.byteArray = new ArrayList<>(1024);
    }

    public StoreBuffer(int capacity){
        this.byteArray = new ArrayList<>(capacity);
    }

    public void append(byte[] bytes){
        for(byte b:bytes){
            byteArray.add(b);
        }
    }

    /**
     * @param offset 位置
     * @param limit 读取长度
     * @return
     */
    public byte[] read(int offset,int limit){

        return null;
    }



}
