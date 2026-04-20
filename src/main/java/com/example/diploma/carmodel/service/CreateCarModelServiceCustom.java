package com.example.diploma.carmodel.service;

import com.example.diploma.carmodel.dto.CreateCarModelRequest;
import com.example.diploma.carmodel.model.CarModel;
import com.example.diploma.carmodel.repository.CarModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateCarModelServiceCustom implements CreateCarModelService {
    private final CarModelRepository carModelRepository;

    @Override
    public CarModel createCarModel(CreateCarModelRequest createCarModelRequest) {
        CarModel carModel = new CarModel();
        carModel.setName(createCarModelRequest.name());
        return carModelRepository.save(carModel);
    }

    @Override
    public CarModel updateCarModel(UUID uuid, CreateCarModelRequest createCarModelRequest) {
        CarModel carModel = carModelRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Car model with id " + uuid + " not found"));
        carModel.setName(createCarModelRequest.name());
        return carModelRepository.save(carModel);
    }

    @Override
    public void deleteCarModel(UUID uuid) {
        CarModel carModel = carModelRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("Car model with id " + uuid + " not found"));
        carModelRepository.delete(carModel);
    }
}


