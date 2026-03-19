package com.example.diploma.car.model;

import com.example.diploma.engine.model.Engine;
import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "dip_car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private Engine engine;

    @Column(nullable = false)
    private Long price;

    private boolean available;
}
