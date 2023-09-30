package com.xyt.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtils {
    public static Channel getChannel() throws Exception{
        //创建工厂
        ConnectionFactory factory = new ConnectionFactory();
        //工厂IP 连接rabbitmq队列
        factory.setHost("192.160.1.180");
        //用户名
        factory.setUsername("admin");
        //密码
        factory.setPassword("123");
        //创建连接
        Connection connection = factory.newConnection();
        //获取信道
        Channel channel = connection.createChannel();
        return channel;
    }
}
