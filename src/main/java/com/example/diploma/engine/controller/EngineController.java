package com.example.diploma.engine.controller;

import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.repository.EngineRepository;
import com.example.diploma.engine.service.CreateEngineService;
import com.example.diploma.utils.HTTP.HttpSpecs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(HttpSpecs.Engine.ROOT)
@RequiredArgsConstructor
public class EngineController {
    private final CreateEngineService createEngineService;
    private final EngineRepository engineRepository;

    @GetMapping(HttpSpecs.Engine.GET_ALL)
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

    @PostMapping(HttpSpecs.Engine.CREATE)
    public ResponseEntity<UUID> createEngine(@Valid @RequestBody CreateEngineRequest createEngineRequest) {
        return ResponseEntity.ok(createEngineService.createEngine(createEngineRequest).getUuid());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UUID> updateEngine(
            @PathVariable UUID uuid,
            @Valid @RequestBody CreateEngineRequest createEngineRequest
    ) {
        return ResponseEntity.ok(createEngineService.updateEngine(uuid, createEngineRequest).getUuid());
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<UUID> patchEngine(
            @PathVariable UUID uuid,
            @Valid @RequestBody CreateEngineRequest createEngineRequest
    ) {
        return ResponseEntity.ok(createEngineService.updateEngine(uuid, createEngineRequest).getUuid());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteEngine(@PathVariable UUID uuid) {
        createEngineService.deleteEngine(uuid);
        return ResponseEntity.noContent().build();
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

