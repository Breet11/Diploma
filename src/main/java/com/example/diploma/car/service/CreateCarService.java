package com.example.diploma.car.service;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;

import java.util.List;

public interface CreateCarService {
    List<Car> getAllCars();

    Car createCar(CreateCarRequest createCarRequest);
}

