package com.vladimish.consulter.gateway.rabbitmq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.gateway.rabbitmq.holder.CheckHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.CheckReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReceiveCheckReplyHandler {

    @RabbitListener(queues = "${ConfigureRabbitMQ.CONSUMER_CHECK_QUEUE_NAME}")
    public void handleCheckReply(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        CheckReply reply = new CheckReply();
        try {
            reply = objectMapper.readValue(messageBody, CheckReply.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        CheckHolder.getINSTANCE().list.add(reply);
        log.info("Handling message" + messageBody);
    }

}
