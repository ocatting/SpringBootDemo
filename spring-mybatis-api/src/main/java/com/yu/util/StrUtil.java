package com.yu.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class StrUtil {

    /**
     * 替换所有特殊符号
     * \n 回车(\u000a)
     * \t 水平制表符(\u0009)
     * \s 空格(\u0008)
     * \r 换行(\u000d)
     * @param text
     * @return
     */
    public static String replaceAllSymbol(String text){
        if(text == null ||text.isEmpty()){
            return null;
        }
        Pattern p = Pattern.compile("\\t|\r|\n");
        Matcher matcher = p.matcher(text);
        String result = matcher.replaceAll("");
        return result.trim();
    }

}
