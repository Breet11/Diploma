package com.example.diploma.car.controller;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.service.CreateCarService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/cars")
@RequiredArgsConstructor
public class CarController {
    private final CreateCarService createCarService;

    @PostMapping
    public ResponseEntity<UUID> createCar(@Valid @RequestBody CreateCarRequest createCarRequest) {
        return ResponseEntity.ok(createCarService.createCar(createCarRequest).getUuid());
    }
}
