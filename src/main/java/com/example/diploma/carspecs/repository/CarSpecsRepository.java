package com.example.diploma.carspecs.repository;

import com.example.diploma.carspecs.model.CarSpecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CarSpecsRepository extends JpaRepository<CarSpecs, UUID> {
}

