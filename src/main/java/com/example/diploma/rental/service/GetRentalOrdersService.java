package com.example.diploma.rental.service;

import com.example.diploma.rental.dto.RentalOrderAdminListItemResponse;
import com.example.diploma.rental.dto.RentalOrderProfileListItemResponse;

import java.util.List;
import java.util.UUID;

public interface GetRentalOrdersService {
    List<RentalOrderAdminListItemResponse> getRentalOrders();

    List<RentalOrderProfileListItemResponse> getCurrentUserOrders(String currentLogin);

    void updateOrderStatus(UUID rentalOrderUuid, String status);
}

