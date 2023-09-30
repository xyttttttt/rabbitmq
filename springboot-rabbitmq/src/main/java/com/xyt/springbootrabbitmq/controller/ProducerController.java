package com.xyt.springbootrabbitmq.controller;

import com.xyt.springbootrabbitmq.config.ConfirmConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/confirm")
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    //发送消息  测试确认
    @GetMapping("/sendMessage/{message}")
    public void sendMessage(@PathVariable String message){
        CorrelationData correlationData1 = new CorrelationData("1");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME
                                    ,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData1);
        log.info("发消息内容为：{}",message);

        CorrelationData correlationData2 = new CorrelationData("2");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME+1
                ,ConfirmConfig.CONFIRM_ROUTING_KEY,message,correlationData2);
        log.info("修改交换机名称,发消息内容为：{}",message);

        CorrelationData correlationData3 = new CorrelationData("3");
        rabbitTemplate.convertAndSend(ConfirmConfig.CONFIRM_EXCHANGE_NAME
                ,ConfirmConfig.CONFIRM_ROUTING_KEY+23,message,correlationData3);
        log.info("修改RoutingKey，发消息内容为：{}",message);

    }
}
