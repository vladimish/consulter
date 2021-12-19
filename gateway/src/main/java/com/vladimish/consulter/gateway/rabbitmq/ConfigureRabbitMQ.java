package com.vladimish.consulter.gateway.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMQ {
    public static final String EXCHANGE_NAME = "consulter.exchange";
    public static final String PRODUCER_REGISTER_QUEUE_NAME = "consulter.auth.client.register";
    public static final String CONSUMER_REGISTER_QUEUE_NAME = "consulter.auth.client.register." + System.getenv("TOKEN") + ".reply";
    public static final String PRODUCER_LOGIN_QUEUE_NAME = "consulter.auth.client.login";
    public static final String CONSUMER_LOGIN_QUEUE_NAME = "consulter.auth.client.login." + System.getenv("TOKEN") + ".reply";

    @Bean
    Queue createProducerRegisterQueue() {
        return new Queue(PRODUCER_REGISTER_QUEUE_NAME, false);
    }

    @Bean
    DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding registerProducerBinding(@Qualifier("createProducerRegisterQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(PRODUCER_REGISTER_QUEUE_NAME);
    }

    @Bean
    Queue createRegisterConsumerQueue() {
        return new Queue(CONSUMER_REGISTER_QUEUE_NAME, false);
    }

    @Bean
    Binding registerConsumerBinding(@Qualifier("createRegisterConsumerQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(CONSUMER_REGISTER_QUEUE_NAME);
    }

    @Bean
    Queue createProducerLoginQueue() {
        return new Queue(PRODUCER_LOGIN_QUEUE_NAME, false);
    }

    @Bean
    Binding loginProducerBinding(@Qualifier("createProducerLoginQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(PRODUCER_LOGIN_QUEUE_NAME);
    }

    @Bean
    Queue createConsumerLoginQueue() {
        return new Queue(CONSUMER_LOGIN_QUEUE_NAME, false);
    }

    @Bean
    Binding loginConsumerBinding(@Qualifier("createConsumerLoginQueue") Queue q, DirectExchange e) {
        return BindingBuilder.bind(q).to(e).with(CONSUMER_LOGIN_QUEUE_NAME);
    }

}
