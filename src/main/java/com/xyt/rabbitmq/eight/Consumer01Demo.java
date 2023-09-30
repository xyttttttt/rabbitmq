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
public class Consumer01Demo {
//    交换机
    public static final String NORMAL_EXCHANGE = "normal_exchange";
    public static final String DEAD_EXCHANGE = "dead_exchange";
//    队列
    public static final String NORMAL_QUEUE = "normal_queue";
    public static final String DEAD_QUEUE = "dead_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //声明死信交换机和普通交换机
        channel.exchangeDeclare(NORMAL_EXCHANGE, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE,BuiltinExchangeType.DIRECT);
        //普通队列
        Map<String,Object> arguments = new HashMap<>();
        //过期时间
        //arguments.put("x-message-ttl",10000);
        //正常队列设置死信交换机是谁
        arguments.put("x-dead-letter-exchange",DEAD_EXCHANGE);
        //设置死信routingKey
        arguments.put("x-dead-letter-routing-key","lisi");
        //设置队列达到最大长度      正常队列的长度的限制
//        arguments.put("x-max-length",6);
        channel.queueDeclare(NORMAL_QUEUE,false,false,false,arguments);
        channel.queueBind(NORMAL_QUEUE,NORMAL_EXCHANGE,"zhangsan");

        //死信队列
        channel.queueDeclare(DEAD_QUEUE,false,false,false,null);
        channel.queueBind(DEAD_QUEUE,DEAD_EXCHANGE,"lisi");
        DeliverCallback deliverCallback = (consumerTag,message) ->{
            String msg =  new String(message.getBody());
            if (msg.equals("info6")){
                System.out.println("Consumer1接收的消息："+msg +"，此消息被拒绝");
                channel.basicReject(message.getEnvelope().getDeliveryTag(),false);   //取消消息重新入队
            }else {
                System.out.println("Consumer1接收的消息："+new String(message.getBody()));
                channel.basicAck(message.getEnvelope().getDeliveryTag(),false);      //取消批量应答
            }
        };
       // channel.basicConsume(NORMAL_QUEUE,true, deliverCallback,consumerTag->{});
        channel.basicConsume(NORMAL_QUEUE,false, deliverCallback,consumerTag->{});
    }
}
