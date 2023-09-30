package com.xyt.rabbitmq.one;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer {

    //队列名称
    public static final String QUEUE_NAME ="hello";

    //发消息
    public static void main(String[] args) throws IOException, TimeoutException {
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
        //生成一个队列
        /**
         * 1 队列名称
         * 2 队列里面地消息是否持久化，默认消息存储在内存中
         * 3 该队列是否只供一个消费者进行消息， 是否共享消息 ， true可以多个消费者消费  false 只能一个消费者消费
         * 4 是否自动删除。最后一个消费者端开连接后，该队列是否自动删除 true自动删除  false不自动删除
         * 5 其他参数
         * */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //发消息
        String message = "hello world";
        /**
         * 发送一个消息
         * 1 发送到哪个交换机
         * 2 路由地Key值是哪个 本次队列名称
         * 3 其他参数信息
         * 4 消息体
         * */
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("消息发送完毕");
    }
}
