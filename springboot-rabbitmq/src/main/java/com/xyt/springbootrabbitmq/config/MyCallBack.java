package com.xyt.springbootrabbitmq.config;

import com.xyt.springbootrabbitmq.SpringbootRabbitmqApplication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
//RabbitTemplate.ConfirmCallback 交换机确认
//RabbitTemplate.ReturnCallback 回退接口
public class MyCallBack implements RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnCallback{


    @Autowired
    private RabbitTemplate rabbitTemplate;
    //注入
    @PostConstruct
    public void  init(){
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnCallback(this);
    }

    /**
     * Confirmation callback.
     * @param correlationData correlation data for the callback. 保存回调消息的ID以及相关信息
     * @param ack true for ack, false for nack
     * @param cause An optional cause, for nack, when available, otherwise null.
     */
    // 1 发消息  交换机接收到了  回调
    // 2 发消息  交换机接收失败了  回调
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

        String id =correlationData !=null ? correlationData.getId() : "";
        if (ack){
            log.info("交换机已经收到了Id为：{}的消息",id);
        }else {
            log.info("交换机还未收到id为：{}的消息，由于原因：{}",id,cause);
        }
    }

    //可以在当消息传递过程中不可达目的地时候将消息返回生产者
    //只有不可达目的地时候 才进行回退
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.error("消息{}，被交换机{}退回，退回地原因：{}，路由Key：{}",new String(message.getBody()),exchange,replyText,routingKey);
    }
}
