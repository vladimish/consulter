package com.vladimish.consulter.frontend.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class Record {

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    String client;

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    String employee;
    LocalDateTime start;
    String topic;
}
