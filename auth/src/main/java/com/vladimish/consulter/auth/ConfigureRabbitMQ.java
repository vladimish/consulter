package com.vladimish.consulter.auth;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMQ {
    public static final String EXCHANGE_NAME = "consulter.exchange";
    public static final String PRODUCER_AUTH_QUEUE_NAME = "consulter.auth.client";
    public static final String CONSUMER_AUTH_QUEUE_NAME = "consulter.auth.client.reply";


    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(CONSUMER_AUTH_QUEUE_NAME);
    }

    @Bean
    Queue createConsumerQueue() {
        return new Queue(CONSUMER_AUTH_QUEUE_NAME, false);
    }

}