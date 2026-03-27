package com.example.diploma.enginetype.service;

import com.example.diploma.enginetype.EngineType;
import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;

public interface CreateEngineTypeService {
    EngineType createEngineType(CreateEngineTypeRequest createEngineTypeRequest);
}


