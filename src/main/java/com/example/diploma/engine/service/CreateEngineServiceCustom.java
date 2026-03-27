package com.example.diploma.engine.service;

import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.model.Engine;
import com.example.diploma.engine.repository.EngineRepository;
import com.example.diploma.enginespecs.repository.EngineSpecsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEngineServiceCustom implements CreateEngineService {
    private final EngineRepository engineRepository;
    private final EngineSpecsRepository engineSpecsRepository;

    @Override
    public Engine createEngine(CreateEngineRequest createEngineRequest) {
        var engineSpecs = engineSpecsRepository.findById(createEngineRequest.engineSpecsUuid())
                .orElseThrow(() -> new IllegalArgumentException("Engine specs with id " + createEngineRequest.engineSpecsUuid() + " not found"));

        Engine engine = new Engine();
        engine.setEngineSpecs(engineSpecs);

        return engineRepository.save(engine);
    }
}


