package com.example.diploma.engine.model;

import com.example.diploma.enginespecs.model.EngineSpecs;
import com.example.diploma.enginetype.EngineType;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "dip_engine")
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private EngineSpecs engineSpecs;
}
