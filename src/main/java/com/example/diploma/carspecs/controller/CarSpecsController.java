package com.example.diploma.carspecs.controller;

import com.example.diploma.carspecs.dto.CreateCarSpecsRequest;
import com.example.diploma.carspecs.service.CreateCarSpecsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/car-specs")
@RequiredArgsConstructor
public class CarSpecsController {
    private final CreateCarSpecsService createCarSpecsService;

    @PostMapping
    public ResponseEntity<UUID> createCarSpecs(@Valid @RequestBody CreateCarSpecsRequest createCarSpecsRequest) {
        return ResponseEntity.ok(createCarSpecsService.createCarSpecs(createCarSpecsRequest).getUuid());
    }
}

