package com.vladimish.consulter.gateway.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.gateway.rabbitmq.ConfigureRabbitMQ;
import com.vladimish.consulter.gateway.rabbitmq.holder.CheckHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ConcurrentModificationException;
import java.util.UUID;

@Slf4j
@RestController
public class SendEditTimetable {

    private final RabbitTemplate rabbitTemplate;

    public SendEditTimetable(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/edit_timetable")
    public String sendMessage(@RequestBody EditTimetableRequest r) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        objectMapper.registerModule(new JavaTimeModule());


        var ttr = new GetTimetableRequest();
        ttr.setToken(r.employee);
        ttr.setId(UUID.randomUUID().toString());
        ttr.setConsumer(System.getenv("TOKEN"));

        String str = "";
        try {
            str = objectMapper.writeValueAsString(ttr);
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
                while (CheckHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(ttr.getId())).count() == 0 && t.isAfter(LocalDateTime.now())) {
                    ow = false;
                }
            } catch (ConcurrentModificationException e) {

            }
        }

        if (!t.isAfter(LocalDateTime.now()) || CheckHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(ttr.getId())).count() == 0) {
            throw new RuntimeException("Timed out");
        }

        CheckReply c = new CheckReply();
        var res = "";
        try {
            c = (CheckReply) CheckHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(ttr.getId())).toArray()[0];
            res = objectMapper.writeValueAsString(c);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info(res);
        CheckReply ans = new CheckReply();
        try {
            ans = objectMapper.readValue(res, CheckReply.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


        EditTimetablesRequest req = new EditTimetablesRequest();
        req.setClient(r.client);
        req.setConsumer(System.getenv("TOKEN"));
        req.setEmployee(ans.getEmail());
        req.setId(UUID.randomUUID().toString());

        var ret = "{}";
        try {
            ret = objectMapper.writeValueAsString(req);
            log.info("TEST: " + ret);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info(ret);

        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME, ConfigureRabbitMQ.PRODUCER_TIMETABLE_EDIT_QUEUE_NAME, ret);

        return "Da";
    }
}