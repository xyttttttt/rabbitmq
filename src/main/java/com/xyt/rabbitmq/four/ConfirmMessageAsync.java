package com.xyt.rabbitmq.four;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.util.UUID;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 * 异步发送确认
 * */
public class ConfirmMessageAsync {
    public static final int MESSAGE_COUNT = 1000;
    public static void main(String[] args) throws Exception {
        Channel channel = RabbitMQUtils.getChannel();
        String queueName = UUID.randomUUID().toString();
        channel.queueDeclare(queueName, true, false, false, null);
        //开启确认模式
        channel.confirmSelect();
        /**
         * 线程安全有序的一个哈希表    适用于高并发的情况下
         * 1 轻松的将序号与消息进行关联
         * 2 轻松的批量删除条目 只要给到序号
         * 3 支持高并发 （多线程）
        * */
        ConcurrentSkipListMap<Long,String> outstandingConfirms = new ConcurrentSkipListMap<>();

        //准备消息监听器，  监听哪些消息成功了，哪些消息失败了
        //消息确认成功回调函数
        ConfirmCallback ackCallback = (deliveryTag,multiple)->{
            if (multiple){
                //删除确认的消息   剩下的就是未确认的消息
                ConcurrentNavigableMap<Long, String> map = outstandingConfirms.headMap(deliveryTag);
                map.clear();
            }else {
                outstandingConfirms.remove(deliveryTag);
            }
            System.out.println("确认的消息："+deliveryTag);
        };
        ConfirmCallback nackCallback = (deliveryTag,multiple)->{
            String s = outstandingConfirms.get(deliveryTag);
            System.out.println("未确认的消息是："+s+"\t"+"未确认的消息："+deliveryTag);
        };
        channel.addConfirmListener(ackCallback,nackCallback);   //异步通知
        //开始时间
        long begin = System.currentTimeMillis();
        for (int i = 0; i < MESSAGE_COUNT; i++) {
            String message = i+"";
            channel.basicPublish("",queueName,null,message.getBytes());
            //此处记录下所有要发送的消息，消息的总和
            outstandingConfirms.put(channel.getNextPublishSeqNo(),message);
            System.out.println(channel.getNextPublishSeqNo());
        }
        long end = System.currentTimeMillis();
        System.out.println("发布"+MESSAGE_COUNT +"个异步发送确认消息，总用时："+(end -begin)+"毫秒");//总用时：66毫秒
    }
}
