package com.example.diploma.enginespecs.model;

import com.example.diploma.enginetype.EngineType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "dip_engine_specs")
public class EngineSpecs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private EngineType engineType;

    @Column(nullable = false)
    private String fuelConsumption;

    @Column(nullable = false)
    private Long horsepower;

    @Column(nullable = false)
    private Long torque;
}
