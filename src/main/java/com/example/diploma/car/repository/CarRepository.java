package com.example.diploma.car.repository;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository<T> extends CrudRepository<T, UUID> {
    Optional<List<T>> getAllCars();
}
