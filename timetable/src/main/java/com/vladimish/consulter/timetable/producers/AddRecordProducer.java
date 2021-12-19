package com.vladimish.consulter.timetable.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimish.consulter.timetable.ConfigureRabbitMQ;
import com.vladimish.consulter.timetable.models.AddRecordResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AddRecordProducer {
    private static RabbitTemplate rabbitTemplate;

    public AddRecordProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void sendResponse(AddRecordResponse response){

        ObjectMapper objectMapper = new ObjectMapper();

        String str = null;
        try {
            str = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        rabbitTemplate.convertAndSend(str, ConfigureRabbitMQ.CONSUMER_TIMETABLE_CREATE_QUEUE_NAME + "." + response.getConsumer() + ".reply");
    }

}
