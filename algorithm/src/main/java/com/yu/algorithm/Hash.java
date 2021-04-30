package com.yu.algorithm;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @Author: Yan XinYu
 * @Date: 2021-04-26 11:37
 */

public class Hash {

    /**
     * 有效的字母异位词
     * 异位词：两个数字符一样，但是乱序
     * @param s
     * @param t
     * @return
     */
    public boolean isAnagram(String s, String t) {
        if(s == t){
            return true;
        }
        if(s.length() != t.length()){
            return false;
        }
        char[] s1 = s.toCharArray();
        char[] t1 = t.toCharArray();
        Arrays.sort(s1);
        Arrays.sort(t1);
        return Arrays.equals(s1,t1);
    }

    /**
     * 有效的字母异位词
     * 异位词：两个数字符一样，但是乱序
     * @param s
     * @param t
     * @return
     */
    public boolean isAnagram2(String s, String t) {
        if(s == t){
            return true;
        }
        if(s.length() != t.length()){
            return false;
        }
        Map<Character,Integer> map = new HashMap<>();
        for(int i = 0;i<s.length();i++){
            char ch = s.charAt(i);
            map.put(ch,map.getOrDefault(ch,0)+1);
        }
        for(int i=0;i<t.length();i++){
            char ch = t.charAt(i);
            map.put(ch,map.getOrDefault(ch,0)-1);
            if(map.get(ch)<0){
                return false;
            }
        }
        return true;
    }
}
