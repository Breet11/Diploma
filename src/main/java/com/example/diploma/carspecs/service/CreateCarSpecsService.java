package com.example.diploma.carspecs.service;

import com.example.diploma.carspecs.dto.CreateCarSpecsRequest;
import com.example.diploma.carspecs.model.CarSpecs;

public interface CreateCarSpecsService {
    CarSpecs createCarSpecs(CreateCarSpecsRequest createCarSpecsRequest);
}


