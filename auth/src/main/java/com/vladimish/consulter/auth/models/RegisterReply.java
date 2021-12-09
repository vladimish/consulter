package com.vladimish.consulter.auth.models;

import java.time.LocalDateTime;
import java.util.UUID;

public class RegisterReply {

    UUID id;

    public void setId(UUID id) {
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

    public UUID getId() {
        return id;
    }

    public LocalDateTime getTime() {
        return time;
    }
}
