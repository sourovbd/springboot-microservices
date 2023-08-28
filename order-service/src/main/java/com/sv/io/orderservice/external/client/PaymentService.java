package com.sv.io.orderservice.external.client;

import com.sv.io.orderservice.exception.CustomException;
import com.sv.io.orderservice.external.model.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@CircuitBreaker(name = "external", fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
    @RequestMapping
    public ResponseEntity<Long> doPayment(
            @RequestBody PaymentRequest paymentRequest
    );

    default void fallback(Exception e) {
        throw new CustomException("Payment Service is not available", "UNAVAILABLE", HttpStatus.SERVICE_UNAVAILABLE.value());
    }

}
