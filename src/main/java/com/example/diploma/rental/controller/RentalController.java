package com.example.diploma.rental.controller;

import com.example.diploma.rental.dto.CalculateRentalPriceRequest;
import com.example.diploma.rental.dto.CalculateRentalPriceResponse;
import com.example.diploma.rental.dto.CreateRentalRequest;
import com.example.diploma.rental.dto.CreateRentalResponse;
import com.example.diploma.rental.dto.RentalOrderAdminListItemResponse;
import com.example.diploma.rental.service.CalculateRentalPriceService;
import com.example.diploma.rental.service.CreateRentalService;
import com.example.diploma.rental.service.GetRentalOrdersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rentals")
@RequiredArgsConstructor
public class RentalController {
    private final CreateRentalService createRentalService;
    private final CalculateRentalPriceService calculateRentalPriceService;
    private final GetRentalOrdersService getRentalOrdersService;

    @GetMapping("/admin")
    public ResponseEntity<List<RentalOrderAdminListItemResponse>> getRentalOrdersForAdmin() {
        return ResponseEntity.ok(getRentalOrdersService.getRentalOrders());
    }

    @PostMapping("/price")
    public ResponseEntity<CalculateRentalPriceResponse> calculatePrice(@Valid @RequestBody CalculateRentalPriceRequest calculateRentalPriceRequest) {
        return ResponseEntity.ok(calculateRentalPriceService.calculatePrice(calculateRentalPriceRequest));
    }

    @PostMapping
    public ResponseEntity<CreateRentalResponse> createRental(
            @Valid @RequestBody CreateRentalRequest createRentalRequest,
            Authentication authentication
    ) {
        String currentLogin = authentication == null ? null : authentication.getName();
        return ResponseEntity.ok(createRentalService.createRental(createRentalRequest, currentLogin));
    }
}

