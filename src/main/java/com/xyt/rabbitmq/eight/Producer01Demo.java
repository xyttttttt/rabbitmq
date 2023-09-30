package com.xyt.rabbitmq.eight;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

/**
 * 死信队列之生产者代码
 * */
public class Producer01Demo {
    //    交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        //死信消息  设置TTL时间  单位：ms
        //AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().expiration("10000").build();
        for (int i = 0; i < 10; i++) {
            String message = "info"+i;
         //   channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",properties,message.getBytes());
            channel.basicPublish(NORMAL_EXCHANGE,"zhangsan",null,message.getBytes());
        }
    }
}
