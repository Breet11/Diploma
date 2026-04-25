package com.example.diploma.rental.repository;

import com.example.diploma.rental.model.RentalOrder;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface RentalOrderRepository extends CrudRepository<RentalOrder, UUID> {
	List<RentalOrder> findAllByOrderByCreatedAtDesc();

	List<RentalOrder> findAllByUser_LoginOrderByCreatedAtDesc(String login);
}

