package com.chen.Bi.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * mq 初始化
 *
 * @author CSY
 * @date 2023/06/26
 */
@Component
@Slf4j
public class BiInitMain {
    @PostConstruct
    void biMqInit() {
        log.info("Mq管道初始化开始...");
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            //创建一个管道
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = BiMqConstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");
            //创建队列
            String queueName = BiMqConstant.BI_QUEUE_NAME;
            String MY_ROUTINGKEY = BiMqConstant.BI_ROUTING_KEY;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, MY_ROUTINGKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            log.info("Mq管道初始化结束...");
        }
    }
}
