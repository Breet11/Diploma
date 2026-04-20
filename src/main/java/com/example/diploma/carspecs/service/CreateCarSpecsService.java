package com.example.diploma.carspecs.service;

import com.example.diploma.carspecs.dto.CreateCarSpecsRequest;
import com.example.diploma.carspecs.model.CarSpecs;

import java.util.UUID;

public interface CreateCarSpecsService {
    CarSpecs createCarSpecs(CreateCarSpecsRequest createCarSpecsRequest);

    CarSpecs updateCarSpecs(UUID uuid, CreateCarSpecsRequest createCarSpecsRequest);

    void deleteCarSpecs(UUID uuid);
}


