package com.vladimish.consulter.gateway.rabbitmq.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterReply {
    String id;
    String status;
    LocalDateTime time;

    public String getConsumer() {
        return consumer;
    }

    String consumer;

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
