package com.example.diploma.engine.controller;

import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.service.CreateEngineService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/engines")
@RequiredArgsConstructor
public class EngineController {
    private final CreateEngineService createEngineService;

    @PostMapping
    public ResponseEntity<UUID> createEngine(@Valid @RequestBody CreateEngineRequest createEngineRequest) {
        return ResponseEntity.ok(createEngineService.createEngine(createEngineRequest).getUuid());
    }
}

