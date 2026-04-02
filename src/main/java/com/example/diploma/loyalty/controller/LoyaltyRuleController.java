package com.example.diploma.loyalty.controller;

import com.example.diploma.loyalty.dto.CreateLoyaltyRuleRequest;
import com.example.diploma.loyalty.service.CreateLoyaltyRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/loyalty-rules")
@RequiredArgsConstructor
public class LoyaltyRuleController {
    private final CreateLoyaltyRuleService createLoyaltyRuleService;

    @PostMapping
    public ResponseEntity<UUID> createLoyaltyRule(@Valid @RequestBody CreateLoyaltyRuleRequest createLoyaltyRuleRequest) {
        return ResponseEntity.ok(createLoyaltyRuleService.createLoyaltyRule(createLoyaltyRuleRequest).getUuid());
    }
}

