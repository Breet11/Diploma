package com.example.diploma.enginetype.service;

import com.example.diploma.enginetype.EngineType;
import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;
import com.example.diploma.enginetype.repository.EngineTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    @Override
    public EngineType updateEngineType(UUID uuid, CreateEngineTypeRequest createEngineTypeRequest) {
        EngineType engineType = engineTypeRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Engine type with id " + uuid + " not found"));
        engineType.setEngineType(createEngineTypeRequest.engineType());
        return engineTypeRepository.save(engineType);
    }

    @Override
    public void deleteEngineType(UUID uuid) {
        EngineType engineType = engineTypeRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Engine type with id " + uuid + " not found"));
        engineTypeRepository.delete(engineType);
    }
}


