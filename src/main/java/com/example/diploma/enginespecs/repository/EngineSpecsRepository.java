package com.example.diploma.enginespecs.repository;

import com.example.diploma.enginespecs.model.EngineSpecs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EngineSpecsRepository extends JpaRepository<EngineSpecs, UUID> {
}

