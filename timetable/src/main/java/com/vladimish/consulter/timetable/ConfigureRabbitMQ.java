package com.vladimish.consulter.timetable;

public class ConfigureRabbitMQ {
    public static final String EXCHANGE_NAME = "consulter.exchange";
    public static final String CONSUMER_TIMETABLE_CREATE_QUEUE_NAME = "consulter.timetable.records.add";
    public static final String CONSUMER_TIMETABLE_GET_QUEUE_NAME = "consulter.timetable.records.get";
}
