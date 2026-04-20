package com.example.diploma.enginetype.service;

import com.example.diploma.enginetype.EngineType;
import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;

import java.util.UUID;

public interface CreateEngineTypeService {
    EngineType createEngineType(CreateEngineTypeRequest createEngineTypeRequest);

    EngineType updateEngineType(UUID uuid, CreateEngineTypeRequest createEngineTypeRequest);

    void deleteEngineType(UUID uuid);
}


