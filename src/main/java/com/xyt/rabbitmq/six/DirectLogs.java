package com.xyt.rabbitmq.six;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;

public class DirectLogs {
    //交换机的名称
    public  static final String EXCHANGE_NAME = "direct_logs";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish(EXCHANGE_NAME,"info",null,message.getBytes());
            System.out.println("生产者发出消息："+message);
        }
    }
}
