package com.xyt.rabbitmq.two;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

/**
 * 这是一个工作线程 （相当于消费者）
 * */
public class Worker01 {
    //队列名称
    public static final String  QUEUE_NAME = "hello";

    //接收消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明接收消息
        DeliverCallback deliverCallback = (consumerTag, message) ->{
            System.out.println("接收到的消息："+new String(message.getBody()));
            channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
        };
        //取消消息时的回调
        CancelCallback cancelCallback = consumerTag->{
            System.out.println(consumerTag+"消费者取消消费接口回调逻辑");
        };
        //消息的接收
        System.out.println("C2等待接收消息。。。。。。");
        //channel.basicConsume(QUEUE_NAME,true,deliverCallback,cancelCallback);
        //手动应答
        channel.basicConsume(QUEUE_NAME,false,deliverCallback,cancelCallback);
    }

}
