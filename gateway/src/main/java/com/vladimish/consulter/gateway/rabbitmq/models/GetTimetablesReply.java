package com.vladimish.consulter.gateway.rabbitmq.models;

import java.time.LocalDateTime;

public class GetTimetablesReply {
    String id;
    String consumer;

    public Record[] getRecords() {
        return records;
    }

    public void setRecords(Record[] records) {
        this.records = records;
    }

    Record[] records;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

}
