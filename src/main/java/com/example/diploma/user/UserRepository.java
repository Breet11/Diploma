package com.example.diploma.user;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository<T> extends CrudRepository<T, UUID> {
    Optional<T> findByLogin(String login);
}
