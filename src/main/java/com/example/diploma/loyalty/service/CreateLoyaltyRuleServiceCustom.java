package com.example.diploma.loyalty.service;

import com.example.diploma.loyalty.dto.CreateLoyaltyRuleRequest;
import com.example.diploma.loyalty.model.LoyaltyRule;
import com.example.diploma.loyalty.repository.LoyaltyRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateLoyaltyRuleServiceCustom implements CreateLoyaltyRuleService {
    private final LoyaltyRuleRepository loyaltyRuleRepository;

    @Override
    public LoyaltyRule createLoyaltyRule(CreateLoyaltyRuleRequest createLoyaltyRuleRequest) {
        if (createLoyaltyRuleRequest.maxHours() != null && createLoyaltyRuleRequest.maxHours() < createLoyaltyRuleRequest.minHours()) {
            throw new IllegalArgumentException("maxHours must be greater than or equal to minHours");
        }

        LoyaltyRule loyaltyRule = new LoyaltyRule();
        loyaltyRule.setMinHours(createLoyaltyRuleRequest.minHours());
        loyaltyRule.setMaxHours(createLoyaltyRuleRequest.maxHours());
        loyaltyRule.setMultiplier(createLoyaltyRuleRequest.multiplier());
        loyaltyRule.setActive(createLoyaltyRuleRequest.active());

        return loyaltyRuleRepository.save(loyaltyRule);
    }
}

