package com.example.diploma.loyalty.controller;

import com.example.diploma.loyalty.dto.CreateLoyaltyRuleRequest;
import com.example.diploma.loyalty.repository.LoyaltyRuleRepository;
import com.example.diploma.loyalty.service.CreateLoyaltyRuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/loyalty-rules")
@RequiredArgsConstructor
public class LoyaltyRuleController {
    private final CreateLoyaltyRuleService createLoyaltyRuleService;
    private final LoyaltyRuleRepository loyaltyRuleRepository;

    @GetMapping
    public ResponseEntity<List<LoyaltyRuleListItem>> getLoyaltyRules() {
        List<LoyaltyRuleListItem> items = StreamSupport.stream(loyaltyRuleRepository.findAll().spliterator(), false)
                .map(rule -> new LoyaltyRuleListItem(
                        rule.getUuid(),
                        rule.getMinHours(),
                        rule.getMaxHours(),
                        rule.getMultiplier(),
                        rule.isActive()
                ))
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping
    public ResponseEntity<UUID> createLoyaltyRule(@Valid @RequestBody CreateLoyaltyRuleRequest createLoyaltyRuleRequest) {
        return ResponseEntity.ok(createLoyaltyRuleService.createLoyaltyRule(createLoyaltyRuleRequest).getUuid());
    }

    public record LoyaltyRuleListItem(
            UUID uuid,
            Long minHours,
            Long maxHours,
            BigDecimal multiplier,
            boolean active
    ) {
    }
}

