package com.example.diploma.rental.service;

import com.example.diploma.car.repository.CarRepository;
import com.example.diploma.rental.dto.CalculateRentalPriceRequest;
import com.example.diploma.rental.dto.CreateRentalRequest;
import com.example.diploma.rental.dto.CreateRentalResponse;
import com.example.diploma.rental.model.RentalOrder;
import com.example.diploma.rental.repository.RentalOrderRepository;
import com.example.diploma.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CreateRentalServiceCustom implements CreateRentalService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final RentalOrderRepository rentalOrderRepository;
    private final CalculateRentalPriceService calculateRentalPriceService;

    @Override
    public CreateRentalResponse createRental(CreateRentalRequest createRentalRequest, String currentLogin) {
        var car = carRepository.findById(createRentalRequest.carUuid())
                .orElseThrow(() -> new IllegalArgumentException("Car with id " + createRentalRequest.carUuid() + " not found"));

        var priceInfo = calculateRentalPriceService.calculatePrice(
                new CalculateRentalPriceRequest(createRentalRequest.carUuid(), createRentalRequest.hours())
        );

        RentalOrder rentalOrder = new RentalOrder();
        rentalOrder.setCar(car);
        rentalOrder.setHours(createRentalRequest.hours());
        rentalOrder.setTotalPrice(priceInfo.totalPrice());
        rentalOrder.setStatus("NEW");
        rentalOrder.setCreatedAt(OffsetDateTime.now());

        if (currentLogin != null) {
            var user = userRepository.findByLogin(currentLogin)
                    .orElseThrow(() -> new IllegalArgumentException("User with login " + currentLogin + " not found"));
            rentalOrder.setUser(user);
        } else {
            if (!createRentalRequest.hasGuestContactData()) {
                throw new IllegalArgumentException("Guest must provide firstName, lastName and phone");
            }
            rentalOrder.setFirstName(createRentalRequest.firstName());
            rentalOrder.setLastName(createRentalRequest.lastName());
            rentalOrder.setPhone(createRentalRequest.phone());
        }

        var saved = rentalOrderRepository.save(rentalOrder);

        return new CreateRentalResponse(
                saved.getUuid(),
                saved.getTotalPrice(),
                "Заявка успешно создана. С вами свяжется менеджер."
        );
    }
}

