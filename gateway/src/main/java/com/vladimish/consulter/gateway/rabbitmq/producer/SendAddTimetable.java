package com.vladimish.consulter.gateway.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.gateway.rabbitmq.ConfigureRabbitMQ;
import com.vladimish.consulter.gateway.rabbitmq.holder.CheckHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.AddTimetableRequest;
import com.vladimish.consulter.gateway.rabbitmq.models.AddTimetablesRequest;
import com.vladimish.consulter.gateway.rabbitmq.models.CheckReply;
import com.vladimish.consulter.gateway.rabbitmq.models.GetTimetableRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.UUID;

@Slf4j
@RestController
public class SendAddTimetable {

    private final RabbitTemplate rabbitTemplate;

    public SendAddTimetable(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/add_timetable")
    public String sendMessage(@RequestBody AddTimetableRequest r) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        objectMapper.registerModule(new JavaTimeModule());

        AddTimetablesRequest req = new AddTimetablesRequest();
        req.setClient(r.getClient());
        req.setConsumer(System.getenv("TOKEN"));
        req.setEmployee("");
        req.setId(UUID.randomUUID().toString());
        req.setStart(r.getStart());
        req.setTopic(r.getTopic());

        var res = "{}";
        try {
            res = objectMapper.writeValueAsString(req);
            log.info("TEST: " + res);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME, ConfigureRabbitMQ.PRODUCER_TIMETABLE_CREATE_QUEUE_NAME, res);

        return "Da";
    }
}