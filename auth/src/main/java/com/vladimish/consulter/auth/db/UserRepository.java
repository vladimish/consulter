package com.vladimish.consulter.auth.db;

import com.vladimish.consulter.auth.db.models.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAllByEmail(String email);
}
