package com.sv.io.orderservice.service;

import com.sv.io.orderservice.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);
}
