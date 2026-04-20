package com.example.diploma.engine.service;

import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.model.Engine;
import com.example.diploma.engine.repository.EngineRepository;
import com.example.diploma.enginespecs.model.EngineSpecs;
import com.example.diploma.enginetype.repository.EngineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEngineServiceCustom implements CreateEngineService {
    private final EngineRepository engineRepository;
    private final EngineTypeRepository engineTypeRepository;

    @Override
    public Engine createEngine(CreateEngineRequest createEngineRequest) {
        var engineType = engineTypeRepository.findById(createEngineRequest.engineTypeUuid())
                .orElseThrow(() -> new IllegalArgumentException("Engine type with id " + createEngineRequest.engineTypeUuid() + " not found"));

        EngineSpecs engineSpecs = new EngineSpecs();
        engineSpecs.setEngineType(engineType);
        engineSpecs.setFuelConsumption(createEngineRequest.fuelConsumption());
        engineSpecs.setHorsepower(createEngineRequest.horsepower());
        engineSpecs.setTorque(createEngineRequest.torque());
        engineSpecs.setEngineVolume(createEngineRequest.engineVolume());

        Engine engine = new Engine();
        engine.setEngineName(createEngineRequest.engineName());
        engine.setEngineSpecs(engineSpecs);

        return engineRepository.save(engine);
    }

    @Override
    public Engine updateEngine(java.util.UUID uuid, CreateEngineRequest createEngineRequest) {
        Engine engine = engineRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Engine with id " + uuid + " not found"));
        var engineType = engineTypeRepository.findById(createEngineRequest.engineTypeUuid())
                .orElseThrow(() -> new IllegalArgumentException("Engine type with id " + createEngineRequest.engineTypeUuid() + " not found"));

        EngineSpecs engineSpecs = engine.getEngineSpecs();
        if (engineSpecs == null) {
            engineSpecs = new EngineSpecs();
            engine.setEngineSpecs(engineSpecs);
        }
        engineSpecs.setEngineType(engineType);
        engineSpecs.setFuelConsumption(createEngineRequest.fuelConsumption());
        engineSpecs.setHorsepower(createEngineRequest.horsepower());
        engineSpecs.setTorque(createEngineRequest.torque());
        engineSpecs.setEngineVolume(createEngineRequest.engineVolume());
        engine.setEngineName(createEngineRequest.engineName());

        return engineRepository.save(engine);
    }

    @Override
    public void deleteEngine(java.util.UUID uuid) {
        Engine engine = engineRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Engine with id " + uuid + " not found"));
        engineRepository.delete(engine);
    }
}


