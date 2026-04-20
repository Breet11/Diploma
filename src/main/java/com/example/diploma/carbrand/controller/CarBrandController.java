package com.example.diploma.carbrand.controller;

import com.example.diploma.carbrand.dto.CreateCarBrandRequest;
import com.example.diploma.carbrand.service.CreateCarBrandService;
import com.example.diploma.utils.HTTP.HttpSpecs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(HttpSpecs.CarBrand.ROOT)
@RequiredArgsConstructor
public class CarBrandController {
    private final CreateCarBrandService createCarBrandService;

    @GetMapping(HttpSpecs.CarBrand.GET_ALL)
    public ResponseEntity<List<CarBrandListItem>> getCarBrands() {
        List<CarBrandListItem> items = createCarBrandService.getAllCarBrands().stream()
                .map(brand -> new CarBrandListItem(brand.getUuid(), brand.getName()))
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping(HttpSpecs.CarBrand.CREATE)
    public ResponseEntity<UUID> createCarBrand(@Valid @RequestBody CreateCarBrandRequest createCarBrandRequest) {
        return ResponseEntity.ok(createCarBrandService.createCarBrand(createCarBrandRequest).getUuid());
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<UUID> updateCarBrand(
            @PathVariable UUID uuid,
            @Valid @RequestBody CreateCarBrandRequest createCarBrandRequest
    ) {
        return ResponseEntity.ok(createCarBrandService.updateCarBrand(uuid, createCarBrandRequest).getUuid());
    }

    @PatchMapping("/{uuid}")
    public ResponseEntity<UUID> patchCarBrand(
            @PathVariable UUID uuid,
            @Valid @RequestBody CreateCarBrandRequest createCarBrandRequest
    ) {
        return ResponseEntity.ok(createCarBrandService.updateCarBrand(uuid, createCarBrandRequest).getUuid());
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteCarBrand(@PathVariable UUID uuid) {
        createCarBrandService.deleteCarBrand(uuid);
        return ResponseEntity.noContent().build();
    }

    public record CarBrandListItem(UUID uuid, String name) {
    }
}

