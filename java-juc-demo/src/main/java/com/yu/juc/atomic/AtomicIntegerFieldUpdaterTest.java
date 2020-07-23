package com.yu.juc.atomic;

import sun.applet.Main;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * @Description: Atomic 提供允许修改类中字段的数据，但必须要求 field public ;
 * @Author Yan XinYu
 **/
public class AtomicIntegerFieldUpdaterTest {

    //字段必须为 Public 类型必须为 int 基本类型
    public static void main(String[] args) {
        AtomicIntegerFieldUpdater atomicIntegerFieldUpdater = AtomicIntegerFieldUpdater.newUpdater(Student.class,"age");
        Student stu = new Student("nihao", 12);
        atomicIntegerFieldUpdater.getAndIncrement(stu);
        System.out.println(atomicIntegerFieldUpdater.get(stu));
    }

    static class Student{
        private String name;
        public volatile int age;

        public Student(String name ,int age){
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getOld() {
            return age;
        }
    }
}
