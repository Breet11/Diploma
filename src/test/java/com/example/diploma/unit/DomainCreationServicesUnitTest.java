package com.example.diploma.unit;

import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;
import com.example.diploma.car.repository.CarRepository;
import com.example.diploma.car.service.CreateCarServiceCustom;
import com.example.diploma.carbrand.dto.CreateCarBrandRequest;
import com.example.diploma.carbrand.model.CarBrand;
import com.example.diploma.carbrand.repository.CarBrandRepository;
import com.example.diploma.carbrand.service.CreateCarBrandServiceCustom;
import com.example.diploma.carmodel.dto.CreateCarModelRequest;
import com.example.diploma.carmodel.model.CarModel;
import com.example.diploma.carmodel.repository.CarModelRepository;
import com.example.diploma.carmodel.service.CreateCarModelServiceCustom;
import com.example.diploma.carspecs.dto.CreateCarSpecsRequest;
import com.example.diploma.carspecs.model.CarSpecs;
import com.example.diploma.carspecs.repository.CarSpecsRepository;
import com.example.diploma.carspecs.service.CreateCarSpecsServiceCustom;
import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.model.Engine;
import com.example.diploma.engine.repository.EngineRepository;
import com.example.diploma.engine.service.CreateEngineServiceCustom;
import com.example.diploma.enginespecs.dto.CreateEngineSpecsRequest;
import com.example.diploma.enginespecs.model.EngineSpecs;
import com.example.diploma.enginespecs.repository.EngineSpecsRepository;
import com.example.diploma.enginespecs.service.CreateEngineSpecsServiceCustom;
import com.example.diploma.enginetype.EngineType;
import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;
import com.example.diploma.enginetype.repository.EngineTypeRepository;
import com.example.diploma.enginetype.service.CreateEngineTypeServiceCustom;
import com.example.diploma.loyalty.dto.CreateLoyaltyRuleRequest;
import com.example.diploma.loyalty.model.LoyaltyRule;
import com.example.diploma.loyalty.repository.LoyaltyRuleRepository;
import com.example.diploma.loyalty.service.CreateLoyaltyRuleServiceCustom;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SimpleCreationServicesTest {
    @Mock private CarBrandRepository carBrandRepository;
    @Mock private CarModelRepository carModelRepository;
    @Mock private EngineTypeRepository engineTypeRepository;
    @InjectMocks private CreateCarBrandServiceCustom createCarBrandService;
    @InjectMocks private CreateCarModelServiceCustom createCarModelService;
    @InjectMocks private CreateEngineTypeServiceCustom createEngineTypeService;

    @Test
    void shouldCreateCarBrand() {
        createCarBrandService.createCarBrand(new CreateCarBrandRequest("BMW"));
        ArgumentCaptor<CarBrand> captor = ArgumentCaptor.forClass(CarBrand.class);
        verify(carBrandRepository).save(captor.capture());
        assertEquals("BMW", captor.getValue().getName());
    }

    @Test
    void shouldListAllCarBrands() {
        CarBrand first = new CarBrand();
        first.setName("BMW");
        CarBrand second = new CarBrand();
        second.setName("Audi");
        when(carBrandRepository.findAll()).thenReturn(List.of(first, second));

        var result = createCarBrandService.getAllCarBrands();

        assertEquals(2, result.size());
        assertEquals("BMW", result.getFirst().getName());
    }

    @Test
    void shouldCreateCarModel() {
        createCarModelService.createCarModel(new CreateCarModelRequest("A6"));
        ArgumentCaptor<CarModel> captor = ArgumentCaptor.forClass(CarModel.class);
        verify(carModelRepository).save(captor.capture());
        assertEquals("A6", captor.getValue().getName());
    }

    @Test
    void shouldCreateEngineType() {
        createEngineTypeService.createEngineType(new CreateEngineTypeRequest("Diesel"));
        ArgumentCaptor<EngineType> captor = ArgumentCaptor.forClass(EngineType.class);
        verify(engineTypeRepository).save(captor.capture());
        assertEquals("Diesel", captor.getValue().getEngineType());
    }
}

