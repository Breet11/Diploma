package com.example.diploma.carmodel.model;

import com.example.diploma.carspecs.model.CarSpecs;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import java.util.UUID;

@Entity
@Table(name = "dip_car_model")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarModel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "carModel", fetch = FetchType.LAZY)
    private List<CarSpecs> carSpecs = new ArrayList<>();
}
