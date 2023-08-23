package com.sv.io.orderservice.service;

import com.sv.io.orderservice.entity.Order;
import com.sv.io.orderservice.external.client.ProductService;
import com.sv.io.orderservice.model.OrderRequest;
import com.sv.io.orderservice.model.OrderStatus;
import com.sv.io.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductService productService;

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

        log.info("Order is placed successfully with order id {}", order.getId());
        return order.getId();
    }
}
