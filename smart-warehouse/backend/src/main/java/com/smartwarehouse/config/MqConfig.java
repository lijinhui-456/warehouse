package com.smartwarehouse.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置 - 交换机 + 队列 + 绑定
 * 所有通知消息统一走 warehouse.notice.exchange
 */
@Configuration
public class MqConfig {

    public static final String EXCHANGE = "warehouse.notice.exchange";
    public static final String QUEUE = "warehouse.notice.queue";
    public static final String ROUTING_KEY = "notice.publish";

    @Bean
    public TopicExchange noticeExchange() {
        return ExchangeBuilder.topicExchange(EXCHANGE).durable(true).build();
    }

    @Bean
    public Queue noticeQueue() {
        return QueueBuilder.durable(QUEUE).build();
    }


    @Bean
    public Binding noticeBinding(Queue noticeQueue, TopicExchange noticeExchange) {
        return BindingBuilder.bind(noticeQueue).to(noticeExchange).with(ROUTING_KEY);
    }


    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rt = new RabbitTemplate(connectionFactory);
        rt.setMessageConverter(new Jackson2JsonMessageConverter());
        return rt;
    }
}
