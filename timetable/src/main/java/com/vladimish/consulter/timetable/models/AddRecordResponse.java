package com.vladimish.consulter.timetable.models;

public class AddRecordResponse {
    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String consumer;
    String status;
}
