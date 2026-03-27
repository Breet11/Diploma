package com.example.diploma.user.service;

import com.example.diploma.user.dto.LoginRequestDto;
import com.example.diploma.user.dto.LoginResponseDto;
import com.example.diploma.user.dto.RegisterRequestDto;
import org.springframework.http.ResponseEntity;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);

    ResponseEntity<String> register(RegisterRequestDto registerRequestDto);
}
