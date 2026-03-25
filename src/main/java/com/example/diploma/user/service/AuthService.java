package com.example.diploma.user.service;

import com.example.diploma.security.jwt.JwtService;
import com.example.diploma.user.dto.LoginRequestDto;
import com.example.diploma.user.dto.LoginResponseDto;
import com.example.diploma.user.dto.RegisterRequestDto;
import com.example.diploma.user.model.Role;
import com.example.diploma.user.model.User;
import com.example.diploma.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.login(),
                        loginRequestDto.password()
                )
        );

        User user = userRepository.findByLogin(loginRequestDto.login())
                .orElseThrow(() -> new IllegalArgumentException("Invalid login or password"));
        String accessToken = jwtService.generateToken(user);

        return new LoginResponseDto(
                accessToken,
                "Bearer",
                jwtService.getJwtExpirationMs(),
                user.getRole().name()
        );
    }

    public ResponseEntity<String> register(RegisterRequestDto registerRequestDto) {
        userRepository.findByLogin(registerRequestDto.login())
                .ifPresent(user -> {
                    throw new IllegalArgumentException(
                            String.format("User with login %s already exists", registerRequestDto.login())
                    );
                });

        User user = new User(
                null,
                registerRequestDto.email(),
                registerRequestDto.login(),
                passwordEncoder.encode(registerRequestDto.password()),
                Role.USER
        );
        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully");
    }
}
