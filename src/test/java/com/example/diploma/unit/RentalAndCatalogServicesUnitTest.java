package com.example.diploma.unit;

import com.example.diploma.car.model.Car;
import com.example.diploma.car.repository.CarRepository;
import com.example.diploma.car.service.GetCarCatalogServiceCustom;
import com.example.diploma.carbrand.model.CarBrand;
import com.example.diploma.carmodel.model.CarModel;
import com.example.diploma.carspecs.model.CarSpecs;
import com.example.diploma.engine.model.Engine;
import com.example.diploma.enginespecs.model.EngineSpecs;
import com.example.diploma.enginetype.EngineType;
import com.example.diploma.loyalty.model.LoyaltyRule;
import com.example.diploma.loyalty.repository.LoyaltyRuleRepository;
import com.example.diploma.rental.dto.CalculateRentalPriceRequest;
import com.example.diploma.rental.dto.CreateRentalRequest;
import com.example.diploma.rental.model.RentalOrder;
import com.example.diploma.rental.repository.RentalOrderRepository;
import com.example.diploma.rental.service.CalculateRentalPriceService;
import com.example.diploma.rental.service.CalculateRentalPriceServiceCustom;
import com.example.diploma.rental.service.CreateRentalServiceCustom;
import com.example.diploma.rental.service.GetRentalOrdersServiceCustom;
import com.example.diploma.user.model.Role;
import com.example.diploma.user.model.User;
import com.example.diploma.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CalculateRentalPriceServiceCustomTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private LoyaltyRuleRepository loyaltyRuleRepository;
    @InjectMocks
    private CalculateRentalPriceServiceCustom calculateRentalPriceService;

    @Test
    void shouldCalculatePriceUsingMatchingLoyaltyRule() {
        UUID carUuid = UUID.randomUUID();
        Car car = new Car();
        car.setPrice(100L);
        LoyaltyRule loyaltyRule = new LoyaltyRule();
        loyaltyRule.setMultiplier(new BigDecimal("0.80"));
        when(carRepository.findById(carUuid)).thenReturn(Optional.of(car));
        when(loyaltyRuleRepository.findMatchingRules(5L)).thenReturn(List.of(loyaltyRule));

        var response = calculateRentalPriceService.calculatePrice(new CalculateRentalPriceRequest(carUuid, 5L));

        assertEquals(100L, response.baseHourlyPrice());
        assertEquals(5L, response.hours());
        assertEquals(new BigDecimal("0.80"), response.multiplier());
        assertEquals(400L, response.totalPrice());
    }

    @Test
    void shouldUseDefaultMultiplierWhenNoRuleMatches() {
        UUID carUuid = UUID.randomUUID();
        Car car = new Car();
        car.setPrice(75L);
        when(carRepository.findById(carUuid)).thenReturn(Optional.of(car));
        when(loyaltyRuleRepository.findMatchingRules(2L)).thenReturn(List.of());

        var response = calculateRentalPriceService.calculatePrice(new CalculateRentalPriceRequest(carUuid, 2L));

        assertEquals(BigDecimal.ONE, response.multiplier());
        assertEquals(150L, response.totalPrice());
    }

    @Test
    void shouldThrowWhenCarIsMissing() {
        UUID carUuid = UUID.randomUUID();
        when(carRepository.findById(carUuid)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> calculateRentalPriceService.calculatePrice(new CalculateRentalPriceRequest(carUuid, 2L))
        );

        assertEquals("Car with id " + carUuid + " not found", exception.getMessage());
    }
}

@ExtendWith(MockitoExtension.class)
class CreateRentalServiceCustomTest {
    @Mock
    private CarRepository carRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalOrderRepository rentalOrderRepository;
    @Mock
    private CalculateRentalPriceService calculateRentalPriceService;
    @InjectMocks
    private CreateRentalServiceCustom createRentalService;

    @Test
    void shouldCreateRentalForAuthenticatedUser() {
        UUID carUuid = UUID.randomUUID();
        Car car = buildCar("BMW", "M3", 150L);
        User user = new User(UUID.randomUUID(), "user@example.com", "user", "hashed", Role.USER);
        when(carRepository.findById(carUuid)).thenReturn(Optional.of(car));
        when(userRepository.findByLogin("user")).thenReturn(Optional.of(user));
        when(calculateRentalPriceService.calculatePrice(any())).thenReturn(new com.example.diploma.rental.dto.CalculateRentalPriceResponse(150L, 4L, BigDecimal.ONE, 600L));
        when(rentalOrderRepository.save(any())).thenAnswer(invocation -> {
            RentalOrder order = invocation.getArgument(0);
            order.setUuid(UUID.randomUUID());
            return order;
        });

        var response = createRentalService.createRental(new CreateRentalRequest(carUuid, 4L, null, null, null), "user");

        ArgumentCaptor<RentalOrder> orderCaptor = ArgumentCaptor.forClass(RentalOrder.class);
        verify(rentalOrderRepository).save(orderCaptor.capture());
        RentalOrder savedOrder = orderCaptor.getValue();
        assertEquals(user, savedOrder.getUser());
        assertNull(savedOrder.getPhone());
        assertEquals("NEW", savedOrder.getStatus());
        assertEquals(600L, response.totalPrice());
    }

