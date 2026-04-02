package com.example.diploma.user.controller;

import com.example.diploma.user.dto.ProfileResponseDto;
import com.example.diploma.user.service.GetProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {
    private final GetProfileService getProfileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponseDto> me(Authentication authentication) {
        return ResponseEntity.ok(getProfileService.getProfile(authentication.getName()));
    }
}

