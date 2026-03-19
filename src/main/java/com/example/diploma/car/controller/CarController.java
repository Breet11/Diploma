package com.example.diploma.car.controller;

import com.example.diploma.car.model.Car;
import com.example.diploma.car.service.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping
    public ResponseEntity<List<Car>> getAllCars(){
        return ResponseEntity
                .ok(carService.getAllCars());
    }
}
