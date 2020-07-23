package com.nio.demo;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 模糊查找
 * @Author Yan XinYu
 **/
public class FuzzySearchDemo {

    /**
     * 模糊查找
     * @param name
     * @param list
     * @return
     */
    public List search(String name,List<String> list){
        List<String> results = new LinkedList();
        Pattern pattern = Pattern.compile(name);
        for(int i=0; i < list.size(); i++){
            Matcher matcher = pattern.matcher(list.get(i));
            if(matcher.matches()){
                results.add(list.get(i));
            }
        }
        return results;
    }
}
