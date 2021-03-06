package com.vladimish.consulter.auth.models;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.Random;
import java.util.UUID;

public class RegisterRequest {

    public RegisterRequest(){

    }

    public RegisterRequest(@JsonAlias("first-name") String firstName, @JsonAlias("last-name") String lastName, String email, String password, UUID id){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
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

}
