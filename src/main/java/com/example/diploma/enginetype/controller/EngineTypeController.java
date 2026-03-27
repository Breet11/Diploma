package com.example.diploma.enginetype.controller;

import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;
import com.example.diploma.enginetype.service.CreateEngineTypeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/engine-types")
@RequiredArgsConstructor
public class EngineTypeController {
    private final CreateEngineTypeService createEngineTypeService;

    @PostMapping
    public ResponseEntity<UUID> createEngineType(@Valid @RequestBody CreateEngineTypeRequest createEngineTypeRequest) {
        return ResponseEntity.ok(createEngineTypeService.createEngineType(createEngineTypeRequest).getUuid());
    }
}

