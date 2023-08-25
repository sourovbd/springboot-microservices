package com.sv.io.orderservice.service;

import com.sv.io.orderservice.entity.Order;
import com.sv.io.orderservice.exception.CustomException;
import com.sv.io.orderservice.external.client.PaymentService;
import com.sv.io.orderservice.external.client.ProductService;
import com.sv.io.orderservice.external.model.PaymentRequest;
import com.sv.io.orderservice.external.response.PaymentResponse;
import com.sv.io.orderservice.external.response.ProductResponse;
import com.sv.io.orderservice.model.OrderRequest;
import com.sv.io.orderservice.model.OrderResponse;
import com.sv.io.orderservice.model.OrderStatus;
import com.sv.io.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing order request {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating order with status CREATED.");
        Order order = Order
                .builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus(OrderStatus.CREATED.name())
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Calling payment service to complete payment.");
        PaymentRequest paymentRequest = PaymentRequest
                .builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment completed successfully. Order status is updated to PLACED.");
            orderStatus = OrderStatus.PLACED.name();
        } catch (Exception e) {
            log.info("Error orruced in payment. Updating order status to PAYMENT_FAILED.");
            orderStatus = OrderStatus.PAYMENT_FAILED.name();
        }

        log.info("Updating order with status depending on payment process.");
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);

        log.info("Order is placed successfully with order id {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order is not found by orderId: " + orderId,
                        "ORDER_NOT_FOUND", HttpStatus.NOT_FOUND.value()));

        log.info("Invoking product service to fetch the product details");

        ProductResponse productResponse =
                restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(),
                        ProductResponse.class);

        log.info("Invoking payment service to fetch the payment details");

        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(),
                PaymentResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails
                .builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .price(productResponse.getPrice())
                .quantity(productResponse.getQuantity())
                .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails
                .builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentDate(paymentResponse.getPaymentDate())
                .paymentStatus(paymentResponse.getStatus())
                .paymentMode(paymentResponse.getPaymentMode())
                .build();

        return OrderResponse
                .builder()
                .orderId(order.getId())
                .orderDate(order.getOrderDate())
                .amount(order.getAmount())
                .orderStatus(order.getOrderStatus())
                .productDetails(productDetails)
                .paymentDetails(paymentDetails)
                .build();
    }
}
