package com.example.diploma.enginetype;

import com.example.diploma.enginespecs.model.EngineSpecs;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Entity
@Table(name = "dip_engine_type")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EngineType {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false, unique = true)
    private String engineType;

    @OneToMany(mappedBy = "engineType", fetch = FetchType.LAZY)
    private List<EngineSpecs> engineSpecs = new ArrayList<>();
}
