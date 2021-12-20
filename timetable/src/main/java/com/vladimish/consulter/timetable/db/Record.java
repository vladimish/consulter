package com.vladimish.consulter.timetable.db;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Record {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long id;
    String topic;
    String client;
    String employee;
    LocalDateTime start;

    public Record(String client, String employee, LocalDateTime start, String topic) {
        this.client = client;
        this.employee = employee;
        this.start = start;
        this.topic = topic;
    }

    public Record() {

    }
}
