package com.example.diploma.user.service;

import com.example.diploma.user.dto.CreateUserRequestDto;
import com.example.diploma.user.model.Role;
import com.example.diploma.user.model.User;
import com.example.diploma.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserServiceCustom implements CreateUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserRequestDto createUserRequestDto) {
        userRepository.findByLogin(createUserRequestDto.login())
                .ifPresent(user -> {
                    throw new IllegalArgumentException(
                            String.format("User with login %s already exists", createUserRequestDto.login())
                    );
                });

        Role role = createUserRequestDto.role() == null ? Role.USER : createUserRequestDto.role();
        User user = new User(
                null,
                createUserRequestDto.email(),
                createUserRequestDto.login(),
                passwordEncoder.encode(createUserRequestDto.password()),
                role
        );

        return userRepository.save(user);
    }
}


