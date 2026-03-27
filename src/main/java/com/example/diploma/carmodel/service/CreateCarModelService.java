package com.example.diploma.carmodel.service;

import com.example.diploma.carmodel.dto.CreateCarModelRequest;
import com.example.diploma.carmodel.model.CarModel;

public interface CreateCarModelService {
    CarModel createCarModel(CreateCarModelRequest createCarModelRequest);
}

