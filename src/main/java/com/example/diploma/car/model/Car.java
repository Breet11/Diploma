package com.example.diploma.car.model;

import com.example.diploma.carspecs.model.CarSpecs;
import com.example.diploma.engine.model.Engine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "dip_car")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "engine_uuid", nullable = false, unique = true)
    private Engine engine;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_specs_uuid", nullable = false, unique = true)
    private CarSpecs carSpecs;

    @Column(nullable = false)
    private Long price;

    private boolean available;
}
