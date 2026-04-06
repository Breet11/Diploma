package com.example.diploma.engine.controller;

import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.repository.EngineRepository;
import com.example.diploma.engine.service.CreateEngineService;
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
@RequestMapping("/engines")
@RequiredArgsConstructor
public class EngineController {
    private final CreateEngineService createEngineService;
    private final EngineRepository engineRepository;

    @GetMapping
    public ResponseEntity<List<EngineListItem>> getEngines() {
        List<EngineListItem> items = engineRepository.findAll().stream()
                .map(engine -> new EngineListItem(
                        engine.getUuid(),
                        engine.getEngineName(),
                        engine.getEngineSpecs().getEngineType().getEngineType(),
                        engine.getEngineSpecs().getFuelConsumption(),
                        engine.getEngineSpecs().getHorsepower(),
                        engine.getEngineSpecs().getTorque(),
                        engine.getEngineSpecs().getEngineVolume()
                ))
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<UUID> createEngine(@Valid @RequestBody CreateEngineRequest createEngineRequest) {
        return ResponseEntity.ok(createEngineService.createEngine(createEngineRequest).getUuid());
    }

    public record EngineListItem(
            UUID uuid,
            String engineName,
            String engineType,
            String fuelConsumption,
            Long horsepower,
            Long torque,
            BigDecimal engineVolume
    ) {
    }
}

