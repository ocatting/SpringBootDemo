package com.sync.core;

public class MyEnum {

    /**
     * 字段长度编码扩容 如:gbk -> utf-8 变为原来的两倍
     */
    public enum ColSizeTimes{

        /**
         * 单倍
         */
        EQUAL(1),
        /**
         * 双倍
         */
        DOUBLE(2);

        private int times = 0;


        ColSizeTimes(int times) {
            this.times = times;
        }

        public int getTimes(){
            return times;
        }
    };

}