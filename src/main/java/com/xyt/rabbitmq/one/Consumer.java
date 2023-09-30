package com.xyt.rabbitmq.one;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    //队列名称
    public static final String QUEUE_NAME ="hello";
    //接收消息
    public static void main(String[] args) throws IOException, TimeoutException {
        //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.160.1.180");
        factory.setUsername("admin");
        factory.setPassword("123");
        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();
        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            System.out.println(new String(message.getBody()));
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println("消息消费被中断");
        };
        /**
         * 消费者消费消息
         * 1 消费哪个队列
         * 2 消费成功之后是否要自动应答 true代表自动应答  false表示手动应答
         * 3 消费者成功消费地回调
         * 4 消费者取消消费的回调
         * */
        channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
    }
}