    @Test
    void shouldCreateRentalForGuestWhenContactDataProvided() {
        UUID carUuid = UUID.randomUUID();
        Car car = buildCar("Audi", "A6", 200L);
        when(carRepository.findById(carUuid)).thenReturn(Optional.of(car));
        when(calculateRentalPriceService.calculatePrice(any())).thenReturn(new com.example.diploma.rental.dto.CalculateRentalPriceResponse(200L, 3L, BigDecimal.ONE, 600L));
        when(rentalOrderRepository.save(any())).thenAnswer(invocation -> {
            RentalOrder order = invocation.getArgument(0);
            order.setUuid(UUID.randomUUID());
            return order;
        });

        var response = createRentalService.createRental(new CreateRentalRequest(carUuid, 3L, "Ivan", "Petrov", "+373123456"), null);

        ArgumentCaptor<RentalOrder> orderCaptor = ArgumentCaptor.forClass(RentalOrder.class);
        verify(rentalOrderRepository).save(orderCaptor.capture());
        RentalOrder savedOrder = orderCaptor.getValue();
        assertEquals("Ivan", savedOrder.getFirstName());
        assertEquals("Petrov", savedOrder.getLastName());
        assertEquals("+373123456", savedOrder.getPhone());
        assertEquals("Заявка успешно создана. С вами свяжется менеджер.", response.message());
    }

    @Test
    void shouldRejectGuestWithoutContactData() {
        UUID carUuid = UUID.randomUUID();
        Car car = buildCar("Audi", "A6", 200L);
        when(carRepository.findById(carUuid)).thenReturn(Optional.of(car));
        when(calculateRentalPriceService.calculatePrice(any())).thenReturn(new com.example.diploma.rental.dto.CalculateRentalPriceResponse(200L, 3L, BigDecimal.ONE, 600L));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createRentalService.createRental(new CreateRentalRequest(carUuid, 3L, "Ivan", null, "+373123456"), null)
        );

        assertEquals("Guest must provide firstName, lastName and phone", exception.getMessage());
    }

    @Test
    void shouldFailWhenAuthenticatedUserCannotBeFound() {
        UUID carUuid = UUID.randomUUID();
        Car car = buildCar("Audi", "A6", 200L);
        when(carRepository.findById(carUuid)).thenReturn(Optional.of(car));
        when(userRepository.findByLogin("missing")).thenReturn(Optional.empty());
        when(calculateRentalPriceService.calculatePrice(any())).thenReturn(new com.example.diploma.rental.dto.CalculateRentalPriceResponse(200L, 3L, BigDecimal.ONE, 600L));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> createRentalService.createRental(new CreateRentalRequest(carUuid, 3L, null, null, null), "missing")
        );

        assertEquals("User with login missing not found", exception.getMessage());
    }

    private Car buildCar(String brandName, String modelName, long price) {
        CarBrand brand = new CarBrand();
        brand.setName(brandName);
        CarModel model = new CarModel();
        model.setName(modelName);
        CarSpecs specs = new CarSpecs();
        specs.setCarBrand(brand);
        specs.setCarModel(model);
        Car car = new Car();
        car.setCarSpecs(specs);
        car.setPrice(price);
        return car;
    }
}

@ExtendWith(MockitoExtension.class)
class GetRentalOrdersServiceCustomTest {
    @Mock
    private RentalOrderRepository rentalOrderRepository;
    @InjectMocks
    private GetRentalOrdersServiceCustom getRentalOrdersService;

