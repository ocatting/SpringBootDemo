package com.undertow.demo;

import sun.applet.Main;

/**
 * @Description:
 * @Author Yan XinYu
 **/
public class Test {

    protected interface FunctionEx {
        void apply(String a);
    }

    public void adddd(String a){
        System.out.println("adddd---" + a);
        add(a,this::doAdd);
    }

    public String doAdd(String a){
        System.out.println("doAdd---" + a);
        return "";
    }

    public void add(String a,FunctionEx functionEx){
        System.out.println("add---" + a);
        functionEx.apply(a);  // 其实这里执行的就是doAdd
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.adddd("1");
    }
}
