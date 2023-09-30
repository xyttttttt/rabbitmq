package com.xyt.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

public class ReceiveLogsDirect01 {

    public static final String EXCHANGE_NAME="direct_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明交换机
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare("console",false,false,false,null);
        channel.queueBind("console",EXCHANGE_NAME,"info");
        channel.queueBind("console",EXCHANGE_NAME,"warning");
        DeliverCallback deliverCallback = (consumerTag, message)->{
            System.out.println("ReceiveLogsDirect01控制台打印接收到的消息:"+new String(message.getBody()));
        };
        //消费者取消消息时回调接口
        channel.basicConsume("console",true,deliverCallback,consumerTag->{});
    }
}
