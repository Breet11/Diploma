package com.example.diploma.car.service;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface CreateCarService {
    Car createCar(CreateCarRequest createCarRequest);

    Car createCar(CreateCarRequest createCarRequest, MultipartFile imageFile);

    Car updateCar(UUID uuid, CreateCarRequest createCarRequest);

    Car updateCar(UUID uuid, CreateCarRequest createCarRequest, MultipartFile imageFile);

    void deleteCar(UUID uuid);
}
