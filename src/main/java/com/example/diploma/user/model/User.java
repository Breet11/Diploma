package com.example.diploma.user.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "login" ,nullable = false)
    private String login;

    @Column(name = "hashed_password", nullable = false)
    private String hashedPassword;
}
