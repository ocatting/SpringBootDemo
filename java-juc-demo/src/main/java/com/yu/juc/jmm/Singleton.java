package com.yu.juc.jmm;

/**
 * @Description:
 * volatile 保证了并发下的 可见性与有序性
 * {并发:可见性，原子性，有序性}
 *
 * volatile 如何保证有序性
 * jmm模型
 * (lock) -> read -> load -> use  加载数据到cpu
 * (unlock) <- write <- store <- assign 写入到主内存
 * 指令重排有两种现象，一种是 CPU的指令重排 ，二种是 JIT的指令重排
 * javac 中会 禁止JIT对volatile关键字进行优化的指令重排
 * 在CPU 中java会加上lock关键字表示线程会独占，这样会出现 StoreStore(loadStore等)对元素的操作，
 *      形成一种天然的屏障叫做内存屏障。来禁止指令重排现象。
 *
 * @Author Yan XinYu
 **/
public class Singleton {

    // volatile 禁止指令重排现象
    private volatile static Singleton singleton;

    /**
     * 多线程单例Singleton, 不加 volatile 可能发生指令重排现象
     * 原理:
     *      在volatile前后添加内存屏障,保证编译器(javac)不会指令优化
     *      StoreStore屏障，StoreLoad屏障，LoadLoad屏障，LoadStore屏障
     * @return
     */
    public static Singleton getInstance(){
        if(singleton == null){
            synchronized (Singleton.class){
                if(singleton == null){
                    /**
                     * 对象实例化可分为三个步骤
                     * 1.分配空间
                     * 2.初始化对象数据
                     * 3.singleton指针分配到对象
                     */
                    singleton = new Singleton();
                    /**
                     * 在多线程环境下 1 与 2 步骤可能发生重排现象
                     * 所以3步骤存在的位置可能发生改变，
                     * 1分配空间
                     * 3.指针分配
                     * 2.初始化
                     * 由于2步骤发生了指令重排现象，
                     * 多线程环境下
                     * t1线程初始化已经分配了指针但是并未完成初始化。
                     * t2线程去判断对象指针不为空，但实际未完成初始化。
                     */
                }
            }
        }
        return singleton;
    }

    // Test
    public static void main(String[] args) {
        Singleton.getInstance();
    }
}
