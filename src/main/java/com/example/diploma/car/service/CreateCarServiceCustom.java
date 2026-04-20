package com.example.diploma.car.service;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;
import com.example.diploma.car.repository.CarRepository;
import com.example.diploma.carspecs.repository.CarSpecsRepository;
import com.example.diploma.engine.repository.EngineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCarServiceCustom implements CreateCarService {
    private final CarRepository carRepository;
    private final EngineRepository engineRepository;
    private final CarSpecsRepository carSpecsRepository;

    @Override
    public Car createCar(CreateCarRequest createCarRequest) {
        return createCar(createCarRequest, null);
    }

    @Override
    public Car createCar(CreateCarRequest createCarRequest, MultipartFile imageFile) {
        var engine = engineRepository.findById(createCarRequest.engineUuid())
                .orElseThrow(() -> new IllegalArgumentException("Engine with id " + createCarRequest.engineUuid() + " not found"));
        var carSpecs = carSpecsRepository.findById(createCarRequest.carSpecsUuid())
                .orElseThrow(() -> new IllegalArgumentException("Car specs with id " + createCarRequest.carSpecsUuid() + " not found"));

        Car car = new Car();
        car.setEngine(engine);
        car.setCarSpecs(carSpecs);
        car.setPrice(createCarRequest.price());
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                car.setImageBlob(imageFile.getBytes());
                car.setImageContentType(imageFile.getContentType());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Unable to read uploaded image", exception);
            }
        }
        car.setAvailable(createCarRequest.available());

        return carRepository.save(car);
    }

    @Override
    public Car updateCar(UUID uuid, CreateCarRequest createCarRequest) {
        return updateCar(uuid, createCarRequest, null);
    }

    @Override
    public Car updateCar(UUID uuid, CreateCarRequest createCarRequest, MultipartFile imageFile) {
        Car car = carRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Car with id " + uuid + " not found"));
        var engine = engineRepository.findById(createCarRequest.engineUuid())
                .orElseThrow(() -> new IllegalArgumentException("Engine with id " + createCarRequest.engineUuid() + " not found"));
        var carSpecs = carSpecsRepository.findById(createCarRequest.carSpecsUuid())
                .orElseThrow(() -> new IllegalArgumentException("Car specs with id " + createCarRequest.carSpecsUuid() + " not found"));

        car.setEngine(engine);
        car.setCarSpecs(carSpecs);
        car.setPrice(createCarRequest.price());
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                car.setImageBlob(imageFile.getBytes());
                car.setImageContentType(imageFile.getContentType());
            } catch (Exception exception) {
                throw new IllegalArgumentException("Unable to read uploaded image", exception);
            }
        }
        car.setAvailable(createCarRequest.available());

        return carRepository.save(car);
    }

    @Override
    public void deleteCar(UUID uuid) {
        Car car = carRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Car with id " + uuid + " not found"));
        carRepository.delete(car);
    }
}
