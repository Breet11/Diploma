package com.example.diploma.enginetype.repository;

import com.example.diploma.enginetype.EngineType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EngineTypeRepository extends JpaRepository<EngineType, UUID> {
}

