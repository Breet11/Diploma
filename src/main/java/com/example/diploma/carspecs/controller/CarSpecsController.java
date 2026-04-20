package com.example.diploma.carspecs.controller;

import com.example.diploma.carspecs.dto.CreateCarSpecsRequest;
import com.example.diploma.carspecs.repository.CarSpecsRepository;
import com.example.diploma.carspecs.service.CreateCarSpecsService;
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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(HttpSpecs.CarSpecs.ROOT)
@RequiredArgsConstructor
public class CarSpecsController {
    private final CreateCarSpecsService createCarSpecsService;
    private final CarSpecsRepository carSpecsRepository;

    @GetMapping(HttpSpecs.CarSpecs.GET_ALL)
    public ResponseEntity<List<CarSpecsListItem>> getCarSpecs() {
        List<CarSpecsListItem> items = carSpecsRepository.findAll().stream()
                .map(specs -> new CarSpecsListItem(
                        specs.getUuid(),
                        specs.getCarBrand().getName(),
                        specs.getCarModel().getName(),
                        specs.getAcceleration(),
                        specs.getTopSpeed(),
                        specs.getReleaseYear()
                ))
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping(HttpSpecs.CarSpecs.CREATE)
    public ResponseEntity<UUID> createCarSpecs(@Valid @RequestBody CreateCarSpecsRequest createCarSpecsRequest) {
        return ResponseEntity.ok(createCarSpecsService.createCarSpecs(createCarSpecsRequest).getUuid());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UUID> updateCarSpecs(
            @PathVariable UUID uuid,
            @Valid @RequestBody CreateCarSpecsRequest createCarSpecsRequest
    ) {
        return ResponseEntity.ok(createCarSpecsService.updateCarSpecs(uuid, createCarSpecsRequest).getUuid());
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<UUID> patchCarSpecs(
            @PathVariable UUID uuid,
            @Valid @RequestBody CreateCarSpecsRequest createCarSpecsRequest
    ) {
        return ResponseEntity.ok(createCarSpecsService.updateCarSpecs(uuid, createCarSpecsRequest).getUuid());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCarSpecs(@PathVariable UUID uuid) {
        createCarSpecsService.deleteCarSpecs(uuid);
        return ResponseEntity.noContent().build();
    }

    public record CarSpecsListItem(
            UUID uuid,
            String brand,
            String model,
            String acceleration,
            Long topSpeed,
            Long releaseYear
    ) {
    }
}

