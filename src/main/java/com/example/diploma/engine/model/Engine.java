package com.example.diploma.engine.model;

import com.example.diploma.car.model.Car;
import com.example.diploma.enginespecs.model.EngineSpecs;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "dip_engine")
@Getter
@Setter
@NoArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "engine_specs_uuid", nullable = false, unique = true)
    private EngineSpecs engineSpecs;

    @OneToOne(mappedBy = "engine", fetch = FetchType.LAZY)
    private Car car;
}
