package com.sv.io.orderservice.external.model;

import com.sv.io.orderservice.external.entity.Payment;
import com.sv.io.orderservice.model.PaymentMode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private long orderId;
    private long amount;
    private String referenceNumber;
    private PaymentMode paymentMode;

    public static Payment dtoToEntity(PaymentRequest paymentRequest) {
        Payment payment = Payment
                .builder()
                .orderId(paymentRequest.getOrderId())
                .amount(paymentRequest.getAmount())
                .referenceNumber(paymentRequest.getReferenceNumber())
                .paymentMode(paymentRequest.getPaymentMode().name())
                .paymentDate(Instant.now())
                .paymentStatus(PaymentStatus.SUCCESS.name())
                .build();
        return payment;
    }
}
