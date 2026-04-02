package com.example.diploma.rental.service;

import com.example.diploma.rental.dto.CalculateRentalPriceRequest;
import com.example.diploma.rental.dto.CalculateRentalPriceResponse;

public interface CalculateRentalPriceService {
    CalculateRentalPriceResponse calculatePrice(CalculateRentalPriceRequest calculateRentalPriceRequest);
}

