package com.example.diploma.enginespecs.controller;

import com.example.diploma.enginespecs.dto.CreateEngineSpecsRequest;
import com.example.diploma.enginespecs.repository.EngineSpecsRepository;
import com.example.diploma.enginespecs.service.CreateEngineSpecsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/engine-specs")
@RequiredArgsConstructor
public class EngineSpecsController {
    private final CreateEngineSpecsService createEngineSpecsService;
    private final EngineSpecsRepository engineSpecsRepository;

    @GetMapping
    public ResponseEntity<List<EngineSpecsListItem>> getEngineSpecs() {
        List<EngineSpecsListItem> items = engineSpecsRepository.findAll().stream()
                .map(specs -> new EngineSpecsListItem(
                        specs.getUuid(),
                        specs.getEngineType().getEngineType(),
                        specs.getFuelConsumption(),
                        specs.getHorsepower(),
                        specs.getTorque(),
                        specs.getEngineVolume()
                ))
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<UUID> createEngineSpecs(@Valid @RequestBody CreateEngineSpecsRequest createEngineSpecsRequest) {
        return ResponseEntity.ok(createEngineSpecsService.createEngineSpecs(createEngineSpecsRequest).getUuid());
    }

    public record EngineSpecsListItem(
            UUID uuid,
            String engineType,
            String fuelConsumption,
            Long horsepower,
            Long torque,
            BigDecimal engineVolume
    ) {
    }
}

