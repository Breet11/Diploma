package com.example.diploma.rental.service;

import com.example.diploma.rental.dto.RentalOrderAdminListItemResponse;
import com.example.diploma.rental.repository.RentalOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class GetRentalOrdersServiceCustom implements GetRentalOrdersService {
    private final RentalOrderRepository rentalOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RentalOrderAdminListItemResponse> getRentalOrders() {
        return StreamSupport.stream(rentalOrderRepository.findAll().spliterator(), false)
                .map(order -> {
                    String customer = order.getUser() != null
                            ? String.format("%s (%s)", order.getUser().getLogin(), order.getUser().getEmail())
                            : String.format("%s %s", order.getFirstName(), order.getLastName());

                    String phone = order.getPhone() == null || order.getPhone().isBlank() ? "-" : order.getPhone();
                    String car = String.format(
                            "%s %s",
                            order.getCar().getCarSpecs().getCarBrand().getName(),
                            order.getCar().getCarSpecs().getCarModel().getName()
                    );

                    return new RentalOrderAdminListItemResponse(
                            order.getUuid(),
                            customer,
                            phone,
                            car,
                            order.getHours(),
                            order.getTotalPrice(),
                            order.getStatus(),
                            order.getCreatedAt()
                    );
                })
                .toList();
    }
}

