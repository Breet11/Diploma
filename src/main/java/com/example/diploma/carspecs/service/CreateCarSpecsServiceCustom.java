package com.example.diploma.carspecs.service;

import com.example.diploma.carbrand.repository.CarBrandRepository;
import com.example.diploma.carmodel.repository.CarModelRepository;
import com.example.diploma.carspecs.dto.CreateCarSpecsRequest;
import com.example.diploma.carspecs.model.CarSpecs;
import com.example.diploma.carspecs.repository.CarSpecsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCarSpecsServiceCustom implements CreateCarSpecsService {
    private final CarSpecsRepository carSpecsRepository;
    private final CarBrandRepository carBrandRepository;
    private final CarModelRepository carModelRepository;

    @Override
    public CarSpecs createCarSpecs(CreateCarSpecsRequest createCarSpecsRequest) {
        var carBrand = carBrandRepository.findById(createCarSpecsRequest.carBrandUuid())
                .orElseThrow(() -> new IllegalArgumentException("Car brand with id " + createCarSpecsRequest.carBrandUuid() + " not found"));
        var carModel = carModelRepository.findById(createCarSpecsRequest.carModelUuid())
                .orElseThrow(() -> new IllegalArgumentException("Car model with id " + createCarSpecsRequest.carModelUuid() + " not found"));

        CarSpecs carSpecs = new CarSpecs();
        carSpecs.setCarBrand(carBrand);
        carSpecs.setCarModel(carModel);
        carSpecs.setAcceleration(createCarSpecsRequest.acceleration());
        carSpecs.setTopSpeed(createCarSpecsRequest.topSpeed());
        carSpecs.setReleaseYear(createCarSpecsRequest.releaseYear());

        return carSpecsRepository.save(carSpecs);
    }
}


