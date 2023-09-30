package com.xyt.rabbitmq.eight;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 死信队列实战
 * */
public class Consumer02Demo {

    public static final String DEAD_QUEUE = "dead_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();

        System.out.println("等待接收消息。。。。。。");
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            System.out.println("Consumer2接收的消息："+new String(message.getBody()));
        };
        channel.basicConsume(DEAD_QUEUE,true, deliverCallback,consumerTag->{});
    }
}
