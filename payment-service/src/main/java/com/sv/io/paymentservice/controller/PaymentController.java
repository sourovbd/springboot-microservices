package com.sv.io.paymentservice.controller;

import com.sv.io.paymentservice.entity.Payment;
import com.sv.io.paymentservice.model.PaymentMode;
import com.sv.io.paymentservice.model.PaymentRequest;
import com.sv.io.paymentservice.model.PaymentResponse;
import com.sv.io.paymentservice.repository.PaymentRepository;
import com.sv.io.paymentservice.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping
    public ResponseEntity<Long> doPayment(@RequestBody PaymentRequest paymentRequest) {

        long paymentId = paymentService.doPayment(paymentRequest);
        return new ResponseEntity<>(paymentId, HttpStatus.OK);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentDetails(@PathVariable long orderId) {

        Payment payment = paymentRepository.findByOrderId(orderId)
                 .orElseThrow();

        PaymentResponse paymentResponse = PaymentResponse
                .builder()
                .paymentMode(PaymentMode.valueOf(payment.getPaymentMode()))
                .paymentDate(payment.getPaymentDate())
                .status(payment.getPaymentStatus())
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .orderId(payment.getOrderId())
                .build();
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

}
