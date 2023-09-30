package com.xyt.rabbitmq.seven;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

/**
 * 声明主题交换机  及相关队列
 * */
public class ReceiveLogsTopic01 {
    public static final String EXCHANGE_NAME="topic_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        //声明队列
        channel.queueDeclare("Q1",false,false,false,null);
        channel.queueBind("Q1",EXCHANGE_NAME,"*.orange.*");
        System.out.println("等待接收消息");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsTopic01控制台打印接收到的消息:"+new String(message.getBody()));
            System.out.println("接收消息队列Q1"+ "\t绑定键："+message.getEnvelope().getRoutingKey());
        };
        //接收消息
        channel.basicConsume("Q1",true,deliverCallback,consumerTag->{});

    }
}
