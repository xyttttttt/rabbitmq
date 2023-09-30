package com.xyt.springbootrabbitmq.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * 配置类   发布确认 （高级）
 * */
@Configuration
public class ConfirmConfig {

    public static final String CONFIRM_EXCHANGE_NAME = "confirm.exchange";
    public static final String CONFIRM_QUEUE_NAME = "confirm.queue";
    public static final String CONFIRM_ROUTING_KEY= "key1";
    public static final String BACKUP_EXCHANGE_NAME = "backup.exchange";
    public static final String BACKUP_QUEUE_NAME = "backup.queue";
    public static final String WARNING_QUEUE_NAME = "warning.queue";

    //交换机声明
    @Bean
    public DirectExchange confirmExchange(){

        return ExchangeBuilder.directExchange(CONFIRM_EXCHANGE_NAME).durable(true)
                            .withArgument("alternate-exchange",BACKUP_EXCHANGE_NAME).build();
    }
    //声明队列
    @Bean
    public Queue confirmQueue(){
        return QueueBuilder.durable(CONFIRM_QUEUE_NAME).build();
    }
    //绑定
    @Bean
    public Binding queueBindingExchange(@Qualifier("confirmExchange") DirectExchange confirmExchange
                                        ,@Qualifier("confirmQueue") Queue confirmQueue){
        return BindingBuilder.bind(confirmQueue).to(confirmExchange).with(CONFIRM_ROUTING_KEY);
    }

    //备份交换机
    @Bean
    public FanoutExchange backupExchange(){
        return new FanoutExchange(BACKUP_EXCHANGE_NAME);
    }
    //备份队列
    @Bean
    public Queue backupQueue(){
        return QueueBuilder.durable(BACKUP_QUEUE_NAME).build();
    }
    //报警队列
    @Bean
    public Queue warningQueue(){
        return QueueBuilder.durable(WARNING_QUEUE_NAME).build();
    }
    //绑定
    @Bean
    public Binding backQueueBindingExchange(@Qualifier("backupExchange") FanoutExchange backupExchange
            ,@Qualifier("backupQueue") Queue backupQueue){
        return BindingBuilder.bind(backupQueue).to(backupExchange);
    }
    //绑定
    @Bean
    public Binding warningQueueBindingExchange(@Qualifier("backupExchange") FanoutExchange backupExchange
            ,@Qualifier("warningQueue") Queue warningQueue){
        return BindingBuilder.bind(warningQueue).to(backupExchange);
    }
}