@ExtendWith(MockitoExtension.class)
class ComplexCreationServicesTest {
    @Mock private EngineTypeRepository engineTypeRepository;
    @Mock private EngineSpecsRepository engineSpecsRepository;
    @Mock private EngineRepository engineRepository;
    @Mock private CarBrandRepository carBrandRepository;
    @Mock private CarModelRepository carModelRepository;
    @Mock private CarSpecsRepository carSpecsRepository;
    @Mock private CarRepository carRepository;
    @Mock private LoyaltyRuleRepository loyaltyRuleRepository;

    @InjectMocks private CreateEngineSpecsServiceCustom createEngineSpecsService;
    @InjectMocks private CreateEngineServiceCustom createEngineService;
    @InjectMocks private CreateCarSpecsServiceCustom createCarSpecsService;
    @InjectMocks private CreateCarServiceCustom createCarService;
    @InjectMocks private CreateLoyaltyRuleServiceCustom createLoyaltyRuleService;

    @Test
    void shouldCreateEngineSpecs() {
        UUID engineTypeUuid = UUID.randomUUID();
        EngineType engineType = new EngineType();
        engineType.setUuid(engineTypeUuid);
        when(engineTypeRepository.findById(engineTypeUuid)).thenReturn(Optional.of(engineType));

        createEngineSpecsService.createEngineSpecs(new CreateEngineSpecsRequest(
                engineTypeUuid,
                "7 l/100km",
                200L,
                320L,
                new BigDecimal("2.00")
        ));

        ArgumentCaptor<EngineSpecs> captor = ArgumentCaptor.forClass(EngineSpecs.class);
        verify(engineSpecsRepository).save(captor.capture());
        assertEquals(engineType, captor.getValue().getEngineType());
        assertEquals(new BigDecimal("2.00"), captor.getValue().getEngineVolume());
    }

    @Test
    void shouldThrowWhenEngineTypeForSpecsIsMissing() {
        UUID engineTypeUuid = UUID.randomUUID();
        when(engineTypeRepository.findById(engineTypeUuid)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createEngineSpecsService.createEngineSpecs(new CreateEngineSpecsRequest(engineTypeUuid, "7 l/100km", 200L, 320L, new BigDecimal("2.00")))
        );

        assertEquals("Engine type with id " + engineTypeUuid + " not found", exception.getMessage());
    }

    @Test
    void shouldCreateEngine() {
        UUID engineTypeUuid = UUID.randomUUID();
        EngineType engineType = new EngineType();
        engineType.setUuid(engineTypeUuid);
        when(engineTypeRepository.findById(engineTypeUuid)).thenReturn(Optional.of(engineType));

        createEngineService.createEngine(new CreateEngineRequest(
                "2.0 TDI",
                engineTypeUuid,
                "5 l/100km",
                190L,
                400L,
                new BigDecimal("2.00")
        ));

        ArgumentCaptor<Engine> captor = ArgumentCaptor.forClass(Engine.class);
        verify(engineRepository).save(captor.capture());
        assertEquals("2.0 TDI", captor.getValue().getEngineName());
        assertEquals(engineType, captor.getValue().getEngineSpecs().getEngineType());
    }

    @Test
    void shouldCreateCarSpecs() {
        UUID brandUuid = UUID.randomUUID();
        UUID modelUuid = UUID.randomUUID();
        CarBrand brand = new CarBrand();
        CarModel model = new CarModel();
        when(carBrandRepository.findById(brandUuid)).thenReturn(Optional.of(brand));
        when(carModelRepository.findById(modelUuid)).thenReturn(Optional.of(model));

        createCarSpecsService.createCarSpecs(new CreateCarSpecsRequest(brandUuid, modelUuid, "6.5s", 240L, 2023L));

        ArgumentCaptor<CarSpecs> captor = ArgumentCaptor.forClass(CarSpecs.class);
        verify(carSpecsRepository).save(captor.capture());
        assertEquals(brand, captor.getValue().getCarBrand());
        assertEquals(model, captor.getValue().getCarModel());
        assertEquals(2023L, captor.getValue().getReleaseYear());
    }

