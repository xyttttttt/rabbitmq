package com.xyt.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.UUID;

/**
 *  1单个确认模式
 * */
public class ConfirmMessageIndividually {

    //批量发消息的个数
    public static final int MESSAGE_COUNT = 1000;
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName,false,false,false,null);
        //开启确认模式
        channel.confirmSelect();
        //开始时间
        long begin = System.currentTimeMillis();

        //批量发消息
        for (int i = 0; i < 1000; i++) {
            String message = i +"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //单个消息马上进行发布确认
            boolean flag = channel.waitForConfirms();
            if (flag){
                System.out.println("消息发送成功");
            }
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT +"个单个确认消息，总用时："+(end -begin)+"毫秒");//总用时：641毫秒
    }
}
