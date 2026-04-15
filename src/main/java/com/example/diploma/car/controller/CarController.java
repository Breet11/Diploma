package com.example.diploma.car.controller;

import com.example.diploma.car.dto.CarCatalogItemResponse;
import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.service.CreateCarService;
import com.example.diploma.car.service.GetCarCatalogService;
import com.example.diploma.utils.HTTP.HttpSpecs;
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
@RequestMapping(HttpSpecs.Car.ROOT)
@RequiredArgsConstructor
public class CarController {
    private final CreateCarService createCarService;
    private final GetCarCatalogService getCarCatalogService;

    @GetMapping(HttpSpecs.Car.GET_CATALOG)
    public ResponseEntity<List<CarCatalogItemResponse>> getCarsCatalog() {
        return ResponseEntity.ok(getCarCatalogService.getCatalog());
    }

    @PostMapping(HttpSpecs.Car.CREATE)
    public ResponseEntity<UUID> createCar(
            @Valid @RequestPart("payload") CreateCarRequest createCarRequest,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(createCarService.createCar(createCarRequest, image).getUuid());
    }
}
