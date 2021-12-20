package com.vladimish.consulter.auth.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.auth.ConfigureRabbitMQ;
import com.vladimish.consulter.auth.db.TokenRepository;
import com.vladimish.consulter.auth.db.UserRepository;
import com.vladimish.consulter.auth.db.models.Token;
import com.vladimish.consulter.auth.models.CheckReply;
import com.vladimish.consulter.auth.models.CheckRequest;
import com.vladimish.consulter.auth.models.LoginReply;
import com.vladimish.consulter.auth.models.LoginRequest;
import com.vladimish.consulter.auth.producers.SendCheckReply;
import com.vladimish.consulter.auth.producers.SendLoginReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class ReceiveCheckHandler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @RabbitListener(queues = ConfigureRabbitMQ.PRODUCER_CHECK_QUEUE_NAME)
    public void handleCheckReply(String messageBody) {
        log.info("Handling message" + messageBody);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        CheckRequest request = new CheckRequest();
        try {
            request = objectMapper.readValue(messageBody, CheckRequest.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        CheckReply reply = new CheckReply();
        reply.setId(request.getId());
        reply.setConsumer(request.getConsumer());

        var userEmials = tokenRepository.findAllByToken(request.getToken());
        var email = "";
        if (userEmials.size() > 0) {
            email = userEmials.get(0).getEmail();
        } else {
            throw new AmqpRejectAndDontRequeueException("Email not found");
        }

        var users = userRepository.findAllByEmail(email);
        if (users.size() > 0) {
            reply.setName(users.get(0).getFirstName() + " " + users.get(0).getLastName());
            var token = UUID.randomUUID().toString();
            reply.setEmail(email);
        } else {
            throw new RuntimeException("User not found");
        }

        log.info(SendCheckReply.sendMessage(reply));
    }
}