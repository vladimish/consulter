package com.vladimish.consulter.gateway.rabbitmq.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladimish.consulter.gateway.rabbitmq.ConfigureRabbitMQ;
import com.vladimish.consulter.gateway.rabbitmq.holder.RegisterHolder;
import com.vladimish.consulter.gateway.rabbitmq.models.RegisterReply;
import com.vladimish.consulter.gateway.rabbitmq.models.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class SendMessageController {

    private final RabbitTemplate rabbitTemplate;

    public SendMessageController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @PostMapping("/register")
    public String sendMessage(@RequestBody RegisterRequest r) {
        ObjectMapper objectMapper = new ObjectMapper();
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
        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME, "consulter.auth.client", m);

        var t = LocalDateTime.now().plusSeconds(20);

        log.info(LocalDateTime.now().toString());
        while (RegisterHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0 && t.isAfter(LocalDateTime.now())) {
        }

        if (!t.isAfter(LocalDateTime.now()) || RegisterHolder.getINSTANCE().list.stream().filter(o -> o.getId().equals(r.getId())).count() == 0) {
            return "Timed out";
        }

        return "Message sent: " + str;
    }

}
