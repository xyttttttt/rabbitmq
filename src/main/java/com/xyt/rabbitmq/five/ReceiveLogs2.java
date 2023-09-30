package com.xyt.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

public class ReceiveLogs2 {
    //交换机的名称
    public  static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明一个交换机    类型fanout
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
        //声明一个队列  临时队列
        /**
         * 队列名称随机，当消费者断开与队列的连接的时候，队列就会自动删除
         * */
        String queue = channel.queueDeclare().getQueue();
        /**
         * 绑定交换机与队列
         * */
        channel.queueBind(queue,EXCHANGE_NAME,"111");
        System.out.println("等待接收消息，把接收到的消息打印在屏幕上。。。。。。");
        //接收消息
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogs2控制台打印接收到的消息:"+new String(message.getBody()));
        };
        //消费者取消消息时回调接口
        channel.basicConsume(queue,true,deliverCallback,consumerTag->{});
    }
}
