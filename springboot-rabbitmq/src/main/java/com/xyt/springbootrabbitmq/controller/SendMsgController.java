package com.xyt.springbootrabbitmq.controller;

import com.xyt.springbootrabbitmq.config.DelayedQueueConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * 发送延迟消息
 * */
@Slf4j
@Controller
@RequestMapping("/ttl")
public class SendMsgController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //开始发消息
    @GetMapping("/sendMsg/{message}")
    public void sendMsg(@PathVariable String message){
      log.info("当前时间:{},发送一条信息给两个ttl队列：{}",new Date().toString(),message);
      rabbitTemplate.convertAndSend("X","XA","消息来自ttl为10s的队列："+message);
      rabbitTemplate.convertAndSend("X","XB","消息来自ttl为40s的队列："+message);
    }

    //发送消息  消息 ttl
    @GetMapping("/sendExpirationMsg/{message}/{ttlTime}")
    public void sendExpirationMsg(@PathVariable String message,@PathVariable String ttlTime){
        log.info("当前时间:{},发送一条时长{}毫秒的信息给队列：{}",new Date().toString(),ttlTime,message);
        rabbitTemplate.convertAndSend("X","XC",message,msg -> {
            //发送消息时候的延迟时长
            msg.getMessageProperties().setExpiration(ttlTime);
            return msg;
        });
    }

    //开始发消息  基于插件的消息 以及 延迟的消息
    @GetMapping("/sendDelayMsg/{message}/{delayTime}")
    public void sendDelayedMsg(@PathVariable String message,@PathVariable Integer delayTime){
        log.info("当前时间:{},发送一条时长{}毫秒的信息给延迟队列队列delayed.queue：{}",new Date().toString(),delayTime,message);
        rabbitTemplate.convertAndSend(DelayedQueueConfig.DELAYED_EXCHANGE_NAME
                                    ,DelayedQueueConfig.DELAYED_ROUTING_KEY
                                    ,message,msg->{
                                    //发送消息时候的延迟时长
                                    msg.getMessageProperties().setDelay(delayTime);
                                    return msg;
        });
    }


}
