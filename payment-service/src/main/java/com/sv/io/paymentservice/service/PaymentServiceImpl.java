package com.sv.io.paymentservice.service;

import com.sv.io.paymentservice.entity.Payment;
import com.sv.io.paymentservice.model.PaymentRequest;
import com.sv.io.paymentservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public long doPayment(PaymentRequest paymentRequest) {
        log.info("Recording ");
        Payment payment = paymentRepository.save(PaymentRequest.dtoToEntity(paymentRequest));
        log.info("Payment completed with id: {}" + payment.getId());
        return payment.getId();
    }
}
