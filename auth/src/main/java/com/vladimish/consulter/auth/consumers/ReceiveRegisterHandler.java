package com.vladimish.consulter.auth.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.vladimish.consulter.auth.ConfigureRabbitMQ;
import com.vladimish.consulter.auth.db.UserRepository;
import com.vladimish.consulter.auth.db.models.User;
import com.vladimish.consulter.auth.models.RegisterReply;
import com.vladimish.consulter.auth.models.RegisterRequest;
import com.vladimish.consulter.auth.producers.SendRegisterReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class ReceiveRegisterHandler {

    @Autowired
    private UserRepository userRepository;

    @RabbitListener(queues = ConfigureRabbitMQ.PRODUCER_AUTH_QUEUE_NAME)
    public void handleRegisterReply(String messageBody) {
        log.info("Handling message" + messageBody);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        RegisterRequest request = new RegisterRequest(null, null, null, null, null);
        try {
            request = objectMapper.readValue(messageBody, RegisterRequest.class);
        } catch (JsonProcessingException e) {
            // TODO: Add error handling.
            e.printStackTrace();
        }

        var users = userRepository.findAllByEmail(request.getEmail());
        if (users.size() == 0) {
            var newUser = new User(request.getFirstName(), request.getLastName(), request.getEmail(), request.getPassword());
            userRepository.save(newUser);
        } else {
            log.info("User already exists");
        }

        RegisterReply registerReply = new RegisterReply();
        registerReply.setId(request.getId());
        registerReply.setStatus("OK");
        registerReply.setTime(LocalDateTime.now());

        SendRegisterReply.sendMessage(registerReply);
    }
}
