package com.example.diploma.loyalty.service;

import com.example.diploma.loyalty.dto.CreateLoyaltyRuleRequest;
import com.example.diploma.loyalty.model.LoyaltyRule;

public interface CreateLoyaltyRuleService {
    LoyaltyRule createLoyaltyRule(CreateLoyaltyRuleRequest createLoyaltyRuleRequest);
}

