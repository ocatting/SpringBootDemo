package com.sync.utils;

import static org.junit.Assert.*;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-04-13 14:51
 */

public class AesUtilTest {

    public static void main(String[] args) {
        String message = "root";
        String key = "cloerwaxczcz";
        String ciphertext = AesUtil.encrypt(message,key);

        System.out.println("加密后密文为: " + ciphertext);
        System.out.println("解密后明文为:" + AesUtil.decrypt(ciphertext,key));
    }

}