package com.example.diploma.rental.service;

import com.example.diploma.car.repository.CarRepository;
import com.example.diploma.loyalty.model.LoyaltyRule;
import com.example.diploma.loyalty.repository.LoyaltyRuleRepository;
import com.example.diploma.rental.dto.CalculateRentalPriceRequest;
import com.example.diploma.rental.dto.CalculateRentalPriceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CalculateRentalPriceServiceCustom implements CalculateRentalPriceService {
    private final CarRepository carRepository;
    private final LoyaltyRuleRepository loyaltyRuleRepository;

    @Override
    @Transactional(readOnly = true)
    public CalculateRentalPriceResponse calculatePrice(CalculateRentalPriceRequest calculateRentalPriceRequest) {
        var car = carRepository.findById(calculateRentalPriceRequest.carUuid())
                .orElseThrow(() -> new IllegalArgumentException("Car with id " + calculateRentalPriceRequest.carUuid() + " not found"));

        BigDecimal baseHourlyPrice = BigDecimal.valueOf(car.getPrice());
        BigDecimal hours = BigDecimal.valueOf(calculateRentalPriceRequest.hours());
        BigDecimal multiplier = resolveMultiplier(calculateRentalPriceRequest.hours());

        long totalPrice = baseHourlyPrice
                .multiply(hours)
                .multiply(multiplier)
                .setScale(0, RoundingMode.HALF_UP)
                .longValue();

        return new CalculateRentalPriceResponse(
                car.getPrice(),
                calculateRentalPriceRequest.hours(),
                multiplier,
                totalPrice
        );
    }

    private BigDecimal resolveMultiplier(Long hours) {
        return loyaltyRuleRepository.findMatchingRules(hours).stream()
                .findFirst()
                .map(LoyaltyRule::getMultiplier)
                .orElse(BigDecimal.ONE);
    }
}

