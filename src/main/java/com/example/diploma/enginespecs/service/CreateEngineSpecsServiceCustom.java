package com.example.diploma.enginespecs.service;

import com.example.diploma.enginespecs.dto.CreateEngineSpecsRequest;
import com.example.diploma.enginespecs.model.EngineSpecs;
import com.example.diploma.enginespecs.repository.EngineSpecsRepository;
import com.example.diploma.enginetype.repository.EngineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEngineSpecsServiceCustom implements CreateEngineSpecsService {
    private final EngineSpecsRepository engineSpecsRepository;
    private final EngineTypeRepository engineTypeRepository;

    @Override
    public EngineSpecs createEngineSpecs(CreateEngineSpecsRequest createEngineSpecsRequest) {
        var engineType = engineTypeRepository.findById(createEngineSpecsRequest.engineTypeUuid())
                .orElseThrow(() -> new IllegalArgumentException("Engine type with id " + createEngineSpecsRequest.engineTypeUuid() + " not found"));

        EngineSpecs engineSpecs = new EngineSpecs();
        engineSpecs.setEngineType(engineType);
        engineSpecs.setFuelConsumption(createEngineSpecsRequest.fuelConsumption());
        engineSpecs.setHorsepower(createEngineSpecsRequest.horsepower());
        engineSpecs.setTorque(createEngineSpecsRequest.torque());

        return engineSpecsRepository.save(engineSpecs);
    }
}


