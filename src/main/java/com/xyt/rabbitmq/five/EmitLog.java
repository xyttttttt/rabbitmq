package com.xyt.rabbitmq.five;

import com.rabbitmq.client.Channel;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;

/**
 * 发消息
 * */
public class EmitLog {
    //交换机的名称
    public  static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"",null,message.getBytes());
            System.out.println("生产者发出消息："+message);
        }
    }
}
