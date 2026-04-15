package com.example.diploma.enginetype.controller;

import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;
import com.example.diploma.enginetype.repository.EngineTypeRepository;
import com.example.diploma.enginetype.service.CreateEngineTypeService;
import com.example.diploma.utils.HTTP.HttpSpecs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(HttpSpecs.EngineType.ROOT)
@RequiredArgsConstructor
public class EngineTypeController {
    private final CreateEngineTypeService createEngineTypeService;
    private final EngineTypeRepository engineTypeRepository;

    @GetMapping(HttpSpecs.EngineType.GET_ALL)
    public ResponseEntity<List<EngineTypeListItem>> getEngineTypes() {
        List<EngineTypeListItem> items = engineTypeRepository.findAll().stream()
                .map(type -> new EngineTypeListItem(type.getUuid(), type.getEngineType()))
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping(HttpSpecs.EngineType.CREATE)
    public ResponseEntity<UUID> createEngineType(@Valid @RequestBody CreateEngineTypeRequest createEngineTypeRequest) {
        return ResponseEntity.ok(createEngineTypeService.createEngineType(createEngineTypeRequest).getUuid());
    }

    private record EngineTypeListItem(UUID uuid, String engineType) {
    }
}

