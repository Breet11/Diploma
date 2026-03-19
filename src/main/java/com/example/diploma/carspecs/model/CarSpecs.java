package com.example.diploma.carspecs.model;

import com.example.diploma.carbrand.model.CarBrand;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity(name="dip_car_specs")
public class CarSpecs {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    private CarBrand carBrand;

    @Column(nullable = false)
    private String acceleration;

    @Column(nullable = false)
    private Long topSpeed;

    @Column(nullable = false)
    private Long releaseYear;
}
