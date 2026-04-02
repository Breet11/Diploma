package com.example.diploma.car.service;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;

public interface CreateCarService {
    Car createCar(CreateCarRequest createCarRequest);
}
