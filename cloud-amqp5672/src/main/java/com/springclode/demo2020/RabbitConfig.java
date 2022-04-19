package com.springclode.demo2020;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.plan.exchangeName}")
    private String exchangeName;

    @Value("${rabbitmq.plan.queueName}")
    private String queueName;

    @Bean
    public Queue planQueue() {
        return new Queue(queueName);
    }

    @Bean
    public TopicExchange planExchange() {
        return new TopicExchange(exchangeName);
    }
}
