package com.sv.io.orderservice.service;

import com.sv.io.orderservice.model.OrderRequest;
import com.sv.io.orderservice.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}
