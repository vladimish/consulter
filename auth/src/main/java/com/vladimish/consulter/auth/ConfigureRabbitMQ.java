package com.vladimish.consulter.auth;

import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMQ {
    public static final String EXCHANGE_NAME = "consulter.exchange";
    public static final String PRODUCER_REGISTER_QUEUE_NAME = "consulter.auth.client.register";
    public static final String PRODUCER_LOGIN_QUEUE_NAME = "consulter.auth.client.login";
    public static final String PRODUCER_CHECK_QUEUE_NAME = "consulter.auth.client.check";
}