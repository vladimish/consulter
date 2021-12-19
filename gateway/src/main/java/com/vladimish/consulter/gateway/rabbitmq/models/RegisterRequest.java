package com.vladimish.consulter.gateway.rabbitmq.models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Random;
import java.util.UUID;

public class RegisterRequest {
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    String consumer;
    String firstName;
    String lastName;
    String email;
    String password;
    String id;

    public RegisterRequest(@JsonAlias("first-name") String firsName, @JsonAlias("last-name") String lastName, String password, String email) {
        this.firstName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = UUID.randomUUID().toString();
        this.consumer = System.getenv("TOKEN");
    }

    public String getId() {
        return this.id;
    }

}
