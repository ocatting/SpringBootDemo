package com.yu.zk.use.agent;

import lombok.extern.slf4j.Slf4j;
import org.I0Itec.zkclient.ZkClient;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 客户端监控程序
 * 上传计算机信息，到zookeeper 创建序号临时节点
 * 服务端监控这些节点，若断开则watch通知服务端服务器下线。
 * 若节点数据改变，则 watch 通知服务端数据发生变化。修改服务端数据。
 * @Author Yan XinYu
 **/
@Slf4j
public class Agent {

    private static Agent agent = new Agent();

    private String server = "127.0.0.1:2181";
    private int sessionTimeout = 5 * 1000;
    private int connectionTimeout = 10 * 1000;
    private ZkClient zkClient;
    private String root_path = "/observer";
    private String service_path = root_path + "/service";
    //当前节点路径
    private String node_path;

    //当前线程
    private Thread nThread;

    /**
     * 初始化
     */
    public Agent(){
        log.info("正在创建连接...");
        zkClient = new ZkClient(server,sessionTimeout,connectionTimeout);
        validate();
        init();
        log.info("连接成功...");
    }

    /**
     * 校验基本配置
     */
    private void validate(){
        buildRoot();
    }

    /**
     * 初始化本机参数
     * 创建临时序号节点,并给上客户端信息
     */
    private void init(){
        if(zkClient!=null){
            log.info("创建临时节点");
            node_path = zkClient.createEphemeralSequential(service_path,getInfo());
        }
    }

    public void run (){
       Thread main = new Thread(() -> {
           while (true) {
               zkClient.writeData(node_path, getInfo());
               try {
                   TimeUnit.SECONDS.sleep(5);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
           }
       }, "zk_agent_thread");
       main.setDaemon(true);
       main.start();
       close();
    }

    /**
     * 提供实例化方式
     * @return
     */
    public static Agent getInstance(){
        return agent;
    }

    /**
     * 获取计算机信息
     * @return
     */
    private OsBean getInfo(){
        return CPUMonitorCalc.getInstance().getOsBean();
    }

    private void close(){
        if(zkClient!=null)
            zkClient.close();
    }

    /**
     * 新建根持久节点
     */
    private void buildRoot(){
        if(zkClient!=null && !zkClient.exists(root_path)){
            log.info("创建root节点");
            zkClient.createPersistent(root_path);
        }
    }

    /**
     * 运行 Agent
     * @param args
     * @param instrumentation
     */
    public static void premain(String args, Instrumentation instrumentation){
        Agent.getInstance().run();
    }

}
