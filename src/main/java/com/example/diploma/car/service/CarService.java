package com.example.diploma.car.service;

import com.example.diploma.car.model.Car;
import com.example.diploma.car.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository<Car> carCarRepository;
    public List<Car> getAllCars(){
        return carCarRepository.getAllCars().orElseThrow(
                () -> new IllegalArgumentException("No cars found")
        );
    }
}
