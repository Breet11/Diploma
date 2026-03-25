package com.example.diploma.carbrand.model;

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
@Table(name = "dip_car_brand")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarBrand {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "carBrand", fetch = FetchType.LAZY)
    private List<CarSpecs> carSpecs = new ArrayList<>();
}
