package com.example.diploma.carspecs.model;

import com.example.diploma.car.model.Car;
import com.example.diploma.carbrand.model.CarBrand;
import com.example.diploma.carmodel.model.CarModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "dip_car_specs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarSpecs {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_brand_uuid", nullable = false)
    private CarBrand carBrand;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_model_uuid", nullable = false)
    private CarModel carModel;

    @Column(nullable = false)
    private String acceleration;

    @Column(nullable = false)
    private Long topSpeed;

    @Column(nullable = false)
    private Long releaseYear;

    @OneToOne(mappedBy = "carSpecs")
    private Car car;
}
