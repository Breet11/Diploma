package com.example.diploma.unit;

import com.example.diploma.rental.dto.CreateRentalRequest;
import com.example.diploma.utils.ApiErrorResponse;
import com.example.diploma.utils.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateRentalRequestTest {
    @Test
    void shouldDetectCompleteGuestContactData() {
        CreateRentalRequest request = new CreateRentalRequest(UUID.randomUUID(), 4L, "Ivan", "Petrov", "+373123456");
        assertTrue(request.hasGuestContactData());
    }

    @Test
    void shouldRejectBlankGuestContactData() {
        CreateRentalRequest request = new CreateRentalRequest(UUID.randomUUID(), 4L, "Ivan", " ", "+373123456");
        assertFalse(request.hasGuestContactData());
    }
}

class GlobalExceptionHandlerTest {
    private final GlobalExceptionHandler globalExceptionHandler = new GlobalExceptionHandler();

    @Test
    void shouldHandleIllegalArgumentException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/login");

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleIllegalArgument(
                new IllegalArgumentException("Bad request"),
                request
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bad request", response.getBody().message());
        assertEquals("/user/login", response.getBody().path());
    }

    @Test
    void shouldHandleUnexpectedException() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/rentals");

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleUnhandledException(request);
        ApiErrorResponse body = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(body);
        assertEquals("Internal server error", body.message());
    }

    @Test
    void shouldHandleValidationException() throws Exception {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "target");
        bindingResult.addError(new FieldError("target", "login", null, false, null, null, "must not be blank"));
        bindingResult.addError(new FieldError("target", "email", null, false, null, null, null));
        Method method = ValidationStub.class.getDeclaredMethod("validate", String.class);
        MethodParameter methodParameter = new MethodParameter(method, 0);
        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/user/register");

        ResponseEntity<ApiErrorResponse> response = globalExceptionHandler.handleValidationException(exception, request);
        ApiErrorResponse body = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(body);
        assertEquals("Validation failed", body.message());
        assertEquals("must not be blank", body.validationErrors().get("login"));
        assertEquals("Invalid value", body.validationErrors().get("email"));
    }

    static class ValidationStub {
        @SuppressWarnings("unused")
        void validate(String value) {
        }
    }
}

