package com.vladimish.consulter.auth.models;

import java.util.Random;
import java.util.UUID;

public class RegisterRequest {
    String name;
    String email;
    UUID id;

    public RegisterRequest() {
        this.id = new UUID(new Random().nextLong(), new Random().nextLong());
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public UUID getId(){
        return this.id;
    }

}
