package com.example.diploma.enginespecs.controller;

import com.example.diploma.enginespecs.dto.CreateEngineSpecsRequest;
import com.example.diploma.enginespecs.service.CreateEngineSpecsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/engine-specs")
@RequiredArgsConstructor
public class EngineSpecsController {
    private final CreateEngineSpecsService createEngineSpecsService;

    @PostMapping
    public ResponseEntity<UUID> createEngineSpecs(@Valid @RequestBody CreateEngineSpecsRequest createEngineSpecsRequest) {
        return ResponseEntity.ok(createEngineSpecsService.createEngineSpecs(createEngineSpecsRequest).getUuid());
    }
}

