package com.yu.algorithm;

/**
 * @Description: 字符串匹配
 * @Author Yan XinYu
 **/
public class StringMatch {

    /**
     * 通常的匹配模式 返回下标
     * @param main
     * @param sub
     */
    public static int generalMatch(String main,String sub){
        char[] v1 = main.toCharArray();
        char[] v2 = sub.toCharArray();
        int i = 0; //主串位置
        int j = 0; //子串位置
        while (i < v1.length && j < v2.length){
            if(v1[i] == v2[j]){
                i++;
                j++;
            } else {
                i = i-j+1;
                j = 0;
            }
        }
        if(j >= v2.length){
            return i - v2.length;
        }else {
            return -1;
        }
    }

    /**
     * kmp 匹配算法
     * @param main
     * @param sub
     * @return
     */
    public static int kmpMatch(String main,String sub){
        char[] v1 = main.toCharArray();
        char[] v2 = sub.toCharArray();

        // 计算sub next 回退表
        int[] next = kmpNext(v2);
        System.out.println(next);

        return -1;
    }

    // abcabx 0 1
    public static int[] kmpNext(char[] v2){
        int [] next = new int[v2.length];
        int i = 0,j = 0;
        next[0] = 0;
        while (i < v2.length){
            if(j == 0 || v2[i+1] == v2[j]){
                ++i;
                ++j;
                next[i] = j;
            }else{
                j = next[j-1];
            }
        }
        return next;
    }

    public static void main(String[] args) {

//        String main = "goodgooles";
//        String sub = "gooles";
//        int i = StringMatch.generalMatch(main, sub);
//        System.out.println(i);

        String main = "goodgooles";
        String sub = "abcabx";
        int[] i = StringMatch.kmpNext(sub.toCharArray());
        System.out.println(i);

    }

}
