package com.example.diploma.car.service;

import com.example.diploma.car.dto.CarCatalogItemResponse;

import java.util.List;

public interface GetCarCatalogService {
    List<CarCatalogItemResponse> getCatalog();
}

