package com.vladimish.consulter.gateway.rabbitmq.models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Random;
import java.util.UUID;

public class RegisterRequest {
    public String getFirsName() {
        return firsName;
    }

    public void setFirsName(String firsName) {
        this.firsName = firsName;
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

    public void setId(UUID id) {
        this.id = id;
    }

    String firsName;
    String lastName;
    String email;
    String password;
    UUID id;

    public RegisterRequest(@JsonAlias("first-name") String firsName, @JsonAlias("last-name") String lastName, String password, String email) {
        this.firsName = firsName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.id = new UUID(new Random().nextLong(), new Random().nextLong());
    }

    public UUID getId() {
        return this.id;
    }

}
