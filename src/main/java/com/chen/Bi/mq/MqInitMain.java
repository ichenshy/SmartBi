package com.chen.Bi.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * mq 初始化
 *
 * @author CSY
 * @date 2023/06/26
 */
public class MqInitMain {
    public static void main(String[] args) {
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("localhost");
            Connection connection = factory.newConnection();
            //创建一个管道
            Channel channel = connection.createChannel();
            String EXCHANGE_NAME = "code_exchange";
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            //创建队列
            String queueName = "code_queue";
            String MY_ROUTINGKEY = "my_routingKey";
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, MY_ROUTINGKEY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
