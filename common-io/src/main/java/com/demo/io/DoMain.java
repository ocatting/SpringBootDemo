package com.demo.io;

import java.io.File;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-01-21 14:27
 */
public class DoMain {

    public static void main(String[] args) {
        File file = new File("d://1.txt");
        LogResult logResult = LogFileAppend.readLog(file, 0);
        System.out.println(logResult.getLogContent());

        System.out.println("=======================================================");

        LogResult logResult2 = LogFileAppend.readLog(file, 15);
        System.out.println(logResult2.getLogContent());
    }
}
