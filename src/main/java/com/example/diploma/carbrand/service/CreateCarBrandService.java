package com.example.diploma.carbrand.service;

import com.example.diploma.carbrand.dto.CreateCarBrandRequest;
import com.example.diploma.carbrand.model.CarBrand;

import java.util.List;
import java.util.UUID;

public interface CreateCarBrandService {
    List<CarBrand> getAllCarBrands();

    CarBrand createCarBrand(CreateCarBrandRequest createCarBrandRequest);

    CarBrand updateCarBrand(UUID uuid, CreateCarBrandRequest createCarBrandRequest);

    void deleteCarBrand(UUID uuid);
}

