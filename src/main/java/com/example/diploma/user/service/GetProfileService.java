package com.example.diploma.user.service;

import com.example.diploma.user.dto.ProfileResponseDto;

public interface GetProfileService {
    ProfileResponseDto getProfile(String login);
}

