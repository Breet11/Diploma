package com.example.diploma.car.model;

import com.example.diploma.carspecs.model.CarSpecs;
import com.example.diploma.engine.model.Engine;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "engine_uuid", nullable = false)
    private Engine engine;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_specs_uuid", nullable = false)
    private CarSpecs carSpecs;

    @Column(nullable = false)
    private Long price;

    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "image_blob", columnDefinition = "bytea")
    private byte[] imageBlob;

    @Column(name = "image_content_type")
    private String imageContentType;

    private boolean available;
}
