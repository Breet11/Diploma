package com.example.diploma.utils;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ResponseMessage {
    private final int statusCode;
    private final String message;
    private final Object data;
    private final LocalDateTime timestamp;
}
