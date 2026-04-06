package com.example.diploma.car.controller;

import com.example.diploma.car.dto.CarCatalogItemResponse;
import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.service.CreateCarService;
import com.example.diploma.car.service.GetCarCatalogService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CreateCarService createCarService;
    private final GetCarCatalogService getCarCatalogService;

    @GetMapping
    public ResponseEntity<List<CarCatalogItemResponse>> getCarsCatalog() {
        return ResponseEntity.ok(getCarCatalogService.getCatalog());
    }

    @PostMapping
    public ResponseEntity<UUID> createCar(
            @Valid @RequestPart("payload") CreateCarRequest createCarRequest,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(createCarService.createCar(createCarRequest, image).getUuid());
    }
}
