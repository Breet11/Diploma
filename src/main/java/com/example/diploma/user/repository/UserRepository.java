package com.example.diploma.user.repository;

import com.example.diploma.user.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    Optional<User> findByLogin(String login);
}
