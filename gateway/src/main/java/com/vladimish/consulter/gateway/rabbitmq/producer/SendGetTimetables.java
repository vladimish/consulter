package com.vladimish.consulter.gateway.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.gateway.rabbitmq.ConfigureRabbitMQ;
import com.vladimish.consulter.gateway.rabbitmq.holder.CheckHolder;
import com.vladimish.consulter.gateway.rabbitmq.holder.GetTimetablesHolder;
import com.vladimish.consulter.gateway.rabbitmq.holder.LoginHolder;
import com.vladimish.consulter.gateway.rabbitmq.holder.RegisterHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.CheckReply;
import com.vladimish.consulter.gateway.rabbitmq.models.GetTimetableRequest;
import com.vladimish.consulter.gateway.rabbitmq.models.GetTimetablesRequest;
import com.vladimish.consulter.gateway.rabbitmq.models.RegisterRequest;
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
public class SendGetTimetables {

    private final RabbitTemplate rabbitTemplate;

    public SendGetTimetables(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/get_timetables")
    public String sendMessage(@RequestBody GetTimetableRequest r) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        r.setConsumer(System.getenv("TOKEN"));
        String str = "";
        try {
            str = objectMapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            return e.toString();
        }

        log.info("Sending msg: " + str);

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
        try {
            c = (CheckReply) CheckHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).toArray()[0];
            log.info(objectMapper.writeValueAsString(c));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // Send email to timetable service.
        GetTimetablesRequest req2 = new GetTimetablesRequest();
        req2.setConsumer(System.getenv("TOKEN"));
        req2.setId(r.getId());
        req2.setEmail(c.getEmail());
        req2.setType(r.getType());

        var str2 = "";

        try {
            str2 = objectMapper.writeValueAsString(req2);
        } catch (JsonProcessingException e) {
            return e.toString();
        }

        log.info(str2);

        var msg = new Message(str2.getBytes(), props);
        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME, ConfigureRabbitMQ.PRODUCER_TIMETABLE_GET_QUEUE_NAME, msg);

        t = LocalDateTime.now().plusSeconds(20);

        ow = true;
        while (ow) {
            try {
                while (GetTimetablesHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0 && t.isAfter(LocalDateTime.now())) {
                    ow = false;
                }
            } catch (ConcurrentModificationException e) {

            }
        }

        if (!t.isAfter(LocalDateTime.now()) || GetTimetablesHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0) {
            throw new RuntimeException("Timed out");
        }

        var res = "";

        try {
            res = objectMapper.writeValueAsString(GetTimetablesHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).toList().get(0));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info(res);

        return res;
    }

}
