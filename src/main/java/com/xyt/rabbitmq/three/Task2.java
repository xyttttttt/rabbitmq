package com.xyt.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.Scanner;

/**
 * 消费消息在手动应答中不丢失、放回队列中重新消费
 * */
public class Task2 {
    //大小写切换快捷键 ctrl+shift+u
    public static final String TASK_QUEUE_NAME = "ack_queue";
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        boolean durable = true;  //需要持久化
        channel.queueDeclare(TASK_QUEUE_NAME,durable,false,false,null);
        //开启发布确认
        channel.confirmSelect();
        //从控制台中输入信息
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            String message = scanner.next();
           // channel.basicPublish("",TASK_QUEUE_NAME,null,message.getBytes());
            //持久化
            channel.basicPublish("",TASK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());
            System.out.println("生产者发出消息："+message);
        }
    }
}
