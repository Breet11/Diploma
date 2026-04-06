package com.example.diploma.car.service;

import com.example.diploma.car.dto.CarCatalogItemResponse;
import com.example.diploma.car.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class GetCarCatalogServiceCustom implements GetCarCatalogService {
    private final CarRepository carRepository;

    @Override
    @Transactional(readOnly = true)
    public List<CarCatalogItemResponse> getCatalog() {
        return StreamSupport.stream(carRepository.findAll().spliterator(), false)
                .map(car -> new CarCatalogItemResponse(
                        car.getUuid(),
                        car.getCarSpecs().getCarBrand().getName(),
                        car.getCarSpecs().getCarModel().getName(),
                        car.getCarSpecs().getReleaseYear(),
                        car.getCarSpecs().getTopSpeed(),
                        car.getCarSpecs().getAcceleration(),
                        car.getEngine().getEngineSpecs().getEngineType().getEngineType(),
                        car.getPrice(),
                        car.getImageBlob() == null ? null : Base64.getEncoder().encodeToString(car.getImageBlob()),
                        car.getImageContentType(),
                        car.isAvailable()
                ))
                .toList();
    }
}

