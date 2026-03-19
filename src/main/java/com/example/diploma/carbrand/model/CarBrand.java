package com.example.diploma.carbrand.model;

import jakarta.persistence.*;

import java.util.UUID;

@Entity(name = "dip_car_brand")
public class CarBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String name;
}
