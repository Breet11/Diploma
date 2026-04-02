package com.example.diploma.rental.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.UUID;

public record CreateRentalRequest(
        @NotNull UUID carUuid,
        @NotNull @Positive Long hours,
        String firstName,
        String lastName,
        String phone
) {
    public boolean hasGuestContactData() {
        return notBlank(firstName) && notBlank(lastName) && notBlank(phone);
    }

    private boolean notBlank(String value) {
        return value != null && !value.isBlank();
    }
}
