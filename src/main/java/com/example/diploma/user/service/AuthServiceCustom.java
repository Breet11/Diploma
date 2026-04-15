package com.example.diploma.user.service;

import com.example.diploma.security.jwt.JwtService;
import com.example.diploma.user.dto.AuthMessageResponseDto;
import com.example.diploma.user.dto.LoginRequestDto;
import com.example.diploma.user.dto.LoginResponseDto;
import com.example.diploma.user.dto.RegisterRequestDto;
import com.example.diploma.user.model.Role;
import com.example.diploma.user.model.User;
import com.example.diploma.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthServiceCustom implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final DecryptAuthPasswordService decryptAuthPasswordService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthServiceCustom.class);

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        LOGGER.info("Trying to authenticate user [{}]", loginRequestDto.login());
        String decryptedPassword = decryptAuthPasswordService.decrypt(loginRequestDto.password());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.login(),
                        decryptedPassword
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

    @Override
    public AuthMessageResponseDto register(RegisterRequestDto registerRequestDto) {
        userRepository.findByLogin(registerRequestDto.login())
                .ifPresent(user -> {
                    throw new IllegalArgumentException(
                            String.format("User with login %s already exists", registerRequestDto.login())
                    );
                });

        String decryptedPassword = decryptAuthPasswordService.decrypt(registerRequestDto.password());

        User user = new User(
                null,
                registerRequestDto.email(),
                registerRequestDto.login(),
                passwordEncoder.encode(decryptedPassword),
                Role.USER
        );
        userRepository.save(user);

        return new AuthMessageResponseDto("User registered successfully");
    }
}