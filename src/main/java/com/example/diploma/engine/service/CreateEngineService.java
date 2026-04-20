package com.example.diploma.engine.service;

import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.model.Engine;

import java.util.UUID;

public interface CreateEngineService {
    Engine createEngine(CreateEngineRequest createEngineRequest);

    Engine updateEngine(UUID uuid, CreateEngineRequest createEngineRequest);

    void deleteEngine(UUID uuid);
}


