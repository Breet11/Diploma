package com.example.diploma.car.service;

import com.example.diploma.car.dto.CarCatalogItemResponse;
import com.example.diploma.car.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GetCarCatalogServiceCustom implements GetCarCatalogService {
    private final CarRepository carRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CarCatalogItemResponse> getCatalog() {
        return carRepository.findCatalogItems().stream()
                .map(item -> new CarCatalogItemResponse(
                        item.getUuid(),
                        item.getBrand(),
                        item.getModel(),
                        item.getReleaseYear(),
                        item.getTopSpeed(),
                        item.getAcceleration(),
                        item.getEngineType(),
                        item.getHourlyRentalPrice(),
                        item.getImageBlob() == null ? null : Base64.getEncoder().encodeToString(item.getImageBlob()),
                        item.getImageContentType(),
                        item.isAvailable()
                ))
                .toList();
    }
}

