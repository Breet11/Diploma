package com.example.diploma.unit;

import com.example.diploma.car.controller.CarController;
import com.example.diploma.car.dto.CarCatalogItemResponse;
import com.example.diploma.car.dto.CreateCarRequest;
import com.example.diploma.car.model.Car;
import com.example.diploma.car.service.CreateCarService;
import com.example.diploma.car.service.GetCarCatalogService;
import com.example.diploma.carbrand.controller.CarBrandController;
import com.example.diploma.carbrand.dto.CreateCarBrandRequest;
import com.example.diploma.carbrand.model.CarBrand;
import com.example.diploma.carbrand.service.CreateCarBrandService;
import com.example.diploma.carmodel.controller.CarModelController;
import com.example.diploma.carmodel.dto.CreateCarModelRequest;
import com.example.diploma.carmodel.model.CarModel;
import com.example.diploma.carmodel.repository.CarModelRepository;
import com.example.diploma.carmodel.service.CreateCarModelService;
import com.example.diploma.carspecs.controller.CarSpecsController;
import com.example.diploma.carspecs.dto.CreateCarSpecsRequest;
import com.example.diploma.carspecs.model.CarSpecs;
import com.example.diploma.carspecs.repository.CarSpecsRepository;
import com.example.diploma.carspecs.service.CreateCarSpecsService;
import com.example.diploma.engine.controller.EngineController;
import com.example.diploma.engine.dto.CreateEngineRequest;
import com.example.diploma.engine.model.Engine;
import com.example.diploma.engine.repository.EngineRepository;
import com.example.diploma.engine.service.CreateEngineService;
import com.example.diploma.enginespecs.controller.EngineSpecsController;
import com.example.diploma.enginespecs.dto.CreateEngineSpecsRequest;
import com.example.diploma.enginespecs.model.EngineSpecs;
import com.example.diploma.enginespecs.repository.EngineSpecsRepository;
import com.example.diploma.enginespecs.service.CreateEngineSpecsService;
import com.example.diploma.enginetype.EngineType;
import com.example.diploma.enginetype.controller.EngineTypeController;
import com.example.diploma.enginetype.dto.CreateEngineTypeRequest;
import com.example.diploma.enginetype.repository.EngineTypeRepository;
import com.example.diploma.enginetype.service.CreateEngineTypeService;
import com.example.diploma.loyalty.controller.LoyaltyRuleController;
import com.example.diploma.loyalty.dto.CreateLoyaltyRuleRequest;
import com.example.diploma.loyalty.model.LoyaltyRule;
import com.example.diploma.loyalty.repository.LoyaltyRuleRepository;
import com.example.diploma.loyalty.service.CreateLoyaltyRuleService;
import com.example.diploma.rental.controller.RentalController;
import com.example.diploma.rental.dto.CalculateRentalPriceRequest;
import com.example.diploma.rental.dto.CalculateRentalPriceResponse;
import com.example.diploma.rental.dto.CreateRentalRequest;
import com.example.diploma.rental.dto.CreateRentalResponse;
import com.example.diploma.rental.dto.RentalOrderAdminListItemResponse;
import com.example.diploma.rental.service.CalculateRentalPriceService;
import com.example.diploma.rental.service.CreateRentalService;
import com.example.diploma.rental.service.GetRentalOrdersService;
import com.example.diploma.user.controller.AuthController;
import com.example.diploma.user.controller.ProfileController;
import com.example.diploma.user.dto.AuthMessageResponseDto;
import com.example.diploma.user.dto.LoginRequestDto;
import com.example.diploma.user.dto.LoginResponseDto;
import com.example.diploma.user.dto.ProfileResponseDto;
import com.example.diploma.user.dto.RegisterRequestDto;
import com.example.diploma.user.service.AuthService;
import com.example.diploma.user.service.GetProfileService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BasicControllersUnitTest {
    @Mock private AuthService authService;
    @Mock private GetProfileService getProfileService;
    @Mock private CreateRentalService createRentalService;
    @Mock private CalculateRentalPriceService calculateRentalPriceService;
    @Mock private GetRentalOrdersService getRentalOrdersService;
    @InjectMocks private AuthController authController;
    @InjectMocks private ProfileController profileController;
    @InjectMocks private RentalController rentalController;

    @Test
    void authControllerShouldReturnLoginResponse() {
        LoginRequestDto request = new LoginRequestDto("john", "encrypted");
        LoginResponseDto response = new LoginResponseDto("token", "Bearer", 1000L, "USER");
        when(authService.login(request)).thenReturn(response);

        var entity = authController.login(request);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void authControllerShouldReturnCreatedOnRegister() {
        RegisterRequestDto request = new RegisterRequestDto("john@example.com", "john", "encrypted");
        AuthMessageResponseDto response = new AuthMessageResponseDto("ok");
        when(authService.register(request)).thenReturn(response);

        var entity = authController.register(request);

        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void profileControllerShouldReturnCurrentProfile() {
        ProfileResponseDto response = new ProfileResponseDto("user@example.com", "user", "USER");
        when(getProfileService.getProfile("user")).thenReturn(response);

        var entity = profileController.me(new UsernamePasswordAuthenticationToken("user", null));

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void rentalControllerShouldReturnAdminOrders() {
        List<RentalOrderAdminListItemResponse> responses = List.of(
                new RentalOrderAdminListItemResponse(UUID.randomUUID(), "User", "+373", "BMW M3", 3L, 300L, "NEW", OffsetDateTime.now())
        );
        when(getRentalOrdersService.getRentalOrders()).thenReturn(responses);

        var entity = rentalController.getRentalOrdersForAdmin();

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(responses, entity.getBody());
    }

    @Test
    void rentalControllerShouldReturnCalculatedPrice() {
        CalculateRentalPriceRequest request = new CalculateRentalPriceRequest(UUID.randomUUID(), 4L);
        CalculateRentalPriceResponse response = new CalculateRentalPriceResponse(100L, 4L, BigDecimal.ONE, 400L);
        when(calculateRentalPriceService.calculatePrice(request)).thenReturn(response);

        var entity = rentalController.calculatePrice(request);

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }

    @Test
    void rentalControllerShouldPassCurrentLoginToCreateService() {
        CreateRentalRequest request = new CreateRentalRequest(UUID.randomUUID(), 4L, null, null, null);
        CreateRentalResponse response = new CreateRentalResponse(UUID.randomUUID(), 400L, "ok");
        when(createRentalService.createRental(request, "john")).thenReturn(response);

        var entity = rentalController.createRental(request, new UsernamePasswordAuthenticationToken("john", null));

        assertEquals(HttpStatus.OK, entity.getStatusCode());
        assertEquals(response, entity.getBody());
    }
}

@ExtendWith(MockitoExtension.class)
class CatalogControllersUnitTest {
    @Mock private CreateCarService createCarService;
    @Mock private GetCarCatalogService getCarCatalogService;
    @Mock private CreateCarBrandService createCarBrandService;
    @Mock private CreateCarModelService createCarModelService;
    @Mock private CarModelRepository carModelRepository;
    @Mock private CreateCarSpecsService createCarSpecsService;
    @Mock private CarSpecsRepository carSpecsRepository;
    @Mock private CreateEngineService createEngineService;
    @Mock private EngineRepository engineRepository;
    @Mock private CreateEngineSpecsService createEngineSpecsService;
    @Mock private EngineSpecsRepository engineSpecsRepository;
    @Mock private CreateEngineTypeService createEngineTypeService;
    @Mock private EngineTypeRepository engineTypeRepository;
    @Mock private CreateLoyaltyRuleService createLoyaltyRuleService;
    @Mock private LoyaltyRuleRepository loyaltyRuleRepository;
    @InjectMocks private CarController carController;
    @InjectMocks private CarBrandController carBrandController;
    @InjectMocks private CarModelController carModelController;
    @InjectMocks private CarSpecsController carSpecsController;
    @InjectMocks private EngineController engineController;
    @InjectMocks private EngineSpecsController engineSpecsController;
    @InjectMocks private EngineTypeController engineTypeController;
    @InjectMocks private LoyaltyRuleController loyaltyRuleController;

    @Test
    void carControllerShouldReturnCatalog() {
        List<CarCatalogItemResponse> response = List.of(
                new CarCatalogItemResponse(UUID.randomUUID(), "BMW", "M3", 2024L, 250L, "4.0s", "Petrol", 200L, null, null, true)
        );
        when(getCarCatalogService.getCatalog()).thenReturn(response);

        var entity = carController.getCarsCatalog();

        assertEquals(response, entity.getBody());
    }

    @Test
    void carControllerShouldCreateCar() {
        UUID carUuid = UUID.randomUUID();
        Car car = new Car();
        car.setUuid(carUuid);
        CreateCarRequest request = new CreateCarRequest(UUID.randomUUID(), UUID.randomUUID(), 150L, true);
        MockMultipartFile image = new MockMultipartFile("image", "car.png", "image/png", new byte[]{1});
        when(createCarService.createCar(request, image)).thenReturn(car);

        var entity = carController.createCar(request, image);

        assertEquals(carUuid, entity.getBody());
    }

    @Test
    void carBrandControllerShouldReturnMappedBrands() {
        CarBrand brand = new CarBrand();
        brand.setUuid(UUID.randomUUID());
        brand.setName("Audi");
        when(createCarBrandService.getAllCarBrands()).thenReturn(List.of(brand));

        var entity = carBrandController.getCarBrands();

        assertNotNull(entity.getBody());
        assertEquals(1, entity.getBody().size());
    }

    @Test
    void carBrandControllerShouldCreateBrand() {
        CarBrand brand = new CarBrand();
        brand.setUuid(UUID.randomUUID());
        when(createCarBrandService.createCarBrand(any())).thenReturn(brand);

        var entity = carBrandController.createCarBrand(new CreateCarBrandRequest("Audi"));

        assertEquals(brand.getUuid(), entity.getBody());
    }

    @Test
    void carModelControllerShouldReturnModels() {
        CarModel model = new CarModel();
        model.setUuid(UUID.randomUUID());
        model.setName("A6");
        when(carModelRepository.findAll()).thenReturn(List.of(model));

        var entity = carModelController.getCarModels();

        assertNotNull(entity.getBody());
        assertEquals(1, entity.getBody().size());
    }

    @Test
    void carModelControllerShouldCreateModel() {
        CarModel model = new CarModel();
        model.setUuid(UUID.randomUUID());
        when(createCarModelService.createCarModel(any())).thenReturn(model);

        var entity = carModelController.createCarModel(new CreateCarModelRequest("A6"));

        assertEquals(model.getUuid(), entity.getBody());
    }

    @Test
    void carSpecsControllerShouldReturnSpecs() {
        CarBrand brand = new CarBrand();
        brand.setName("BMW");
        CarModel model = new CarModel();
        model.setName("M5");
        CarSpecs specs = new CarSpecs();
        specs.setUuid(UUID.randomUUID());
        specs.setCarBrand(brand);
        specs.setCarModel(model);
        specs.setAcceleration("3.5s");
        specs.setTopSpeed(250L);
        specs.setReleaseYear(2023L);
        when(carSpecsRepository.findAll()).thenReturn(List.of(specs));

        var entity = carSpecsController.getCarSpecs();

        assertNotNull(entity.getBody());
        assertEquals(1, entity.getBody().size());
    }

    @Test
    void carSpecsControllerShouldCreateSpecs() {
        CarSpecs specs = new CarSpecs();
        specs.setUuid(UUID.randomUUID());
        when(createCarSpecsService.createCarSpecs(any())).thenReturn(specs);

        var entity = carSpecsController.createCarSpecs(new CreateCarSpecsRequest(UUID.randomUUID(), UUID.randomUUID(), "5.0s", 220L, 2024L));

        assertEquals(specs.getUuid(), entity.getBody());
    }

    @Test
    void engineControllerShouldReturnEngines() {
        EngineType engineType = new EngineType();
        engineType.setEngineType("Petrol");
        EngineSpecs engineSpecs = new EngineSpecs();
        engineSpecs.setEngineType(engineType);
        engineSpecs.setFuelConsumption("7 l/100km");
        engineSpecs.setHorsepower(190L);
        engineSpecs.setTorque(320L);
        engineSpecs.setEngineVolume(new BigDecimal("2.00"));
        Engine engine = new Engine();
        engine.setUuid(UUID.randomUUID());
        engine.setEngineName("2.0 TFSI");
        engine.setEngineSpecs(engineSpecs);
        when(engineRepository.findAll()).thenReturn(List.of(engine));

        var entity = engineController.getEngines();

        assertNotNull(entity.getBody());
        assertEquals(1, entity.getBody().size());
    }

    @Test
    void engineControllerShouldCreateEngine() {
        Engine engine = new Engine();
        engine.setUuid(UUID.randomUUID());
        when(createEngineService.createEngine(any())).thenReturn(engine);

        var entity = engineController.createEngine(new CreateEngineRequest("2.0 TFSI", UUID.randomUUID(), "7 l/100km", 190L, 320L, new BigDecimal("2.00")));

        assertEquals(engine.getUuid(), entity.getBody());
    }

    @Test
    void engineSpecsControllerShouldReturnSpecs() {
        EngineType engineType = new EngineType();
        engineType.setEngineType("Hybrid");
        EngineSpecs specs = new EngineSpecs();
        specs.setUuid(UUID.randomUUID());
        specs.setEngineType(engineType);
        specs.setFuelConsumption("5 l/100km");
        specs.setHorsepower(250L);
        specs.setTorque(400L);
        specs.setEngineVolume(new BigDecimal("3.00"));
        when(engineSpecsRepository.findAll()).thenReturn(List.of(specs));

        var entity = engineSpecsController.getEngineSpecs();

        assertNotNull(entity.getBody());
        assertEquals(1, entity.getBody().size());
    }

    @Test
    void engineSpecsControllerShouldCreateSpecs() {
        EngineSpecs specs = new EngineSpecs();
        specs.setUuid(UUID.randomUUID());
        when(createEngineSpecsService.createEngineSpecs(any())).thenReturn(specs);

        var entity = engineSpecsController.createEngineSpecs(new CreateEngineSpecsRequest(UUID.randomUUID(), "5 l/100km", 250L, 400L, new BigDecimal("3.00")));

        assertEquals(specs.getUuid(), entity.getBody());
    }

    @Test
    void engineTypeControllerShouldReturnTypes() {
        EngineType type = new EngineType();
        type.setUuid(UUID.randomUUID());
        type.setEngineType("Electric");
        when(engineTypeRepository.findAll()).thenReturn(List.of(type));

        var entity = engineTypeController.getEngineTypes();

        assertNotNull(entity.getBody());
        assertEquals(1, entity.getBody().size());
    }

    @Test
    void engineTypeControllerShouldCreateType() {
        EngineType type = new EngineType();
        type.setUuid(UUID.randomUUID());
        when(createEngineTypeService.createEngineType(any())).thenReturn(type);

        var entity = engineTypeController.createEngineType(new CreateEngineTypeRequest("Electric"));

        assertEquals(type.getUuid(), entity.getBody());
    }

    @Test
    void loyaltyRuleControllerShouldReturnRules() {
        LoyaltyRule rule = new LoyaltyRule();
        rule.setUuid(UUID.randomUUID());
        rule.setMinHours(5L);
        rule.setMaxHours(10L);
        rule.setMultiplier(new BigDecimal("0.80"));
        rule.setActive(true);
        when(loyaltyRuleRepository.findAll()).thenReturn(List.of(rule));

        var entity = loyaltyRuleController.getLoyaltyRules();

        assertNotNull(entity.getBody());
        assertEquals(1, entity.getBody().size());
    }

    @Test
    void loyaltyRuleControllerShouldCreateRule() {
        LoyaltyRule rule = new LoyaltyRule();
        rule.setUuid(UUID.randomUUID());
        when(createLoyaltyRuleService.createLoyaltyRule(any())).thenReturn(rule);

        var entity = loyaltyRuleController.createLoyaltyRule(new CreateLoyaltyRuleRequest(5L, 10L, new BigDecimal("0.80"), true));

        assertEquals(rule.getUuid(), entity.getBody());
    }
}


