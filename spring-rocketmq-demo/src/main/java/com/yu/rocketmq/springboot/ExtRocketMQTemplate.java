package com.yu.rocketmq.springboot;

import org.apache.rocketmq.spring.annotation.ExtRocketMQTemplateConfiguration;
import org.apache.rocketmq.spring.core.RocketMQTemplate;

/**
 * @Description:
 * @Author Yan XinYu
 **/
@ExtRocketMQTemplateConfiguration(nameServer = "${tl.rocketmq.extNameServer}")
public class ExtRocketMQTemplate extends RocketMQTemplate {
}
