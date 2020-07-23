package com.yu.juc.queue;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * @Description: 阻塞(队列 基于 线性表，链表的数据结构)
 *
 * 队列操作
 *
 *  -添加操作:
 *  add(E) 有空间就添加返回true，没有就抛出 IllegalStateException
 *  offer(E)/(E,timeout,unit) 有空间返回true,没有空间就返回false,{可以设定等待一定时间}
 *  put(E) 等待空位并放入元素。无返回
 *
 *  -取出
 *  poll(timeout,unit) 取出并删除头 可以设定等待一定时间。
 *  take() 取出并删除头 不同实现可能会等待。
 *  peek() 取出但不删除头元素。
 *
 *  -删除
 *  drainTo(Collection<? super E> c) 删除所有元素并给到指定集合中。
 *  drainTo(Collection<? super E> c，int )删除指定数量的元素，并给到集合中。
 *  remove(Object o) 删除指定元素。返回boolean
 *
 * @Author Yan XinYu
 **/
public class BlockingQueueTestDemo {

    /**
     * 主要基于不同策略与不同的数据结构区分
     * 同样 队列分为 单向(queue){先进先出}与双向队列(deque){一边进一边出，类似优先级队列}
     */

    //基于数组的阻塞队列
    private ArrayBlockingQueue<Student> arrayBlockingQueue = new ArrayBlockingQueue(1);

    //基于 链表结构的阻塞队列
    private LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(1);

    //优先级队列 默认长度是11 会自动扩容
    private PriorityBlockingQueue<SortObject> priorityBlockingQueue = new PriorityBlockingQueue(1);

    //延时队列
    private DelayQueue<CinemaTicketDelay> delayQueue = new DelayQueue();

    /**
     * 学生检测体重
     */
    class Student{

        private String name;
        private boolean isCheck;

        public Student(String name,boolean isCheck){
            this.name = name;
            this.isCheck = isCheck;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        @Override
        public String toString() {
            return "Student :"+name+" isCheck:"+isCheck;
        }
    }

    /*
     * 测试数组类型的对列
     * 该队列中存放的元素满了之后 其他的数据进入等待当中
     * 该队列中消费了一个数据后开始head队列节点唤醒
     */
    public void arrayBlockingQueueTest(){
        ExecutorService executorService = Executors.newCachedThreadPool();

        //去排队检查身体
        executorService.submit(()->{
            int i = 0;
            while (true){
                try {
                    Student student = new Student("学生"+i,false);
                    System.out.println("准备放入:"+student);
                    arrayBlockingQueue.put(student);
                    System.out.println(student+" 队列中的有:"+arrayBlockingQueue.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i++;
            }
        });

        //检查身体
        executorService.submit(()->{
            while (true){
                System.out.println("准备开始拿学生开始检查");
                Student student = arrayBlockingQueue.take();
                student.setCheck(true);
                System.out.println(student+" 其中还有待检测:"+arrayBlockingQueue.size());
            }
        });
    }

    /**
     * 延迟队列测试
     */
    public void delayQueueTest(){

        CinemaTicketDelay t1 = new CinemaTicketDelay("音乐", 1000);
        delayQueue.put(t1);
        CinemaTicketDelay t2 = new CinemaTicketDelay("电影", 5000);
        delayQueue.put(t2);
        CinemaTicketDelay t3 = new CinemaTicketDelay("歌剧", 7000);
        delayQueue.put(t3);
        CinemaTicketDelay t4 = new CinemaTicketDelay("相声", 2000);
        delayQueue.put(t4);

        while (delayQueue.size() > 0){
            CinemaTicketDelay ticket = null;
            try {
                ticket = delayQueue.take();
                System.out.println("电影票出队:"+ticket.toString()+": "+ LocalDateTime.now());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 优先级 队列
     * 队列元素必须实现 Comparable<?> 方法
     */
    public void priorityBlockingQueueTest(){

        SortObject t1 = new SortObject("王",0);
        priorityBlockingQueue.put(t1);

        SortObject t2 = new SortObject("李",-2);
        priorityBlockingQueue.put(t2);

        SortObject t3 = new SortObject("熊",9);
        priorityBlockingQueue.put(t3);

        SortObject t4 = new SortObject("杨",1);
        priorityBlockingQueue.put(t4);

        while (priorityBlockingQueue.size()>0){
            SortObject take = null;
            try {
                take = priorityBlockingQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(take);
        }
    }

    /**
     * poll 填写指定时间可以延迟获取队列中的值，
     * 但若立刻put/offer插入元素则立刻执行poll方法获取到该元素
     */
    public void testBlockingQueue(){
        //阻塞队列，当元素满后阻塞等待。
        ArrayBlockingQueue<String> arrayBlockingQueue = new ArrayBlockingQueue(5);
        new Thread(()->{

            System.out.println("开始等待:" + System.currentTimeMillis());
            try {
                String poll = arrayBlockingQueue.poll(30, TimeUnit.SECONDS);
                System.out.println("取消等待:"+poll+":"+ System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }).start();

        //2s 后开始插入元素
        System.out.println("2s后开始插入元素:"+System.currentTimeMillis());
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            arrayBlockingQueue.put("nihao");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void main(String[] args) {
        final BlockingQueueTestDemo blockingQueueTestDemo = new BlockingQueueTestDemo();
        //线性表 或者阻塞队列 测试
//        blockingQueueTestDemo.arrayBlockingQueueTest();
        //优先级 测试
//        blockingQueueTestDemo.priorityBlockingQueueTest();
        //延迟队列 测试
//        blockingQueueTestDemo.delayQueueTest();

        //队列等待指定时间测试
        blockingQueueTestDemo.testBlockingQueue();
    }
}
