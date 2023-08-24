package com.sv.io.paymentservice.service;

import com.sv.io.paymentservice.model.PaymentRequest;

public interface PaymentService {
    long doPayment(PaymentRequest paymentRequest);
}
