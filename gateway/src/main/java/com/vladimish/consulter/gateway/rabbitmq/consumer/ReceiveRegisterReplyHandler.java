package com.vladimish.consulter.gateway.rabbitmq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.gateway.rabbitmq.ConfigureRabbitMQ;
import com.vladimish.consulter.gateway.rabbitmq.holder.RegisterHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.RegisterReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReceiveRegisterReplyHandler {

    @RabbitListener(queues = ConfigureRabbitMQ.CONSUMER_AUTH_QUEUE_NAME)
    public void handleRegisterReply(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        RegisterReply reply = new RegisterReply();
        try {
            reply = objectMapper.readValue(messageBody, RegisterReply.class);
        } catch (JsonProcessingException e) {
            // TODO: Add error handling.
            e.printStackTrace();
        }

        RegisterHolder.getINSTANCE().list.add(reply);
        log.info("Handling message" + messageBody);
    }
}
