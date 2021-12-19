package com.vladimish.consulter.gateway.rabbitmq.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.gateway.rabbitmq.holder.CheckHolder;
import com.vladimish.consulter.gateway.rabbitmq.holder.GetTimetablesHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.CheckReply;
import com.vladimish.consulter.gateway.rabbitmq.models.GetTimetablesReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ReceiveGetTimetableHandler {

    @RabbitListener(queues = "${ConfigureRabbitMQ.CONSUMER_TIMETABLE_GET_QUEUE_NAME}")
    public void handleGetTimetablesReply(String messageBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        GetTimetablesReply reply = new GetTimetablesReply();

        log.info("Handling: " + messageBody);

        try {
            reply = objectMapper.readValue(messageBody, GetTimetablesReply.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        GetTimetablesHolder.getINSTANCE().list.add(reply);
        log.info("Handling message" + messageBody);
    }

}