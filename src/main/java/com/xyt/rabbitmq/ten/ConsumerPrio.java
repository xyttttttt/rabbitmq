package com.xyt.rabbitmq.ten;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

public class ConsumerPrio {
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        DeliverCallback deliverCallback = (consumerTag, message) ->{
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
        channel.basicConsume("q1",true,deliverCallback,cancelCallback);
    }
}
