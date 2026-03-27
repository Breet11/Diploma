package com.example.diploma.carbrand.service;

import com.example.diploma.carbrand.dto.CreateCarBrandRequest;
import com.example.diploma.carbrand.model.CarBrand;
import com.example.diploma.carbrand.repository.CarBrandRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
}

