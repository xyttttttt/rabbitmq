package com.xyt.rabbitmq.two;

import com.rabbitmq.client.Channel;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;

/**
 * 生产者
 * 发送大量消息
 * */
public class Task01 {
    public static final String  QUEUE_NAME = "hello";

    //发送大量消息
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        //队列声明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //从控制台输入发送消息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("发送消息完成"+message);
        }
    }
    /**
     * 小学篱笆旁的蒲公英
     * 是记忆里有味道风景
     * 午睡操场传来蝉的声音
     * 多少年后也还是很好听
     * 将愿望折纸飞机寄成信
     * 因为我们等不到那流星
     * */
}
