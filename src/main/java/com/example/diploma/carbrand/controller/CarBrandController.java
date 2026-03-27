package com.example.diploma.carbrand.controller;

import com.example.diploma.carbrand.dto.CreateCarBrandRequest;
import com.example.diploma.carbrand.service.CreateCarBrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/car-brands")
@RequiredArgsConstructor
public class CarBrandController {
    private final CreateCarBrandService createCarBrandService;

    @PostMapping
    public ResponseEntity<UUID> createCarBrand(@Valid @RequestBody CreateCarBrandRequest createCarBrandRequest) {
        return ResponseEntity.ok(createCarBrandService.createCarBrand(createCarBrandRequest).getUuid());
    }
}