    @Test
    void shouldThrowWhenCarBrandIsMissing() {
        UUID brandUuid = UUID.randomUUID();
        UUID modelUuid = UUID.randomUUID();
        when(carBrandRepository.findById(brandUuid)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createCarSpecsService.createCarSpecs(new CreateCarSpecsRequest(brandUuid, modelUuid, "6.5s", 240L, 2023L))
        );

        assertEquals("Car brand with id " + brandUuid + " not found", exception.getMessage());
    }

    @Test
    void shouldCreateCarWithoutImage() {
        UUID engineUuid = UUID.randomUUID();
        UUID specsUuid = UUID.randomUUID();
        Engine engine = new Engine();
        CarSpecs specs = new CarSpecs();
        when(engineRepository.findById(engineUuid)).thenReturn(Optional.of(engine));
        when(carSpecsRepository.findById(specsUuid)).thenReturn(Optional.of(specs));

        createCarService.createCar(new CreateCarRequest(engineUuid, specsUuid, 150L, true));

        ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository).save(captor.capture());
        assertEquals(150L, captor.getValue().getPrice());
        assertEquals(true, captor.getValue().isAvailable());
        assertNull(captor.getValue().getImageBlob());
    }

    @Test
    void shouldCreateCarWithUploadedImage() {
        UUID engineUuid = UUID.randomUUID();
        UUID specsUuid = UUID.randomUUID();
        Engine engine = new Engine();
        CarSpecs specs = new CarSpecs();
        when(engineRepository.findById(engineUuid)).thenReturn(Optional.of(engine));
        when(carSpecsRepository.findById(specsUuid)).thenReturn(Optional.of(specs));
        MockMultipartFile file = new MockMultipartFile("image", "car.png", "image/png", new byte[]{1, 2, 3});

        createCarService.createCar(new CreateCarRequest(engineUuid, specsUuid, 150L, true), file);

        ArgumentCaptor<Car> captor = ArgumentCaptor.forClass(Car.class);
        verify(carRepository).save(captor.capture());
        assertArrayEquals(new byte[]{1, 2, 3}, captor.getValue().getImageBlob());
        assertEquals("image/png", captor.getValue().getImageContentType());
    }

    @Test
    void shouldThrowWhenCarEngineIsMissing() {
        UUID engineUuid = UUID.randomUUID();
        UUID specsUuid = UUID.randomUUID();
        when(engineRepository.findById(engineUuid)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createCarService.createCar(new CreateCarRequest(engineUuid, specsUuid, 150L, true))
        );

        assertEquals("Engine with id " + engineUuid + " not found", exception.getMessage());
    }

    @Test
    void shouldValidateLoyaltyRuleHourRange() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createLoyaltyRuleService.createLoyaltyRule(new CreateLoyaltyRuleRequest(10L, 5L, new BigDecimal("0.80"), true))
        );

        assertEquals("maxHours must be greater than or equal to minHours", exception.getMessage());
    }

    @Test
    void shouldCreateLoyaltyRule() {
        createLoyaltyRuleService.createLoyaltyRule(new CreateLoyaltyRuleRequest(5L, 10L, new BigDecimal("0.80"), true));

        ArgumentCaptor<LoyaltyRule> captor = ArgumentCaptor.forClass(LoyaltyRule.class);
        verify(loyaltyRuleRepository).save(captor.capture());
        assertEquals(5L, captor.getValue().getMinHours());
        assertEquals(10L, captor.getValue().getMaxHours());
        assertEquals(new BigDecimal("0.80"), captor.getValue().getMultiplier());
        assertEquals(true, captor.getValue().isActive());
    }
}


