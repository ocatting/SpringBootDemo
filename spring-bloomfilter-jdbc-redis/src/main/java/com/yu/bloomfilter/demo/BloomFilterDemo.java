package com.yu.bloomfilter.demo;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: guava 布隆过滤器
 * 应用场景：redis缓存穿透，例如：当查询大量redis不存在的数据直接怼到数据库层，导致数据库层直接崩溃。
 *  解决：可在之前添加布隆过滤器拦截掉不存在的数据值。
 *  查询数组里面是否存在某一个元素。
 *  布隆过滤器实现原理，数据通过put方法(hash)解析成bitset数组中的某几个值，并填上bit数组为1，
 *      当查询数据，如果命中该数组位置则代表存在此数据，
 *
 * @Author Yan XinYu
 **/
public class BloomFilterDemo {

    /**
     * 存在问题:
     * 一 例如：黑名单数据如果需要删除，删除困难。
     *      可以给bloomFilter过滤器添加上时效，或者时效时间，指定日期清除bloomFilter数据。
     * 二 例如：宕机数据恢复问题(持久化问题)。(已解决，下面有答案)
     * 三 例如：指定bitset数据太小扩容问题。(已解决，下面有答案)
     */

    /**
     * 基础查询
     */
    @Test
    public void infrastructure_demo(){
        //指定bitset数组大小
        int size = 1000;
        //预期误判率，
        double fpp = 0.03;
        //BloomFilter 会根据上面参数进行计算生成一个合理的bitset数组大小
        BloomFilter filter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),size,fpp);

        //向bloomFilter中添加此数据
        filter.put("www.baidu.com");
        filter.put("www.163.com");

        //查询数据
        System.out.println(filter.mightContain("www.126.com"));
        System.out.println(filter.mightContain("www.163.com"));
        System.out.println(filter.mightContain("www.baidu.com"));
    }


    /**
     * 数据持久化
     */
    @Test
    public void data_persistence(){
        //预期bitset数组大小
        int size = 1000;
        //预期误判率，
        double fpp = 0.03;
        //BloomFilter 会根据上面参数进行计算生成一个合理的bitset数组大小
        BloomFilter filter = BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),size,fpp);

        //向bloomFilter中添加此数据
        filter.put("www.baidu.com");
        filter.put("www.163.com");

        //查询数据
        System.out.println(filter.mightContain("www.126.com"));
        System.out.println(filter.mightContain("www.163.com"));
        System.out.println(filter.mightContain("www.baidu.com"));

        /**
         * 数据写入
         */
        try {
            filter.writeTo(get_file_persistence_out());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 数据恢复
     */
    @Test
    public void data_recovery(){
        //预期bitset数组大小
        int size = 1000;
        //预期误判率，
        double fpp = 0.03;
        //BloomFilter 会根据上面参数进行计算生成一个合理的bitset数组大小
        try {
            BloomFilter filter = BloomFilter.readFrom(get_file_persistence_in(),Funnels.stringFunnel(StandardCharsets.UTF_8));
            //查询数据
            System.out.println(filter.mightContain("www.126.com"));
            System.out.println(filter.mightContain("www.163.com"));
            System.out.println(filter.mightContain("www.baidu.com"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //对数据持久化的文件存放位置输出
    public FileInputStream get_file_persistence_in() throws IOException {
        //操作系统名称,小写
        String os = System.getProperty("os.name").toLowerCase();
        String path="D:/bloomFilter_test";
        if(os.startsWith("linux")){
            path="/root/data";
        }
        File file = new File(path);
        if(!file.exists()){
            file.createNewFile();
        }
        System.out.println("path:"+path);
        return new FileInputStream(file);
    }

    //对数据持久化的文件存放位置输入
    public FileOutputStream get_file_persistence_out() throws FileNotFoundException {
        //操作系统名称,小写
        String os = System.getProperty("os.name").toLowerCase();
        String path="D:/bloomFilter_test";
        if(os.startsWith("linux")){
            path="/root/data";
        }
        System.out.println("path:"+path);
        return new FileOutputStream(new File(path));
    }


    /**
     * 布隆过滤器扩容,仅仅提供demo方法未实现与运行
     */
//    @Test
    public void add_capacity(){
        /**
         * 过滤器循环，多个布隆过滤器循环执行
         */
        BloomFilterDemoAddCapacity bloomFilterDemoAddCapacity = new BloomFilterDemoAddCapacity();
        for (int i = 0; i < 1200; i++) {
            bloomFilterDemoAddCapacity.add("你好"+i);
        }

        for (int i = 347; i < 500; i++) {
            System.out.println(bloomFilterDemoAddCapacity.mightContain("你好"+i));
        }

    }

    /**
     * 扩容 BloomFilter demo
     */
    public class BloomFilterDemoAddCapacity {
        //初始长度等于1000
        private volatile int init_size = 1000;

        private AtomicInteger num = new AtomicInteger(0);

        private List<BloomFilter> filters = new ArrayList<>();

        /**
         * 初始化容量
         */
        public BloomFilterDemoAddCapacity(){
            filters.add(BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),init_size));
        }

        /**
         * 扩容
         */
        public void addCapacity(){
            filters.add(BloomFilter.create(Funnels.stringFunnel(StandardCharsets.UTF_8),init_size));
            init_size = init_size << 2;//扩容两倍，责令只是为了简单，实际情况请自定义完善。
        }

        /**
         * 新增
         * @param object
         */
        public void add(Object object){

            if( num.get() >= init_size ){
                addCapacity();
            }
            //获取队尾的BloomFilter
            filters.get(filters.size() -1 ).put(object);
            //自增
            num.incrementAndGet();
        }

        /**
         * 循环查找
         * @param object
         * @return
         */
        public boolean mightContain(Object object){
            for (BloomFilter b: filters) {
                if(b.mightContain(object))
                    return true;
            }
            return false;
        }

    }






}
