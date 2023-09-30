package com.xyt.rabbitmq.three;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import com.xyt.rabbitmq.utils.RabbitMQUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Worker02 {

    public static final String TASK_QUEUE_NAME = "ack_queue";
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            // Channel channel = null;
            try {
                Channel channel = RabbitMQUtils.getChannel();
                //设置不公平分发
                //int prefetchCount = 1;
                //预取值
                int prefetchCount = 2;
                channel.basicQos(prefetchCount);
                System.out.println("C1等待消息处理时间短");
                DeliverCallback deliverCallback = (consumerTag, message) -> {
                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("C1接收到的消息：" + new String(message.getBody()));
                    /**
                     * 1 消息的标记 tag
                     * 2 是否批量应答  false：不批量应答
                     * */
                    channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
                };
                //采用手动应答
                channel.basicConsume(TASK_QUEUE_NAME, false, deliverCallback, (consumerTag) -> {
                    System.out.println(consumerTag + "消费者取消消费接口回调");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t1");
        t1.start();


        Thread t2 = new Thread(()->{
            // Channel channel = null;
            try {
                Channel  channel  =  RabbitMQUtils.getChannel();
                //预取值
                int prefetchCount = 5;
                channel.basicQos(prefetchCount);
                System.out.println("C1等待消息处理时间短");
                DeliverCallback deliverCallback = (consumerTag,message)->{
                    try { TimeUnit.SECONDS.sleep(30); } catch (InterruptedException e) { e.printStackTrace(); }
                    System.out.println("C2接收到的消息："+new String(message.getBody()));
                    /**
                     * 1 消息的标记 tag
                     * 2 是否批量应答  false：不批量应答
                     * */
                    channel.basicAck(message.getEnvelope().getDeliveryTag(),false);
                };
                //采用手动应答
                channel.basicConsume(TASK_QUEUE_NAME,false,deliverCallback,(consumerTag)->{
                    System.out.println(consumerTag+"消费者取消消费接口回调");
                });
            } catch (Exception e) { e.printStackTrace(); }
        },"t2");
        t2.start();
      /*  try { TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e) { e.printStackTrace(); }
        t2.stop();*/
    }
}
