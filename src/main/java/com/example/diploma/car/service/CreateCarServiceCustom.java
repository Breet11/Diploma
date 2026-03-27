package com.example.diploma.car.service;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;
import com.example.diploma.car.repository.CarRepository;
import com.example.diploma.carspecs.repository.CarSpecsRepository;
import com.example.diploma.engine.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateCarServiceCustom implements CreateCarService {
    private final CarRepository carRepository;
    private final EngineRepository engineRepository;
    private final CarSpecsRepository carSpecsRepository;

    @Override
    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();
        carRepository.findAll().forEach(cars::add);
        if (cars.isEmpty()) {
            throw new IllegalArgumentException("No cars found");
        }
        return cars;
    }

    @Override
    public Car createCar(CreateCarRequest createCarRequest) {
        var engine = engineRepository.findById(createCarRequest.engineUuid())
                .orElseThrow(() -> new IllegalArgumentException("Engine with id " + createCarRequest.engineUuid() + " not found"));
        var carSpecs = carSpecsRepository.findById(createCarRequest.carSpecsUuid())
                .orElseThrow(() -> new IllegalArgumentException("Car specs with id " + createCarRequest.carSpecsUuid() + " not found"));

        Car car = new Car();
        car.setEngine(engine);
        car.setCarSpecs(carSpecs);
        car.setPrice(createCarRequest.price());
        car.setAvailable(createCarRequest.available());

        return carRepository.save(car);
    }
}


