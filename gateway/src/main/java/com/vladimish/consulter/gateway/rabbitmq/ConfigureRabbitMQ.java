package com.vladimish.consulter.gateway.rabbitmq;

import com.vladimish.consulter.gateway.rabbitmq.consumer.ReceiveRegisterReplyHandler;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMQ {
    public static final String EXCHANGE_NAME = "consulter.exchange";
    public static final String PRODUCER_AUTH_QUEUE_NAME = "consulter.auth.client";
    public static final String CONSUMER_AUTH_QUEUE_NAME = "consulter.auth.client.reply";

    @Bean
    Queue createProducerQueue() {
        return new Queue(PRODUCER_AUTH_QUEUE_NAME, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(@Qualifier("createProducerQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(PRODUCER_AUTH_QUEUE_NAME);
    }

    @Bean
    Queue createConsumerQueue() {
        return new Queue(CONSUMER_AUTH_QUEUE_NAME, false);
    }

}