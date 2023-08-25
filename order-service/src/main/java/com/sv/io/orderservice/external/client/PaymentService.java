package com.sv.io.orderservice.external.client;

import com.sv.io.orderservice.external.model.PaymentRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
    @RequestMapping
    public ResponseEntity<Long> doPayment(
            @RequestBody PaymentRequest paymentRequest
    );

}
