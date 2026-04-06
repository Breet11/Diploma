package com.example.diploma.rental.service;

import com.example.diploma.rental.dto.RentalOrderAdminListItemResponse;

import java.util.List;

public interface GetRentalOrdersService {
    List<RentalOrderAdminListItemResponse> getRentalOrders();
}

