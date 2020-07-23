package com.yu.zk.use;

import org.apache.zookeeper.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Description: mysql 一主多从模式下
 * 当主节点挂掉后发生选举
 *
 * 本文描述了 两种选举模式:
 * 公平模式
 * 1）首先通过zk创建一个 /server/mysql/ 的PERSISTENT节点
 * 2）多台机器同时创建 /server/mysql/leader EPHEMERAL_SEQUENTIAL子节点
 * 3）/server/leader000000xxx 后面数字最小的那个节点被选为leader节点
 * 4）所有机器监听 前一个 /server/mysql/leader 的变化，比如 (leader00001监听 leader00002) 一旦节点被删除，就获取/server下所有leader，如果自己的数字最小那么自己就被选为leader
 *
 * 非公平模式
 * 1）首先通过zk创建一个 /server/mysql 的PERSISTENT节点
 * 2）多台机器同时创建 /server/mysql/leader EPHEMERAL子节点
 * 3）子节点只能创建一个，后创建的会失败。创建成功的节点被选为leader节点
 * 4）所有机器监听 /server/mysql/leader 的变化，一旦节点被删除，就重新进行选举，抢占式地创建 /server/mysql/leader节点，谁创建成功谁就是leader。
 *
 * @Author Yan XinYu
 *
 **/
public class MysqlVoteDemo {

    private static ZooKeeper zooKeeper;
    private static String ZK_PATH = "/server/mysql/leader";

    @Before
    public void init() throws IOException {
        String connect = "127.0.0.1:2181";
        int sessionTimeout = 10 * 1000;
        System.out.println("zookeeper is starting!");
        this.zooKeeper = new ZooKeeper(connect,sessionTimeout,event -> {
            System.out.println(event.getType() + "---" + event.getPath() + "---" + event.getState());
        });
        System.out.println("zookeeper has started!");
    }

    @After
    public void close() throws InterruptedException {
        System.out.println("zookeeper is stopping!");
        TimeUnit.SECONDS.sleep(10);
        zooKeeper.close();
        System.out.println("zookeeper has stopped");
    }

    /**
     * 公平选举
     */
    @Test
    public void fairVote() throws Exception {
        //先创建一个临时有序节点
        String nodeVal = zooKeeper.create(ZK_PATH, "node1".getBytes(),
                            ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        //遍历子下节点
        List<String> children = zooKeeper.getChildren("/server", null);
        Collections.sort(children);

        String formerNode = "";  //前一个节点，用于监听
        for (int i = 0; i < children.size(); i++) {
            String node = children.get(i);
            if (nodeVal.equals("/server/" + node)) {
                if (i == 0) {
                    //第一个
                    System.out.println("我被选为leader节点了");
                } else {
                    formerNode = children.get(i - 1);
                }
            }
        }
        if (!"".equals(formerNode)) {
            //自己不是第一个，如果是第一个formerNode应该没有值
            System.out.println("我竞选失败了");
            //3、监听前一个节点的删除事件，如果删除了，重新进行选举
            zooKeeper.getData("/server/" + formerNode, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    System.out.println(event.getType() + "---" + event.getPath() + "---" + event.getState());
                    try {
                        if (Objects.equals(event.getType(), Event.EventType.NodeDeleted)) {
                            fairVote();
                        }
                    } catch (Exception e) {
                    }
                }
            }, null);
        }
        System.out.println("children:" + children);
    }

    /**
     * 非公平选举
     */
    @Test
    public void nonFairVote() throws Exception {
        try {
            zooKeeper.create(ZK_PATH,"mysql_master_node".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException.NodeExistsException e) {
            System.out.println("选举失败");
            e.printStackTrace();
        } finally {
            //重新监听节点，看是否会断开,这里有个问题(递归调用nonFairVote()方法)
            zooKeeper.getData(ZK_PATH,event -> {
                System.out.println(event.getType() + "---" + event.getPath() + "---" + event.getState());
                try{
                    if (Objects.equals(event.getType(), Watcher.Event.EventType.NodeDeleted)) {
                        nonFairVote();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            },null);
        }
    }
}
