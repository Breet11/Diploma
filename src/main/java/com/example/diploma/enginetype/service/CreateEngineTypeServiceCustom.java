package com.example.diploma.enginetype.service;

import com.example.diploma.enginetype.EngineType;
import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;
import com.example.diploma.enginetype.repository.EngineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateEngineTypeServiceCustom implements CreateEngineTypeService {
    private final EngineTypeRepository engineTypeRepository;

    @Override
    public EngineType createEngineType(CreateEngineTypeRequest createEngineTypeRequest) {
        EngineType engineType = new EngineType();
        engineType.setEngineType(createEngineTypeRequest.engineType());
        return engineTypeRepository.save(engineType);
    }
}


