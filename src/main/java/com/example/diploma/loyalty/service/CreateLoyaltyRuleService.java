package com.example.diploma.loyalty.service;

import com.example.diploma.loyalty.dto.CreateLoyaltyRuleRequest;
import com.example.diploma.loyalty.model.LoyaltyRule;

import java.util.UUID;

public interface CreateLoyaltyRuleService {
    LoyaltyRule createLoyaltyRule(CreateLoyaltyRuleRequest createLoyaltyRuleRequest);

    LoyaltyRule updateLoyaltyRule(UUID uuid, CreateLoyaltyRuleRequest createLoyaltyRuleRequest);

    void deleteLoyaltyRule(UUID uuid);
}

