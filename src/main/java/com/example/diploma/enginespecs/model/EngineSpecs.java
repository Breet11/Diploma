package com.example.diploma.enginespecs.model;

import com.example.diploma.engine.model.Engine;
import com.example.diploma.enginetype.EngineType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "dip_engine_specs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EngineSpecs {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "engine_type_uuid", nullable = false)
    private EngineType engineType;

    @Column(nullable = false)
    private String fuelConsumption;

    @Column(nullable = false)
    private Long horsepower;

    @Column(nullable = false)
    private Long torque;

    @OneToOne(mappedBy = "engineSpecs")
    private Engine engine;
}
