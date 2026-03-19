package com.example.diploma.user.service;

import com.example.diploma.user.UserRepository;
import com.example.diploma.user.dto.LoginRequestDto;
import com.example.diploma.user.dto.LoginResponseDto;
import com.example.diploma.user.dto.RegisterRequestDto;
import com.example.diploma.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository<User> userRepository;

    public LoginResponseDto login(LoginRequestDto loginRequestDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.login(),
                        loginRequestDto.hashedPassword()
                )
        );
    }

    public ResponseEntity<String> register(RegisterRequestDto registerRequestDto){
        var existingUser = userRepository
                .findByLogin(registerRequestDto.login())
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("User with login %s already exists", registerRequestDto.login()))
                );
        User user = new User(
                UUID.randomUUID(),
                registerRequestDto.login(),
                registerRequestDto.password(),
                registerRequestDto.email()
        );
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
