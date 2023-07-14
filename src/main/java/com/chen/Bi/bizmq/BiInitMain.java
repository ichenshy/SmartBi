package com.chen.Bi.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * mq 初始化
 *
 * @author CSY
 * @date 2023/06/26
 */
public class BiInitMain {
    public static void main(String[] args) {
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
        }
    }
}
