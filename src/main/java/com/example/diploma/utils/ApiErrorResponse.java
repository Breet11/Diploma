package com.example.diploma.utils;

import java.time.OffsetDateTime;
import java.util.Map;

public record ApiErrorResponse(
        int status,
        String error,
        String message,
        String path,
        OffsetDateTime timestamp,
        Map<String, String> validationErrors
) {
}

