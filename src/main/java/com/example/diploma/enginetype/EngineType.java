package com.example.diploma.enginetype;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "dip_engine_type")
public class EngineType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String engineType;
}
