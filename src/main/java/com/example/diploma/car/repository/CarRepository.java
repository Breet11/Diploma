package com.example.diploma.car.repository;

import com.example.diploma.car.model.Car;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CarRepository extends CrudRepository<Car, UUID> {
	@Query("""
			select c.uuid as uuid,
			       e.uuid as engineUuid,
			       cs.uuid as carSpecsUuid,
				   cb.name as brand,
				   cm.name as model,
				   cs.releaseYear as releaseYear,
				   cs.topSpeed as topSpeed,
				   cs.acceleration as acceleration,
				   et.engineType as engineType,
				   es.fuelConsumption as fuelConsumption,
				   es.horsepower as horsepower,
				   es.torque as torque,
				   es.engineVolume as engineVolume,
				   c.price as hourlyRentalPrice,
				   c.imageBlob as imageBlob,
				   c.imageContentType as imageContentType,
				   c.available as available
			from Car c
			join c.carSpecs cs
			join cs.carBrand cb
			join cs.carModel cm
			join c.engine e
			join e.engineSpecs es
			join es.engineType et
			""")
	List<CarCatalogProjection> findCatalogItems();

	interface CarCatalogProjection {
		UUID getUuid();

		UUID getEngineUuid();

		UUID getCarSpecsUuid();

		String getBrand();

		String getModel();

		Long getReleaseYear();

		Long getTopSpeed();

		String getAcceleration();

		String getEngineType();

		String getFuelConsumption();

		Long getHorsepower();

		Long getTorque();

		java.math.BigDecimal getEngineVolume();

		Long getHourlyRentalPrice();

		byte[] getImageBlob();

		String getImageContentType();

		boolean isAvailable();
	}
}
