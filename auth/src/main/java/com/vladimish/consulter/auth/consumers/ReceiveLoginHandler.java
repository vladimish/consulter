package com.vladimish.consulter.auth.consumers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vladimish.consulter.auth.ConfigureRabbitMQ;
import com.vladimish.consulter.auth.db.TokenRepository;
import com.vladimish.consulter.auth.db.UserRepository;
import com.vladimish.consulter.auth.db.models.Token;
import com.vladimish.consulter.auth.db.models.User;
import com.vladimish.consulter.auth.models.LoginReply;
import com.vladimish.consulter.auth.models.LoginRequest;
import com.vladimish.consulter.auth.models.RegisterReply;
import com.vladimish.consulter.auth.models.RegisterRequest;
import com.vladimish.consulter.auth.producers.SendLoginReply;
import com.vladimish.consulter.auth.producers.SendRegisterReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class ReceiveLoginHandler {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TokenRepository tokenRepository;

    @RabbitListener(queues = ConfigureRabbitMQ.PRODUCER_LOGIN_QUEUE_NAME)
    public void handleRegisterReply(String messageBody) {
        log.info("Handling message" + messageBody);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.KEBAB_CASE);
        LoginRequest request = new LoginRequest();
        try {
            request = objectMapper.readValue(messageBody, LoginRequest.class);
        } catch (JsonProcessingException e) {
            // TODO: Add error handling.
            e.printStackTrace();
        }

        LoginReply reply = new LoginReply();
        reply.setId(request.getId());
        reply.setConsumer(request.getConsumer());

        var users = userRepository.findAllByEmail(request.getEmail());
        if (users.size() > 0) {
            if (users.get(0).getPassword().equals(request.getPassword())) {
                reply.setStatus("OK");
                var token = UUID.randomUUID().toString();
                reply.setToken(token);
                reply.setPrivileges(users.get(0).getPrivileges());
                Token t = new Token(request.getEmail(), token, LocalDateTime.now().minusWeeks(1));
                tokenRepository.save(t);
            } else {
                reply.setStatus("UNAUTHORIZED");
            }
        } else {
            reply.setStatus("USER_IS_NOT_REGISTERED");
        }

        SendLoginReply.sendMessage(reply);
    }
}
