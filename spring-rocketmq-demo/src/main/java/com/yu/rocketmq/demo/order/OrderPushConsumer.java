package com.yu.rocketmq.demo.order;

/**
 * @Description: mq 顺序消费
 *  原理：由于是队列(先进先出)，所以 只需要保证其存放一个队列中按顺序即可消费。
 *  如: 订单创建，订单支付，订单库存/积分。 按照先后顺序放入到一个队列中即可消费成功。
 *
 * @Author Yan XinYu
 **/
public class OrderPushConsumer {
}
