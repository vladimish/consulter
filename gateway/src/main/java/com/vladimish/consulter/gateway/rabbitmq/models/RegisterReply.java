package com.vladimish.consulter.gateway.rabbitmq.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterReply {
    UUID id;
    String status;
    LocalDateTime time;

    public String getStatus() {
        return status;
    }

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
