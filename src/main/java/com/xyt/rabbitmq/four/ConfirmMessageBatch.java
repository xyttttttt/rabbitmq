package com.xyt.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.UUID;

public class ConfirmMessageBatch {
    public static final int MESSAGE_COUNT = 1000;
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启确认模式
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();
        //批量确认消息大小
        int batchSize = 100;
        //未确认消息个数
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i +"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //判断达到100 条消息的时候  批量确认一次
            if (i%batchSize==0){
                //发布确认
                channel.waitForConfirms();
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT +"个批量确认消息，总用时："+(end -begin)+"毫秒");//总用时：115毫秒
    }
}
