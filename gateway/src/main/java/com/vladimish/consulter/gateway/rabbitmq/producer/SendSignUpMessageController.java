package com.vladimish.consulter.gateway.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.vladimish.consulter.gateway.rabbitmq.ConfigureRabbitMQ;
import com.vladimish.consulter.gateway.rabbitmq.holder.RegisterHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class SendSignUpMessageController {

    private final RabbitTemplate rabbitTemplate;

    public SendSignUpMessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/register")
    public String sendMessage(@RequestBody RegisterRequest r) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        String str = null;
        try {
            str = objectMapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            return e.toString();
        }

        log.info("Sending msg: " + str);

        MessageProperties props = new MessageProperties();
        props.setExpiration("20000");
        Message m = new Message(str.getBytes(), props);
        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME, "consulter.auth.client.register", m);

        var t = LocalDateTime.now().plusSeconds(20);

        log.info(LocalDateTime.now().toString());
        while (RegisterHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0 && t.isAfter(LocalDateTime.now())) {
        }

        log.info("time or ok");

        if (!t.isAfter(LocalDateTime.now()) || RegisterHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0) {
            log.info("timeout");
            throw new RuntimeException("Timed out");
        }

        return "Message sent: " + str;
    }

}
