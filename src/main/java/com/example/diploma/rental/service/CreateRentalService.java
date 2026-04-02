package com.example.diploma.rental.service;

import com.example.diploma.rental.dto.CreateRentalRequest;
import com.example.diploma.rental.dto.CreateRentalResponse;

public interface CreateRentalService {
    CreateRentalResponse createRental(CreateRentalRequest createRentalRequest, String currentLogin);
}

