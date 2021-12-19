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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String email;
    String token;
    LocalDateTime expires;
}
