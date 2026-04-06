package com.example.diploma.car.service;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;
import org.springframework.web.multipart.MultipartFile;

public interface CreateCarService {
    Car createCar(CreateCarRequest createCarRequest);

    Car createCar(CreateCarRequest createCarRequest, MultipartFile imageFile);
}
