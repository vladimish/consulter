package com.vladimish.consulter.auth.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterReply {

    String id;

    public void setId(String id) {
        this.id = id;
    }

    String status;

    public void setStatus(String status) {
        this.status = status;
    }

    LocalDateTime time;

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    String consumer;
}
