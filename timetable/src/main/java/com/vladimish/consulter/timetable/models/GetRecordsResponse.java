package com.vladimish.consulter.timetable.models;

import com.vladimish.consulter.timetable.db.Record;

public class GetRecordsResponse {
    String consumer;

    public String getConsumer() {
        return consumer;
    }

    public void setConsumer(String consumer) {
        this.consumer = consumer;
    }

    public RecordResponse[] getRecords() {
        return records;
    }

    public void setRecords(RecordResponse[] records) {
        this.records = records;
    }

    String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    RecordResponse[] records;
}
