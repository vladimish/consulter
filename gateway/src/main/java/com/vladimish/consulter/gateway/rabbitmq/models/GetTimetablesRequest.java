package com.vladimish.consulter.gateway.rabbitmq.models;

public class GetTimetablesRequest {
    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String consumer;

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email;

    public void setType(String type) {
        this.type = type;
    }

    String type;

    public String getType() {
        return type;
    }
}
