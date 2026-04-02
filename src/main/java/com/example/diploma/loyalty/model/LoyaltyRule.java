package com.example.diploma.loyalty.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "dip_rental_loyalty_rule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoyaltyRule {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @Column(name = "min_hours", nullable = false)
    private Long minHours;

    @Column(name = "max_hours")
    private Long maxHours;

    @Column(name = "multiplier", nullable = false, precision = 8, scale = 4)
    private BigDecimal multiplier;

    @Column(nullable = false)
    private boolean active;
}

