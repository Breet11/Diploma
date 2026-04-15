package com.example.diploma.carmodel.controller;

import com.example.diploma.carmodel.dto.CreateCarModelRequest;
import com.example.diploma.carmodel.repository.CarModelRepository;
import com.example.diploma.carmodel.service.CreateCarModelService;
import com.example.diploma.utils.HTTP.HttpSpecs;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(HttpSpecs.CarModel.ROOT)
@RequiredArgsConstructor
public class CarModelController {
    private final CreateCarModelService createCarModelService;
    private final CarModelRepository carModelRepository;

    @GetMapping(HttpSpecs.CarModel.GET_ALL)
    public ResponseEntity<List<CarModelListItem>> getCarModels() {
        List<CarModelListItem> items = carModelRepository.findAll().stream()
                .map(model -> new CarModelListItem(model.getUuid(), model.getName()))
                .toList();
        return ResponseEntity.ok(items);
    }

    @PostMapping(HttpSpecs.CarModel.CREATE)
    public ResponseEntity<UUID> createCarModel(@Valid @RequestBody CreateCarModelRequest createCarModelRequest) {
        return ResponseEntity.ok(createCarModelService.createCarModel(createCarModelRequest).getUuid());
    }

    public record CarModelListItem(UUID uuid, String name) {
    }
}

