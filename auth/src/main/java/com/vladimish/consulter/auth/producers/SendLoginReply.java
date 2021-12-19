package com.vladimish.consulter.auth.producers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.auth.ConfigureRabbitMQ;
import com.vladimish.consulter.auth.models.LoginReply;
import com.vladimish.consulter.auth.models.RegisterReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SendLoginReply {
    private static RabbitTemplate rabbitTemplate;

    public SendLoginReply(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public static String sendMessage(LoginReply r) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String str = null;
        try {
            str = objectMapper.writeValueAsString(r);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.info("Sending msg: " + str);

        MessageProperties props = new MessageProperties();
        props.setExpiration("20000");
        Message m = new Message(str.getBytes(), props);
        rabbitTemplate.convertAndSend(ConfigureRabbitMQ.EXCHANGE_NAME, "consulter.auth.client.login." + r.getConsumer() + ".reply", m);

        return "Message sent: " + str;
    }
}
