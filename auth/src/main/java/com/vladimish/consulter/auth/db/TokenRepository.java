package com.vladimish.consulter.auth.db;

import com.vladimish.consulter.auth.db.models.Token;
import com.vladimish.consulter.auth.db.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, Long> {
    List<Token> findAllByEmail(String email);
    List<Token> findAllByToken(String token);
}
