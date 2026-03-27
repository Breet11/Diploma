package com.example.diploma.engine.service;

import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.model.Engine;

public interface CreateEngineService {
    Engine createEngine(CreateEngineRequest createEngineRequest);
}


