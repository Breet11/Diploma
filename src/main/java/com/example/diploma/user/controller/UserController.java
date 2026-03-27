package com.example.diploma.user.controller;

import com.example.diploma.user.dto.CreateUserRequestDto;
import com.example.diploma.user.service.CreateUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final CreateUserService createUserService;

    @PostMapping
    public ResponseEntity<UUID> createUser(@Valid @RequestBody CreateUserRequestDto createUserRequestDto) {
        return ResponseEntity.ok(createUserService.createUser(createUserRequestDto).getUuid());
    }
}

