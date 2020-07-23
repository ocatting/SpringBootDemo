package com.yu.algorithm;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class BinaryArray {
    public static int intA[][]={{1,2},{2,3},{3,4,5}};

    public static void main(String[] args) {
        for (int i : BinaryArray.intA[1]) {
            System.out.println(i);
        }
        System.out.println(BinaryArray.intA[1].toString());

    }
}
