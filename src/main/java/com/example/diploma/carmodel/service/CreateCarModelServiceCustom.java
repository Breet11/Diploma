package com.example.diploma.carmodel.service;

import com.example.diploma.carmodel.dto.CreateCarModelRequest;
import com.example.diploma.carmodel.model.CarModel;
import com.example.diploma.carmodel.repository.CarModelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}


