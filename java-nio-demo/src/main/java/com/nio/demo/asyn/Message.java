package com.nio.demo.asyn;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@Data
public class Message {
    // 事务Id
    private String id;
    // 处理类型
    private String type;
    // 发送到指定队列
    private String queue;

    // 执行状态
    private int state;
    private LocalDateTime finishTime;
    // 寻卡
    private String searchCard;
    private String searchCardResult;

    // 订单
    private String order;
    private String orderResult;
}