    @Test
    void shouldMapAuthenticatedAndGuestOrdersForAdminView() {
        RentalOrder authenticatedOrder = new RentalOrder();
        authenticatedOrder.setUuid(UUID.randomUUID());
        authenticatedOrder.setUser(new User(UUID.randomUUID(), "auth@example.com", "authUser", "hashed", Role.USER));
        authenticatedOrder.setCar(buildCar("Tesla", "Model S", 300L));
        authenticatedOrder.setHours(5L);
        authenticatedOrder.setTotalPrice(1200L);
        authenticatedOrder.setStatus("NEW");
        authenticatedOrder.setCreatedAt(OffsetDateTime.now());

        RentalOrder guestOrder = new RentalOrder();
        guestOrder.setUuid(UUID.randomUUID());
        guestOrder.setFirstName("Guest");
        guestOrder.setLastName("User");
        guestOrder.setPhone("+373999999");
        guestOrder.setCar(buildCar("Toyota", "Camry", 100L));
        guestOrder.setHours(2L);
        guestOrder.setTotalPrice(200L);
        guestOrder.setStatus("NEW");
        guestOrder.setCreatedAt(OffsetDateTime.now());

        when(rentalOrderRepository.findAll()).thenReturn(List.of(authenticatedOrder, guestOrder));

        var result = getRentalOrdersService.getRentalOrders();

        assertEquals(2, result.size());
        assertEquals("authUser (auth@example.com)", result.getFirst().customer());
        assertEquals("-", result.getFirst().phone());
        assertEquals("Guest User", result.getLast().customer());
        assertEquals("+373999999", result.getLast().phone());
        assertEquals("Toyota Camry", result.getLast().car());
    }

    private Car buildCar(String brandName, String modelName, long price) {
        CarBrand brand = new CarBrand();
        brand.setName(brandName);
        CarModel model = new CarModel();
        model.setName(modelName);
        CarSpecs specs = new CarSpecs();
        specs.setCarBrand(brand);
        specs.setCarModel(model);
        Car car = new Car();
        car.setCarSpecs(specs);
        car.setPrice(price);
        return car;
    }
}

@ExtendWith(MockitoExtension.class)
class GetCarCatalogServiceCustomTest {
    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private GetCarCatalogServiceCustom getCarCatalogService;

    @Test
    void shouldMapCarsToCatalogItemsIncludingImage() {
        EngineType engineType = new EngineType();
        engineType.setEngineType("Hybrid");
        EngineSpecs engineSpecs = new EngineSpecs();
        engineSpecs.setEngineType(engineType);
        Engine engine = new Engine();
        engine.setEngineSpecs(engineSpecs);

        CarBrand brand = new CarBrand();
        brand.setName("Lexus");
        CarModel model = new CarModel();
        model.setName("RX");
        CarSpecs specs = new CarSpecs();
        specs.setCarBrand(brand);
        specs.setCarModel(model);
        specs.setReleaseYear(2024L);
        specs.setTopSpeed(210L);
        specs.setAcceleration("7.5s");

        Car car = new Car();
        car.setUuid(UUID.randomUUID());
        car.setCarSpecs(specs);
        car.setEngine(engine);
        car.setPrice(250L);
        car.setImageBlob(new byte[]{1, 2, 3});
        car.setImageContentType("image/png");
        car.setAvailable(true);

        when(carRepository.findAll()).thenReturn(List.of(car));

        var result = getCarCatalogService.getCatalog();

        assertEquals(1, result.size());
        assertEquals("Lexus", result.getFirst().brand());
        assertEquals("RX", result.getFirst().model());
        assertEquals("Hybrid", result.getFirst().engineType());
        assertEquals("AQID", result.getFirst().imageBase64());
        assertEquals("image/png", result.getFirst().imageContentType());
        assertTrue(result.getFirst().available());
    }

    @Test
    void shouldReturnNullImageWhenBlobIsMissing() {
        EngineType engineType = new EngineType();
        engineType.setEngineType("Diesel");
        EngineSpecs engineSpecs = new EngineSpecs();
        engineSpecs.setEngineType(engineType);
        Engine engine = new Engine();
        engine.setEngineSpecs(engineSpecs);

        CarBrand brand = new CarBrand();
        brand.setName("Skoda");
        CarModel model = new CarModel();
        model.setName("Octavia");
        CarSpecs specs = new CarSpecs();
        specs.setCarBrand(brand);
        specs.setCarModel(model);
        specs.setReleaseYear(2020L);
        specs.setTopSpeed(190L);
        specs.setAcceleration("8.1s");

        Car car = new Car();
        car.setUuid(UUID.randomUUID());
        car.setCarSpecs(specs);
        car.setEngine(engine);
        car.setPrice(120L);
        car.setAvailable(false);

        when(carRepository.findAll()).thenReturn(List.of(car));

        var result = getCarCatalogService.getCatalog();

        assertNull(result.getFirst().imageBase64());
        assertNull(result.getFirst().imageContentType());
    }
}

