package com.yu.zk.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: apache Zookeeper 原生操作
 * @Author Yan XinYu
 **/
@Slf4j
public class ZookeeperTest {

    private ZooKeeper zooKeeper;

    /**
     * 创建连接;
     *  connect 连接集群采用“，”隔开 如：" 127.0.0.1:2181, 192.168.1.45:2181 ";
     *  timeout 有范围限制,不可超过限制{min 2s max 60s}
     */
    @Before
    public void init() throws IOException {
        String connect = "127.0.0.1:2181";
        int timeout = 4*1000;//4s
        zooKeeper = new ZooKeeper(connect,timeout,event ->
                System.out.println("监听路径 main:"+event.getPath())
        );
    }

    /**
     * 创建持久节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void create_node() throws KeeperException, InterruptedException {
        List<ACL> aclList = new ArrayList<>();
        //权限计算
        int perm = ZooDefs.Perms.ALL;
        //添加所有节点
        ACL acl = new ACL(perm,ZooDefs.Ids.ANYONE_ID_UNSAFE);
        aclList.add(acl);
        zooKeeper.create("/yanxinyu/t2","nihao,t2".getBytes(),aclList, CreateMode.PERSISTENT);
    }

    @Test
    public void getData() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/yanxinyu", false, null);
        System.out.println(new String (data));
        Thread.sleep(Integer.MAX_VALUE);
    }

    @Test
    public void getData_watch_true() throws KeeperException, InterruptedException {
        byte[] data = zooKeeper.getData("/yanxinyu", true, null);
        System.out.println(new String (data));
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 持续监听机制
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getData_watch_listens() throws KeeperException, InterruptedException {
        Stat stat = new Stat();
        zooKeeper.getData("/yanxinyu", new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                //重新发起监听
                String value = null;
                try {
                    byte[] data = zooKeeper.getData(event.getPath(), this, null);
                    value = new String(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(event.getPath()+":"+value);
            }
        },stat);
        Thread.sleep(Integer.MAX_VALUE);
    }

    /**
     * 只包含该路径下的子节点
     * @throws KeeperException
     * @throws InterruptedException
     */
    @Test
    public void getChild() throws KeeperException, InterruptedException {
        //监听子节点
        List<String> children = zooKeeper.getChildren("/yanxinyu", event -> {
            System.out.println(event.getPath());
            try {
                List<String> children1 = zooKeeper.getChildren(event.getPath(), false);
                children1.forEach(System.out::println);
            } catch (KeeperException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        children.forEach(System.out::println);
        Thread.sleep(Integer.MAX_VALUE);
    }

}
