package com.example.diploma.enginespecs.service;

import com.example.diploma.enginespecs.dto.CreateEngineSpecsRequest;
import com.example.diploma.enginespecs.model.EngineSpecs;

public interface CreateEngineSpecsService {
    EngineSpecs createEngineSpecs(CreateEngineSpecsRequest createEngineSpecsRequest);
}


