package com.example.diploma.user.service;

import com.example.diploma.user.dto.ProfileResponseDto;
import com.example.diploma.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetProfileServiceCustom implements GetProfileService {
    private final UserRepository userRepository;

    @Override
    public ProfileResponseDto getProfile(String login) {
        var user = userRepository.findByLogin(login)
                .orElseThrow(() -> new IllegalArgumentException("User with login " + login + " not found"));

        return new ProfileResponseDto(user.getEmail(), user.getLogin(), user.getRole().name());
    }
}

