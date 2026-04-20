package com.example.diploma.carbrand.service;

import com.example.diploma.carbrand.dto.CreateCarBrandRequest;
import com.example.diploma.carbrand.model.CarBrand;
import com.example.diploma.carbrand.repository.CarBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class CreateCarBrandServiceCustom implements CreateCarBrandService {
    private final CarBrandRepository carBrandRepository;

    @Override
    public List<CarBrand> getAllCarBrands() {
        return StreamSupport.stream(
                carBrandRepository.findAll().spliterator(),
                false
        ).toList();
    }

    @Override
    public CarBrand createCarBrand(CreateCarBrandRequest createCarBrandRequest) {
        CarBrand carBrand = new CarBrand();
        carBrand.setName(createCarBrandRequest.name());
        return carBrandRepository.save(carBrand);
    }

    @Override
    public CarBrand updateCarBrand(UUID uuid, CreateCarBrandRequest createCarBrandRequest) {
        CarBrand carBrand = carBrandRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Car brand with id " + uuid + " not found"));
        carBrand.setName(createCarBrandRequest.name());
        return carBrandRepository.save(carBrand);
    }

    @Override
    public void deleteCarBrand(UUID uuid) {
        CarBrand carBrand = carBrandRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Car brand with id " + uuid + " not found"));
        carBrandRepository.delete(carBrand);
    }
}

