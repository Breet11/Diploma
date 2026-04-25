package com.example.diploma.rental.service;

import com.example.diploma.rental.dto.RentalOrderAdminListItemResponse;
import com.example.diploma.rental.dto.RentalOrderProfileListItemResponse;
import com.example.diploma.rental.model.RentalOrder;
import com.example.diploma.rental.model.RentalOrderStatus;
import com.example.diploma.rental.repository.RentalOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetRentalOrdersServiceCustom implements GetRentalOrdersService {
    private final RentalOrderRepository rentalOrderRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RentalOrderAdminListItemResponse> getRentalOrders() {
        return rentalOrderRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(this::toAdminResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RentalOrderProfileListItemResponse> getCurrentUserOrders(String currentLogin) {
        if (currentLogin == null || currentLogin.isBlank()) {
            throw new IllegalArgumentException("Current user login is required");
        }

        return rentalOrderRepository.findAllByUser_LoginOrderByCreatedAtDesc(currentLogin).stream()
                .map(this::toProfileResponse)
                .toList();
    }

    @Override
    @Transactional
    public void updateOrderStatus(UUID rentalOrderUuid, String status) {
        if (rentalOrderUuid == null) {
            throw new IllegalArgumentException("Rental order id is required");
        }

        RentalOrder order = rentalOrderRepository.findById(rentalOrderUuid)
                .orElseThrow(() -> new IllegalArgumentException("Rental order with id " + rentalOrderUuid + " not found"));

        RentalOrderStatus normalizedStatus = RentalOrderStatus.fromValue(status);
        order.setStatus(normalizedStatus.name());
        rentalOrderRepository.save(order);
    }

    private RentalOrderAdminListItemResponse toAdminResponse(RentalOrder order) {
        String customer = order.getUser() != null
                ? String.format("%s (%s)", order.getUser().getLogin(), order.getUser().getEmail())
                : String.format("%s %s", order.getFirstName(), order.getLastName());

        String phone = order.getPhone() == null || order.getPhone().isBlank() ? "-" : order.getPhone();

        return new RentalOrderAdminListItemResponse(
                order.getUuid(),
                customer,
                phone,
                getCarLabel(order),
                order.getHours(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

    private RentalOrderProfileListItemResponse toProfileResponse(RentalOrder order) {
        return new RentalOrderProfileListItemResponse(
                order.getUuid(),
                getCarLabel(order),
                order.getHours(),
                order.getTotalPrice(),
                order.getStatus(),
                order.getCreatedAt()
        );
    }

    private String getCarLabel(RentalOrder order) {
        return String.format(
                "%s %s",
                order.getCar().getCarSpecs().getCarBrand().getName(),
                order.getCar().getCarSpecs().getCarModel().getName()
        );
    }
}

