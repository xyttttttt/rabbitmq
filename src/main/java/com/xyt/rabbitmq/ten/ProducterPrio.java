package com.xyt.rabbitmq.ten;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.HashMap;
import java.util.Map;

public class ProducterPrio {

    public static void main(String[] args) throws Exception {

        Channel channel = RabbitMQUtils.getChannel();
        Map<String,Object> arguments = new HashMap<>();
        arguments.put("x-max-priority",10); //官方允许最大优先级范围为 0~255
        channel.queueDeclare("q1",true,false,false,arguments);



        for (int i = 0; i < 10; i++) {
            String message = "hello "+i;
            if (i ==5){
                AMQP.BasicProperties properties = new AMQP.BasicProperties().builder().priority(5).build();
                channel.basicPublish("","q1",properties,message.getBytes());
            }
            else {
                channel.basicPublish("","q1",null,message.getBytes());
            }
        }

        System.out.println("消息发送完毕");
    }
}
