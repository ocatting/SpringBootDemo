package com.yu.juc.unsafe;

import sun.misc.Unsafe;

/**
 * @Description: 直接分配内存空间
 * @Author Yan XinYu
 **/
public class DirectMemoryAccessTest {

    public static void main(String[] args) {

        Unsafe unsafe = UnsafeInstance.getUnsafe();

        long oneHundred = 1;
        byte size = 1;

        //生成内存 返回 内存地址
        long directMemoryAddress = unsafe.allocateMemory(size);

        //放入数据到内存中
        unsafe.putAddress(directMemoryAddress,oneHundred);

        //从内存中读取数据
        long address = unsafe.getAddress(directMemoryAddress);

        System.out.println("oneHundred:"+address);
    }


}
