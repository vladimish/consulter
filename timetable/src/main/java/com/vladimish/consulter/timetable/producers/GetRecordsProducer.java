package com.vladimish.consulter.timetable.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.timetable.ConfigureRabbitMQ;
import com.vladimish.consulter.timetable.models.GetRecordsResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class GetRecordsProducer {
    private static RabbitTemplate rabbitTemplate;

    public GetRecordsProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static void sendResponse(GetRecordsResponse response){

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

        String str = null;
        try {
            str = objectMapper.writeValueAsString(response);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME ,ConfigureRabbitMQ.CONSUMER_TIMETABLE_GET_QUEUE_NAME + "." + response.getConsumer() + ".reply", str);
    }

}
