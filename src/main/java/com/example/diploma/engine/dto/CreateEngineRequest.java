package com.example.diploma.engine.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateEngineRequest(@NotNull UUID engineSpecsUuid) {
}

