package com.vladimish.consulter.auth.db.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Token {

    public Token(String email, String token, LocalDateTime expires) {
        this.email = email;
        this.token = token;
        this.expires = expires;
    }

    public Token(){

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpires() {
        return expires;
    }

    public void setExpires(LocalDateTime expires) {
        this.expires = expires;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String email;
    String token;
    LocalDateTime expires;
}
