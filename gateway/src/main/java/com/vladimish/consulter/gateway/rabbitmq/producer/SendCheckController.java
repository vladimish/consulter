package com.vladimish.consulter.gateway.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vladimish.consulter.gateway.rabbitmq.ConfigureRabbitMQ;
import com.vladimish.consulter.gateway.rabbitmq.holder.CheckHolder;
import com.vladimish.consulter.gateway.rabbitmq.holder.LoginHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.CheckReply;
import com.vladimish.consulter.gateway.rabbitmq.models.GetTimetableRequest;
import com.vladimish.consulter.gateway.rabbitmq.models.LoginRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.UUID;

@Slf4j
@RestController
public class SendCheckController {

    private final RabbitTemplate rabbitTemplate;

    public SendCheckController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/check")
    public String sendMessage(@RequestParam(name = "token") String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);

        var r = new GetTimetableRequest();
        r.setToken(token);
        r.setId(UUID.randomUUID().toString());
        r.setConsumer(System.getenv("TOKEN"));

        String str = "";
        try {
            str = objectMapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            return e.toString();
        }

        MessageProperties props = new MessageProperties();
        props.setExpiration("20000");
        Message m = new Message(str.getBytes(), props);
        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME, ConfigureRabbitMQ.PRODUCER_CHECK_QUEUE_NAME, m);

        var t = LocalDateTime.now().plusSeconds(20);

        log.info(LocalDateTime.now().toString());

        var ow = true;
        while (ow) {
            try {
                while (CheckHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0 && t.isAfter(LocalDateTime.now())) {
                    ow = false;
                }
            } catch (ConcurrentModificationException e) {

            }
        }

        if (!t.isAfter(LocalDateTime.now()) || CheckHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0) {
            throw new RuntimeException("Timed out");
        }

        CheckReply c = new CheckReply();
        var res = "";
        try {
            c = (CheckReply) CheckHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).toArray()[0];
            res = objectMapper.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info(res);
        return res;

    }
}